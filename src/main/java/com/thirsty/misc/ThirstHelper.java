package com.thirsty.misc;

import com.thirsty.config.CommonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;

import static com.thirsty.purity.WaterPurity.hasPurity;

public class ThirstHelper {
    private static final float MODIFIER_HARSHNESS = 0.5f;
    public static CommonConfig commonConfig = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();

    public static Map<Item, Number[]> VALID_DRINKS = ConfigHelper.getItemsWithValues(commonConfig.DRINKS);
    public static Map<Item, Number[]> VALID_FOODS = ConfigHelper.getItemsWithValues(commonConfig.FOODS);

    public static boolean itemRestoresThirst(ItemStack itemStack)
    {
        return isDrink(itemStack) || isFood(itemStack);
    }

    public static boolean isDrink(ItemStack itemStack)
    {
        return VALID_DRINKS.containsKey(itemStack.getItem());
    }

    public static boolean isFood(ItemStack itemStack)
    {
        return VALID_FOODS.containsKey(itemStack.getItem());
    }


    public static float getExhaustionFireResistanceModifier(Player player){
        if(player.hasEffect(MobEffects.FIRE_RESISTANCE)){
            return (float) commonConfig.FIRE_RESISTANCE_DEHYDRATION /100;
        }else return 1.0f;
    }

    public static float getExhaustionBiomeModifier(Player player)
    {
        BlockPos pos = player.getOnPos();
        Level level = player.level();

        if(level.dimensionType().ultraWarm())
            return (float)commonConfig.NETHER_THIRST_DEPLETION_MODIFIER;
        else
        {
            Biome biome = level.getBiome(pos).value();

            //humidity range: 0 - 0.8 == 0.8 midpoint: 0.4
            float humidity = biome.climateSettings.downfall() + 0.6f;
            if(humidity <= 0.6)
                humidity += 0.5F;

            //temperature range: -0.8 - 2 == 2.8 midpoint: 0.8
            float temp = biome.getBaseTemperature() + 0.2f;

            if(temp <= 0)
                temp = (float) Math.exp(temp);
            else if(temp > 1)
                temp /= 2;


            // CommonConfig.THIRST_DEPLETION_MODIFIER.get().floatValue() * (temp  / humidity);
            float thirstModifier = commonConfig.THIRST_DEPLETION_MODIFIER * (temp  / humidity);

            if(thirstModifier < 1)
            {
                float modifierOffset = 1 - thirstModifier;
                modifierOffset *= MODIFIER_HARSHNESS;
                thirstModifier = 1 - modifierOffset;
            }

            return thirstModifier;
        }
    }
    public static float getExhaustionFireProtModifier(Player player)
    {
        final float perLevelMultiplier = 0.0625f;
        int totalLevels = EnchantmentHelper.getDamageProtection(player.getArmorSlots(), player.damageSources().onFire()) / 2;

        return 1.0f - ((totalLevels * perLevelMultiplier) * 0.75f);
    }

    public static int getPurity(ItemStack item)
    {
        if(!hasPurity(item))
            return AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY;
        else {
            assert item.getTag() != null;
            return item.getTag().getInt("Purity");
        }
    }
    public static int getThirst(ItemStack itemStack)
    {
        Item item = itemStack.getItem();

        if(VALID_DRINKS.containsKey(item)) {
            return VALID_DRINKS.get(item)[0].intValue();
        }
        else
            return VALID_FOODS.get(item)[0].intValue();
    }
    public static int getQuenched(ItemStack itemStack)
    {
        Item item = itemStack.getItem();

        if(VALID_DRINKS.containsKey(item))
            return VALID_DRINKS.get(item)[1].intValue();
        else
            return VALID_FOODS.get(item)[1].intValue();
    }
}
