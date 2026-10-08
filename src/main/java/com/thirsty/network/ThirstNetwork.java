package com.thirsty.network;

import com.thirsty.ThirstWasTaken;
import com.thirsty.api.config.CommonConfig;
import com.thirsty.misc.MathHelper;
import com.thirsty.purity.WaterPurity;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class ThirstNetwork {
    public static final String MOD_ID_P = ThirstWasTaken.MOD_ID;

    public static final ResourceLocation MOD_PACKET_ID = new ResourceLocation(MOD_ID_P, "hand_drink_pack");

    public static void onInitialize() {
        registerServerPackets();
    }

    public static void registerServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(MOD_PACKET_ID,
                (MinecraftServer server, ServerPlayer player_, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) -> {
                    server.execute(() -> {
                        BlockPos blockPos_get = buf.readBlockPos();
                        Player player = player_;
                        Level level = player.level();

                        if(!player.isCrouching() || player.isInvulnerable())
                            return;

                        if(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
                            return;


                        BlockPos blockPos = MathHelper.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY).getBlockPos();
                        if(!level.getFluidState(blockPos).is(FluidTags.WATER))
                            return;

                        if (player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) > 25)
                            return;

                        ThirstData thirstData = PLAYER_THIRST.get(player);

                        if(thirstData.getThirst()==20)
                            return;

                        int purity = WaterPurity.getBlockPurity(level, blockPos);
                        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.0F, 1.0F);

                        if(WaterPurity.givePurityEffects(player, purity))
                            ThirstData.drink(player, AutoConfig.getConfigHolder(CommonConfig.class).getConfig().HAND_DRINKING_HYDRATION, AutoConfig.getConfigHolder(CommonConfig.class).getConfig().HAND_DRINKING_QUENCHED);
                    });
                }
        );
    }
}
