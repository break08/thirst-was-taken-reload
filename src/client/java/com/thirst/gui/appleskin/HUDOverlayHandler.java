package com.thirst.gui.appleskin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thirsty.ThirstWasTaken;
import com.thirsty.misc.ThirstHelper;
import com.thirsty.thirst.ThirstData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import squeek.appleskin.ModConfig;
import squeek.appleskin.util.IntPoint;
import com.thirst.gui.ThirstBarRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Random;
import java.util.Vector;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class HUDOverlayHandler {
    private static float unclampedFlashAlpha = 0.0F;
    private static float flashAlpha = 0.0F;
    private static byte alphaDir = 1;
    protected static int foodIconsOffset;
    public static final Vector<IntPoint> foodBarOffsets = new Vector<>();
    private static final Random random = new Random();
    private static final ResourceLocation modIcons;
    static ResourceLocation THIRST_LEVEL_ELEMENT;

    public HUDOverlayHandler() {
    }

    /*
    public static void init() {
        Minecraf Forge.EVENT_BUS.register(new HUDOverlayHandler());
    }
    */

    // @SubscribeEvent
    public static void onRenderGuiOverlayPre() {
        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null){return;}
            boolean isMounted = mc.player.getVehicle() instanceof LivingEntity;
            boolean isAlive = mc.player.isAlive();
            boolean isSur = !mc.player.isCreative() && !mc.player.isSpectator();
            //stop getExhaustion when player is dead to prevent error log spam
            if (isAlive && ModConfig.INSTANCE.showFoodExhaustionHudUnderlay && !isMounted && !mc.options.hideGui && isSur && !ThirstBarRenderer.cancelRender) {
                renderExhaustion(guiGraphics);
            }
        });
    }

    // @SubscribeEvent
    public static void onRenderGuiOverlayPost() {
        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null){return;}
            boolean isMounted = mc.player.getVehicle() instanceof LivingEntity;
            boolean isAlive = mc.player.isAlive();

            boolean isSur = !mc.player.isCreative() && !mc.player.isSpectator();

            if (isAlive && !isMounted && !mc.options.hideGui && isSur && !ThirstBarRenderer.cancelRender) {
                renderThirstOverlay(guiGraphics);
            }
        });
    }

    public static void renderExhaustion(GuiGraphics mStack) {
        foodIconsOffset = 49;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        assert player != null;

        if (player.isEyeInFluid(FluidTags.WATER)) {
            foodIconsOffset = 60;
        }

        // ClientConfig.THIRST_BAR_X_OFFSET.get()
        int right = mc.getWindow().getGuiScaledWidth() / 2 + 91;

        // + ClientConfig.THIRST_BAR_Y_OFFSET.get()
        int top = mc.getWindow().getGuiScaledHeight() - foodIconsOffset;

        ThirstData thirstData = PLAYER_THIRST.get(player);
        float exhaustion = thirstData.getExhaustion();

        drawExhaustionOverlay(exhaustion, mStack, right, top);
    }

    public static void renderThirstOverlay(GuiGraphics guiGraphics) {
        if (!shouldRenderAnyOverlays())
            return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        assert player != null;
        ThirstData thirstData = PLAYER_THIRST.get(player);

        // + ClientConfig.THIRST_BAR_Y_OFFSET.get();
        int top = mc.getWindow().getGuiScaledHeight() - foodIconsOffset;

        // + ClientConfig.THIRST_BAR_X_OFFSET.get()
        int right = mc.getWindow().getGuiScaledWidth() / 2 + 91; // right of food bar

        generateHungerBarOffsets(top, right, mc.gui.getGuiTicks(), player);
        if (ModConfig.INSTANCE.showSaturationHudOverlay) {
            drawSaturationOverlay(0, thirstData.getQuenched(), guiGraphics, right, top, 1f);
        }

        // try to get the item stack in the player hand
        net.minecraft.world.item.ItemStack heldItem = player.getMainHandItem();
        if (ModConfig.INSTANCE.showFoodValuesHudOverlayWhenOffhand && !ThirstHelper.itemRestoresThirst(heldItem))
            heldItem = player.getOffhandItem();

        boolean shouldRenderHeldItemValues = !heldItem.isEmpty() && ThirstHelper.itemRestoresThirst(heldItem);
        if (!shouldRenderHeldItemValues) {
            resetFlash();
            return;
        }

        ThirstValues thirstValues = new ThirstValues(ThirstHelper.getThirst(heldItem), ThirstHelper.getQuenched(heldItem));

        // calculate the final hunger and saturation
        int drinkThirst = thirstValues.thirst;
        float thirstQuenchedIncrement = thirstValues.getQuenchedIncrement();

        // restored hunger/saturation overlay while holding food
        if (thirstData.getThirst() < 20)
            drawHungerOverlay(drinkThirst, thirstData.getThirst(), guiGraphics, right, top, flashAlpha);
        // Redraw saturation overlay for gained
        if (!ThirstHelper.isFood(heldItem) || player.getFoodData().getFoodLevel() < 20)
            drawSaturationOverlay(thirstQuenchedIncrement, thirstData.getQuenched(), guiGraphics, right, top, flashAlpha);
    }

    public static void drawSaturationOverlay(float saturationGained, float saturationLevel, GuiGraphics guiGraphics, int right, int top, float alpha) {
        if (saturationLevel + saturationGained < 0)
            return;

        enableAlpha(alpha);

        ResourceLocation icons = modIcons;
        RenderSystem.setShaderTexture(0, icons);

        float modifiedSaturation = Math.max(0, Math.min(saturationLevel + saturationGained, 20));

        int startSaturationBar = 0;
        int endSaturationBar = (int) Math.ceil(modifiedSaturation / 2.0F);

        // when require rendering the gained saturation, start should relocation to current saturation tail.
        if (saturationGained != 0)
            startSaturationBar = (int) Math.max(saturationLevel / 2.0F, 0);

        int iconSize = 9;

        for (int i = startSaturationBar; i < endSaturationBar; ++i) {
            // gets the offset that needs to be rendered of icon
            IntPoint offset = foodBarOffsets.get(i);
            if (offset == null)
                continue;

            int x = right + offset.x;
            int y = top + offset.y;

            int v = 0;
            int u = 0;

            float effectiveSaturationOfBar = (modifiedSaturation / 2.0F) - i;

            if (effectiveSaturationOfBar >= 1)
                u = 3 * iconSize;
            else if (effectiveSaturationOfBar > .5)
                u = 2 * iconSize;
            else if (effectiveSaturationOfBar > .25)
                u = iconSize;

            guiGraphics.blit(icons, x, y, u, v, iconSize, iconSize);
        }

        // rebind default icons
        RenderSystem.setShaderTexture(0, ThirstBarRenderer.MC_ICONS);
        disableAlpha();
    }

    public static void drawHungerOverlay(int hungerRestored, int foodLevel, GuiGraphics guiGraphics, int right, int top, float alpha) {
        if (hungerRestored <= 0)
            return;

        enableAlpha(alpha);

        ResourceLocation icons = ThirstBarRenderer.THIRST_ICONS;
        RenderSystem.setShaderTexture(0, icons);

        int modifiedFood = Math.max(0, Math.min(20, foodLevel + hungerRestored));

        int startFoodBars = Math.max(0, foodLevel / 2);
        int endFoodBars = (int) Math.ceil(modifiedFood / 2.0F);

        int iconStartOffset = 8 - 3;
        int iconSize = 9;

        for (int i = startFoodBars; i < endFoodBars; ++i) {
            // gets the offset that needs to be rendered of icon
            IntPoint offset = foodBarOffsets.get(i);
            if (offset == null)
                continue;

            int x = right + offset.x;
            int y = top + offset.y;

            // location to normal food by default
            int v = 3 * iconSize;
            int u = iconStartOffset + 4 * iconSize;

            // relocation to half food
            if (i * 2 + 1 == modifiedFood)
                u -= iconSize - 1;

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

            guiGraphics.blit(icons, x, y, u, v, iconSize, iconSize, 25, 9);
        }

        disableAlpha();
    }

    public static void drawExhaustionOverlay(float exhaustion, GuiGraphics guiGraphics, int right, int top) {
        ResourceLocation icons = modIcons;
        RenderSystem.setShaderTexture(0, icons);

        float maxExhaustion = 4.0f;
        // clamp between 0 and 1
        float ratio = Math.min(1, Math.max(0, exhaustion / maxExhaustion));
        int width = (int) (ratio * 81);
        int height = 9;

        enableAlpha(.75f);
        guiGraphics.blit(icons, right - width, top, 81 - width, 18, width, height);
        disableAlpha();

        // rebind default icons
        RenderSystem.setShaderTexture(0, ThirstBarRenderer.MC_ICONS);
    }

    public static void enableAlpha(float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableAlpha() {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    // @SubscribeEvent
    public static void onClientTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            unclampedFlashAlpha += alphaDir * 0.125f;
            if (unclampedFlashAlpha >= 1.5f) {
                alphaDir = -1;
            } else if (unclampedFlashAlpha <= -0.5f) {
                alphaDir = 1;
            }
            flashAlpha = Math.max(0F, Math.min(1F, unclampedFlashAlpha)) * 0.65f;
        });
    }

    public static void resetFlash() {
        unclampedFlashAlpha = flashAlpha = 0f;
        alphaDir = 1;
    }

    private static boolean shouldRenderAnyOverlays() {
        return true;
    }

    private static void generateHungerBarOffsets(int top, int right, int ticks, Player player) {
        final int preferFoodBars = 10;

        boolean shouldAnimatedFood;

        ThirstData thirstData = PLAYER_THIRST.get(player);

        // in vanilla saturation level is zero will show hunger animation
        float quenched = thirstData.getQuenched();
        int thirst = thirstData.getThirst();
        shouldAnimatedFood = quenched <= 0.0F && ticks % (thirst * 3 + 1) == 0;

        if (foodBarOffsets.size() != preferFoodBars)
            foodBarOffsets.setSize(preferFoodBars);

        // right alignment, single row
        for (int i = 0; i < preferFoodBars; ++i) {
            int x = right - i * 8 - 9;
            int y = top;

            // apply the animated offset
            if (shouldAnimatedFood)
                y += random.nextInt(3) - 1;

            // reuse the point object to reduce memory usage
            IntPoint point = foodBarOffsets.get(i);
            if (point == null) {
                point = new IntPoint();
                foodBarOffsets.set(i, point);
            }

            point.x = x - right;
            point.y = y - top;
        }
    }

    static {
        modIcons = ThirstWasTaken.asResource("textures/gui/appleskin_icons.png");
        THIRST_LEVEL_ELEMENT = ThirstWasTaken.asResource("thirst_level");
    }

    public static void event_reg(){
        onClientTick();
        onRenderGuiOverlayPre();
        onRenderGuiOverlayPost();
    }
}
