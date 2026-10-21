package com.thirsty.gui.appleskin;

import com.thirsty.gui.ThirstBarRenderer;
import com.thirsty.ThirstWasTaken;
import com.thirsty.misc.ThirstHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import squeek.appleskin.ModConfig;
import squeek.appleskin.helpers.KeyHelper;

import java.util.Optional;

public class TooltipRenderer {
    public static void register(){
        ClientTooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof ThirstTooltip food) {
                return new FoodTooltipRenderer(food);
            }
            return null;
        });
    }

    public static Optional<TooltipComponent> createTooltip(ItemStack stack) {
        if (!shouldShowTooltip(stack)) return Optional.empty();
        ThirstTooltip tooltip = new ThirstTooltip(stack);
        return Optional.of(tooltip);
    }

    private static final Identifier modIcons;
    private static final TextureOffsets normalBarTextureOffsets;
    private static final TextureOffsets rottenBarTextureOffsets;

    static {
        modIcons = ThirstWasTaken.asResource("textures/gui/appleskin_icons.png");
        normalBarTextureOffsets = new TextureOffsets();
        normalBarTextureOffsets.containerNegativeHunger = 43;
        normalBarTextureOffsets.containerExtraHunger = 133;
        normalBarTextureOffsets.containerNormalHunger = 16;
        normalBarTextureOffsets.containerPartialHunger = 124;
        normalBarTextureOffsets.containerMissingHunger = 34;
        normalBarTextureOffsets.shankMissingFull = 70;
        normalBarTextureOffsets.shankMissingPartial = normalBarTextureOffsets.shankMissingFull + 9;
        normalBarTextureOffsets.shankFull = 52;
        normalBarTextureOffsets.shankPartial = normalBarTextureOffsets.shankFull + 9;
        rottenBarTextureOffsets = new TextureOffsets();
        rottenBarTextureOffsets.containerNegativeHunger = normalBarTextureOffsets.containerNegativeHunger;
        rottenBarTextureOffsets.containerExtraHunger = normalBarTextureOffsets.containerExtraHunger;
        rottenBarTextureOffsets.containerNormalHunger = normalBarTextureOffsets.containerNormalHunger;
        rottenBarTextureOffsets.containerPartialHunger = normalBarTextureOffsets.containerPartialHunger;
        rottenBarTextureOffsets.containerMissingHunger = normalBarTextureOffsets.containerMissingHunger;
        rottenBarTextureOffsets.shankMissingFull = 106;
        rottenBarTextureOffsets.shankMissingPartial = rottenBarTextureOffsets.shankMissingFull + 9;
        rottenBarTextureOffsets.shankFull = 88;
        rottenBarTextureOffsets.shankPartial = rottenBarTextureOffsets.shankFull + 9;
    }

    static class TextureOffsets {
        int containerNegativeHunger;
        int containerExtraHunger;
        int containerNormalHunger;
        int containerPartialHunger;
        int containerMissingHunger;
        int shankMissingFull;
        int shankMissingPartial;
        int shankFull;
        int shankPartial;

        TextureOffsets() {
        }
    }

    public static boolean shouldShowTooltip(ItemStack hoveredStack) {
        if (hoveredStack.isEmpty()) {
            return false;
        } else {
            boolean shouldShowTooltip = ModConfig.INSTANCE.showFoodValuesInTooltip && KeyHelper.isShiftKeyDown() || ModConfig.INSTANCE.showFoodValuesInTooltipAlways;
            if (!shouldShowTooltip) {
                return false;
            } else {
                return ThirstHelper.itemRestoresThirst(hoveredStack);
            }
        }
    }

    static final class ThirstTooltip implements TooltipComponent {
        private final ItemStack stack;
        private final int thirstBars;
        private final int quenchedBars;
        private final String thirstText;
        private final String quenchedText;

        private ThirstTooltip(ItemStack stack) {
            this.stack = stack.copy();
            int thirst = ThirstHelper.getThirst(stack);
            float quenched = ThirstHelper.getQuenched(stack);
            int rawThirstBars = (int) Math.ceil(Math.abs(thirst) / 2.0F);
            int rawQuenchedBars = (int) Math.ceil(Math.abs(quenched) / 2.0F);
            this.thirstBars = rawThirstBars > 10 ? 1 : rawThirstBars;
            this.quenchedBars = rawQuenchedBars > 10 || rawQuenchedBars == 0 ? 1 : rawQuenchedBars;
            this.thirstText = rawThirstBars > 10 ? "x" + rawThirstBars : null;
            this.quenchedText = rawQuenchedBars > 10 || rawQuenchedBars == 0 ? "x" + rawQuenchedBars : null;
        }


    }


    static class FoodTooltipRenderer implements ClientTooltipComponent {
        private final ThirstTooltip tooltip;

        FoodTooltipRenderer(ThirstTooltip tooltip) {
            this.tooltip = tooltip;
        }

        @Override
        public int getHeight(@NotNull Font font) {
            return 20;
        }

        @Override
        public int getWidth(@NotNull Font font) {
            int thirstWidth = tooltip.thirstBars * 9 + (tooltip.thirstText == null ? 0 : font.width(tooltip.thirstText));
            int quenchedWidth = tooltip.quenchedBars * 7 + (tooltip.quenchedText == null ? 0 : font.width(tooltip.quenchedText));
            return Math.max(thirstWidth, quenchedWidth) + 2;
        }

        @Override
        public void extractImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphicsExtractor graphics) {
            if (!shouldShowTooltip(tooltip.stack)) return;

            int thirst = ThirstHelper.getThirst(tooltip.stack);
            int iconX = x + (tooltip.thirstBars - 1) * 9;
            for (int i = 0; i < tooltip.thirstBars * 2; i += 2) {
                int u = thirst == i + 1 ? 8 : 16;
                graphics.blit(RenderPipelines.GUI_TEXTURED, ThirstBarRenderer.THIRST_ICONS, iconX, y, u, 0, 9, 9, 25, 9);
                iconX -= 9;
            }
            if (tooltip.thirstText != null) graphics.text(font, tooltip.thirstText, x + 11, y + 1, 0xFFAAAAAA);

            float quenched = ThirstHelper.getQuenched(tooltip.stack);
            float absolute = Math.abs(quenched);
            iconX = x + (tooltip.quenchedBars - 1) * 7;
            for (int i = 0; i < tooltip.quenchedBars * 2; i += 2) {
                float amount = (absolute - i) / 2.0F;
                int u = amount >= 1 ? 21 : amount > .5F ? 14 : amount > .25F ? 7 : amount > 0 ? 0 : 28;
                int color = absolute <= i ? 0x80FFFFFF : 0xFFFFFFFF;
                graphics.blit(RenderPipelines.GUI_TEXTURED, modIcons, iconX, y + 10, u, quenched >= 0 ? 27 : 34, 7, 7, 256, 256, color);
                iconX -= 7;
            }
            if (tooltip.quenchedText != null) graphics.text(font, tooltip.quenchedText, x + 9, y + 10, 0xFFAAAAAA);
        }
    }

}
