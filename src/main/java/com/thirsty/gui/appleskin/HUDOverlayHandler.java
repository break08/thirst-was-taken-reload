package com.thirsty.gui.appleskin;

import com.thirsty.ThirstWasTaken;
import com.thirsty.api.config.ClientConfig;
import com.thirsty.misc.ThirstHelper;
import com.thirsty.thirst.ThirstData;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import squeek.appleskin.ModConfig;
import squeek.appleskin.util.IntPoint;
import com.thirsty.gui.ThirstBarRenderer;

import java.util.Random;
import java.util.Vector;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;
import static com.thirsty.gui.ThirstBarRenderer.unRender;

public class HUDOverlayHandler {
    private static final Identifier MOD_ICONS = ThirstWasTaken.asResource("textures/gui/appleskin_icons.png");
    private static final Vector<IntPoint> BAR_OFFSETS = new Vector<>();
    private static final RandomSource RANDOM = RandomSource.create();
    private static float unclampedFlashAlpha;
    private static float flashAlpha;
    private static byte alphaDirection = 1;
    public static ClientConfig clientConfig = AutoConfig.getConfigHolder(ClientConfig.class).getConfig();


    public static int getBarRight(int width)
    {
        return width / 2 + 91 + clientConfig.THIRST_BAR_X_OFFSET;
    }

    public static int getBarTop(int height)
    {
        return height - 49 + clientConfig.THIRST_BAR_Y_OFFSET;
    }

    private HUDOverlayHandler() {
    }



    public static void renderThirstOverlay(Player player, GuiGraphicsExtractor graphics, int guiTicks) {
        ThirstData thirst = PLAYER_THIRST.get(player);
        int right = getBarRight(graphics.guiWidth());
        int top = getBarTop(graphics.guiHeight());
        generateBarOffsets(guiTicks, thirst);

        if (ModConfig.INSTANCE.showSaturationHudOverlay) {
            drawSaturationOverlay(0, thirst.getQuenched(), graphics, right, top, 1.0F);
        }

        ItemStack heldItem = player.getMainHandItem();
        if (ModConfig.INSTANCE.showFoodValuesHudOverlayWhenOffhand && !ThirstHelper.itemRestoresThirst(heldItem)) {
            heldItem = player.getOffhandItem();
        }

        if (heldItem.isEmpty() || !ThirstHelper.itemRestoresThirst(heldItem)) {
            resetFlash();
            return;
        }

        int restoredThirst = ThirstHelper.getThirst(heldItem);
        float restoredQuenched = ThirstHelper.getQuenched(heldItem);
        if (thirst.getThirst() < 20) {
            drawThirstOverlay(restoredThirst, thirst.getThirst(), graphics, right, top, flashAlpha);
        }
        if (!ThirstHelper.isFood(heldItem) || player.getFoodData().getFoodLevel() < 20) {
            drawSaturationOverlay(restoredQuenched, thirst.getQuenched(), graphics, right, top, flashAlpha);
        }
    }

    private static void drawSaturationOverlay(float gained, float current, GuiGraphicsExtractor graphics, int right, int top, float alpha) {
        float value = Math.max(0, Math.min(current + gained, 20));
        int start = gained == 0 ? 0 : Math.max((int) current / 2, 0);
        int end = (int) Math.ceil(value / 2.0F);
        int color = color(alpha);

        for (int i = start; i < end && i < BAR_OFFSETS.size(); i++) {
            IntPoint offset = BAR_OFFSETS.get(i);
            if (offset == null) continue;
            float amount = value / 2.0F - i;
            int u = amount >= 1 ? 27 : amount > .5F ? 18 : amount > .25F ? 9 : 0;
            graphics.blit(RenderPipelines.GUI_TEXTURED, MOD_ICONS, right + offset.x, top + offset.y, u, 0, 9, 9, 256, 256, color);
        }
    }

    private static void drawThirstOverlay(int restored, int current, GuiGraphicsExtractor graphics, int right, int top, float alpha) {
        if (restored <= 0) return;
        int value = Math.max(0, Math.min(20, current + restored));
        int start = Math.max(0, current / 2);
        int end = (int) Math.ceil(value / 2.0F);
        int color = color(alpha);

        for (int i = start; i < end && i < BAR_OFFSETS.size(); i++) {
            IntPoint offset = BAR_OFFSETS.get(i);
            if (offset == null) continue;
            int u = i * 2 + 1 == value ? 8 : 16;
            graphics.blit(RenderPipelines.GUI_TEXTURED, ThirstBarRenderer.THIRST_ICONS, right + offset.x, top + offset.y, u, 0, 9, 9, 25, 9, color);
        }
    }

    public static void drawExhaustionOverlay(float exhaustion, GuiGraphicsExtractor graphics, int right, int top) {
        int width = (int) (Math.clamp(exhaustion / 4.0F, 0, 1) * 81);
        if (width > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, MOD_ICONS, right - width, top, 81 - width, 18, width, 9, 256, 256, color(.75F));
        }
    }

    public static void onClientTick() {
        unclampedFlashAlpha += alphaDirection * 0.125F;
        if (unclampedFlashAlpha >= 1.5F) alphaDirection = -1;
        else if (unclampedFlashAlpha <= -0.5F) alphaDirection = 1;
        flashAlpha = Math.clamp(unclampedFlashAlpha, 0, 1) * ModConfig.INSTANCE.maxHudOverlayFlashAlpha;
    }

    private static void resetFlash() {
        unclampedFlashAlpha = flashAlpha = 0;
        alphaDirection = 1;
    }

    private static void generateBarOffsets(int ticks, ThirstData thirst) {
        if (BAR_OFFSETS.size() != 10) BAR_OFFSETS.setSize(10);
        boolean animate = thirst.getQuenched() <= 0 && ticks % (thirst.getThirst() * 3 + 1) == 0;
        for (int i = 0; i < 10; i++) {
            IntPoint point = BAR_OFFSETS.get(i);
            if (point == null) {
                point = new IntPoint();
                BAR_OFFSETS.set(i, point);
            }
            point.x = -i * 8 - 9;
            point.y = animate ? RANDOM.nextInt(3) - 1 : 0;
        }
    }

    private static int color(float alpha) {
        return Math.round(Math.clamp(alpha, 0, 1) * 255) << 24 | 0xFFFFFF;
    }

    private static int lastFlashTick = -1;

    public static void updateFlash(int guiTicks) {
        if (guiTicks == lastFlashTick) return;
        lastFlashTick = guiTicks;
        onClientTick();
    }
}
