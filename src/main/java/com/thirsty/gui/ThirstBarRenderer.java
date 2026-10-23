package com.thirsty.gui;

import com.thirsty.ThirstWasTaken;
import com.thirsty.api.config.ClientConfig;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class ThirstBarRenderer {
    public static final Identifier THIRST_ICONS = Identifier.fromNamespaceAndPath(ThirstWasTaken.MOD_ID, "textures/gui/thirst_icons.png");
    public static final Identifier MC_ICONS = Identifier.fromNamespaceAndPath("minecraft", "textures/gui/icons.png");

    public static boolean cancelRender = false;
    public static ClientConfig clientConfig = AutoConfig.getConfigHolder(ClientConfig.class).getConfig();

    public static int lastRight, lastTop;

    public static boolean rendered = false;

    public static void unRender() {
        Minecraft minecraft = Minecraft.getInstance();
        cancelRender = false;
        if (minecraft.player == null) return;

        boolean isMounted = minecraft.player.getVehicle() instanceof LivingEntity;
        boolean isSurvival = !minecraft.player.isCreative() && !minecraft.player.isSpectator();

        if (isMounted && !minecraft.options.hideGui && isSurvival) {
            ThirstData thirst = PLAYER_THIRST.get(minecraft.player);
            if (minecraft.player.isAlive() && !thirst.isShouldTickThirst()) {
                cancelRender = true;
            }
        }
    }

    public static void renderingHUD(GuiGraphicsExtractor guiGraphics, float tickDelta) {
        rendered = false;
        unRender();
        if (cancelRender) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        if (minecraft.player.isCreative() || minecraft.player.isSpectator()) return;

        ThirstData thirstData = PLAYER_THIRST.get(minecraft.player);
        int level = thirstData.getThirst();
        int ticks = minecraft.gui.getGuiTicks();

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        int left = width / 2 + 91 + clientConfig.THIRST_BAR_X_OFFSET;
        int top = clientConfig.THIRST_BAR_Y_OFFSET + height - 49;

        lastRight = left;
        lastTop = top;
        rendered = true;

        boolean shake = thirstData.getQuenched() <= 0.0F && ticks % (level * 3 + 1) == 0;

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;

            if (shake) {
                RandomSource random = RandomSource.create(ticks * 312871L + i);
                y += random.nextInt(3) - 1;
            }

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, THIRST_ICONS, x, y, 0, 0, 9, 9, 25, 9);

            if (idx < level) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, THIRST_ICONS, x, y, 16, 0, 9, 9, 25, 9);
            } else if (idx == level) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, THIRST_ICONS, x, y, 8, 0, 9, 9, 25, 9);
            }
        }
    }
}