package com.thirsty.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thirsty.ThirstWasTaken;
import com.thirsty.thirst.ThirstData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class ThirstBarRenderer implements HudRenderCallback {
    /**
     * Called after rendering the whole hud, which is displayed in game, in a world.
     *
     */

    public static final ResourceLocation THIRST_ICONS = new ResourceLocation(ThirstWasTaken.MOD_ID, "textures/gui/thirst_icons.png");
    public static final ResourceLocation MC_ICONS = new ResourceLocation("minecraft", "textures/gui/icons.png");
    public static Boolean cancelRender = false;

    public static void unRender(){
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null){return;}
        boolean isMounted = minecraft.player.getVehicle() instanceof LivingEntity;
        cancelRender =false;
        boolean isSur = !minecraft.player.isCreative() && !minecraft.player.isSpectator();
        if (!isMounted && !minecraft.options.hideGui && isSur)
        {
            ThirstData thirst = PLAYER_THIRST.get(minecraft.player);;
            if(minecraft.player.isAlive() && !thirst.isShouldTickThirst()){
                cancelRender = true;
            }
        }
    }

    @Override
    public void onHudRender(GuiGraphics guiGraphics, float tickDelta) {
        renderingHUD(guiGraphics, tickDelta);
    }

    public static void renderingHUD(GuiGraphics guiGraphics, float tickDelta){
        unRender();
        if (cancelRender){
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null){return;}
        if (!minecraft.player.isCreative() && !minecraft.player.isSpectator()) {

            ThirstData thirstdata;

            {
                assert minecraft.player != null;
                thirstdata = PLAYER_THIRST.get(minecraft.player);
            }

            final RandomSource random = RandomSource.create();
            int level = thirstdata.getThirst();

            RenderSystem.enableBlend();
            int width = guiGraphics.guiWidth();
            int height = guiGraphics.guiHeight();
            //+ ClientConfig.THIRST_BAR_X_OFFSET.get()
            int left = width / 2 + 91;
            //+ ClientConfig.THIRST_BAR_Y_OFFSET.get();
            int top = 0;
            if (minecraft.player.isEyeInFluid(FluidTags.WATER)) {
                top = height - 60;
            } else {
                top = height - 49;
            }
            boolean unused = false;
            for (int i = 0; i < 10; ++i) {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;

                if (thirstdata.getQuenched() <= 0.0F && minecraft.gui.getGuiTicks() % (level * 3 + 1) == 0) {
                    y = top + (random.nextInt(3) - 1);
                }

                guiGraphics.blit(THIRST_ICONS, x, y, 0, 0, 9, 9, 25, 9);

                if (idx < level)
                    guiGraphics.blit(THIRST_ICONS, x, y, 16, 0, 9, 9, 25, 9);
                else if (idx == level)
                    guiGraphics.blit(THIRST_ICONS, x, y, 8, 0, 9, 9, 25, 9);
            }
            RenderSystem.disableBlend();
        }
    }
}
