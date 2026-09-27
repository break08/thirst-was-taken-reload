package com.thirsty.misc;

import com.thirsty.config.CommonConfig;
import com.thirsty.network.ThirstModPacketHandler;
import com.thirsty.purity.WaterPurity;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class PlayerThirstHelper {
    public static boolean checkTombstoneEffects = false;
    public static boolean checkFDEffects = false;
    public static boolean checkLetsDoBakeryEffects = false;
    public static boolean checkLetsDoBreweryEffects = false;
    public static void drink(ItemStack item, Player player) {
        if(ThirstHelper.itemRestoresThirst(item) && WaterPurity.givePurityEffects(player, item)) {
            drink(player, ThirstHelper.getThirst(item), ThirstHelper.getQuenched(item));
        }
    }

    public static void drink(Player player, int thirst, int quenched)
    {
        ThirstData thirstData = PLAYER_THIRST.get(player);
        int extra_quenched = Math.max(thirstData.getThirst() + thirst - 20, 0);
        if(!AutoConfig.getConfigHolder(CommonConfig.class).getConfig().EXTRA_HYDRATION_CONVERT_TO_QUENCHED)
            extra_quenched = 0;
        thirstData.setThirst(Math.min(thirstData.getThirst() + thirst, 20));
        thirstData.setQuenched(Math.min(thirstData.getThirst() + quenched + extra_quenched, thirstData.getThirst()));
    }

    public void tick(Player player)
    {
        ThirstData thirstData = PLAYER_THIRST.get(player);
        float exhaustion = thirstData.getExhaustion();
        int quenched = thirstData.getQuenched();
        int thirst = thirstData.getThirst();
        int syncTimer = thirstData.getSyncTimer();
        int damageTimer = thirstData.getDamageTimer();
        boolean init = thirstData.isInit();
        Difficulty difficulty = player.level().getDifficulty();

        if(player.getAbilities().invulnerable)
            return;

        if(!thirstData.isShouldTickThirst()) {
            if (init) {
                init = false;
                updateThirstData(player);
            }
            return;
        }

        if(checkTombstoneEffects && player.getActiveEffects().stream().anyMatch(e -> e.getDescriptionId().contains("ghostly_shape")))
            return;

        boolean isNourished = checkFDEffects && player.hasEffect(ModEffects.NOURISHMENT.get());
        boolean isHunger = player.hasEffect(MobEffects.HUNGER);
        boolean isStuffed = checkLetsDoBakeryEffects &&
                player.getActiveEffects().stream().anyMatch(e -> e.getDescriptionId().contains("stuffed"));
        boolean isSaturated = checkLetsDoBreweryEffects &&
                player.getActiveEffects().stream().anyMatch(e -> e.getDescriptionId().contains("saturated"));
        boolean isSitting = player.isPassenger();

        if(CommonConfig.DEPLETES_WHEN_NAUSEA.get() && player.getActiveEffects().stream().anyMatch(e->e.getEffect().equals(MobEffects.CONFUSION))){
            addExhaustion(player,0.06F);
        }

        if(isHunger){
            thirstData.setExhaustion(0.005F * (float)(player.getEffect(MobEffects.HUNGER).getAmplifier() + 1) *
                    ThirstHelper.getExhaustionBiomeModifier(player) *
                    ThirstHelper.getExhaustionFireProtModifier(player)*
                    ThirstHelper.getExhaustionFireResistanceModifier(player));
        }

        if (!isSitting && !isNourished && !isStuffed && !isSaturated)
        {
            updateExhaustion(player);
        }

        if (exhaustion > 4)
        {
            exhaustion -= 4;
            if (quenched > 0)
            {
                quenched--;
            }
            else if (difficulty != Difficulty.PEACEFUL || CommonConfig.THIRST_DEPLETION_IN_PEACEFUL.get())
            {
                thirst = Math.max(thirst - 1, 0);
            }
        }

        ++syncTimer;
        if(syncTimer > 10 && !player.level().isClientSide())
        {
            if(difficulty == Difficulty.PEACEFUL && !CommonConfig.THIRST_DEPLETION_IN_PEACEFUL.get()){
                thirst = Math.min(thirst + 1,20);
            }

            final float angle = Mth.wrapDegrees(player.getXRot());
            if (angle <= -80  && player.level().isRainingAt(player.blockPosition().above()) && CommonConfig.CAN_DRINK_RAIN_WATETR.get())
            {
                thirst = Math.min(thirst + 1,20);
                quenched = Math.min(quenched +1,20);
            }

            updateThirstData(player);
            syncTimer = 0;
        }

        if (thirst <= 0)
        {
            ++damageTimer;
            if (damageTimer >= 40)
            {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 0 && difficulty == Difficulty.NORMAL)
                {
                    player.hurt(ModDamageSource.getDamageSource(player.level(),ModDamageSource.DIE_OF_THIRST_KEY), 1.0F);
                }

                damageTimer = 0;
            }
        }
    }

    void updateExhaustion(Player player)
    {
        ThirstData thirstData = PLAYER_THIRST.get(player);
        boolean exhaustionRecalculate = thirstData.isExhaustionRecalculate();
        float prevTickExhaustion = thirstData.getPrevTickExhaustion();
        float hungerExhaustion = player.getFoodData().getExhaustionLevel();
        float normalizedHungerExhaustion = hungerExhaustion < prevTickExhaustion ? (exhaustionRecalculate ? hungerExhaustion + 4.0F : hungerExhaustion) : hungerExhaustion;
        if(exhaustionRecalculate){
            exhaustionRecalculate = false;
        }
        float deltaExhaustion = normalizedHungerExhaustion - prevTickExhaustion;
        this.addExhaustion(player, deltaExhaustion);
        prevTickExhaustion = hungerExhaustion;
    }

    public void updateThirstData(Player player)
    {
        ThirstData thirstData = PLAYER_THIRST.get(player);
        float exhaustion = thirstData.getExhaustion();
        int quenched = thirstData.getQuenched();
        int thirst = thirstData.getThirst();
        boolean shouldTickThirst = thirstData.isShouldTickThirst();
        ThirstModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                new PlayerThirstSyncMessage(thirst, quenched, exhaustion,shouldTickThirst));
    }

    public void addExhaustion(Player player, float amount)
    {
        ThirstData thirstData = PLAYER_THIRST.get(player);
        float exhaustion = thirstData.getExhaustion();
        boolean justHealed = thirstData.isJustHealed();
        if(!CommonConfig.HEALTH_REGEN_DEPLETES_HYDRATION.get() && justHealed)
            amount = 0;

        if(!CommonConfig.HEALTH_REGEN_DEHYDRATION_IS_BIOME_DEPENDENT.get() && justHealed)
            exhaustion += amount;
        else
            exhaustion += (amount *
                    ThirstHelper.getExhaustionBiomeModifier(player) *
                    ThirstHelper.getExhaustionFireProtModifier(player)*
                    ThirstHelper.getExhaustionFireResistanceModifier(player)
            );

        if(justHealed)
            justHealed = false;

        updateThirstData(player);
    }
}