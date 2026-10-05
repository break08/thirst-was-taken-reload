package com.thirsty.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.thirsty.purity.WaterPurity.*;
import static com.thirsty.purity.WaterPurity.MAX_PURITY;
import static com.thirsty.purity.WaterPurity.getPurityColor;
import static com.thirsty.purity.WaterPurity.getPurityText;

public class TooltipHelper {

    public static void addPurityTooltip(ItemStack itemStack, List<Component> list){
        if(isWaterFilledContainer(itemStack))
        {
            int purity = getPurity(itemStack);
            if(purity >= MIN_PURITY && purity <= MAX_PURITY)
            {
                String purityText = getPurityText(purity);

                int purityColor = getPurityColor(purity);

                assert purityText != null;
                list.add(MutableComponent
                        .create(new LiteralContents(purityText))
                        .setStyle(Style.EMPTY.withColor(purityColor)));
            }
        }
    }
}
