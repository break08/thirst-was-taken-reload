package com.thirsty.misc;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.api.config.ContainerConfig;
import com.thirsty.api.config.KeyWordConfig;
import com.thirsty.api.config.ItemSettingsConfig;
import com.thirsty.purity.ContainerWithPurity;
import com.thirsty.purity.WaterPurity;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thirsty.purity.WaterPurity.hasPurity;

public class ThirstHelper {
    private static final float MODIFIER_HARSHNESS = 0.5f;
    public static CommonConfig commonConfig = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();
    public static ItemSettingsConfig itemConfig = AutoConfig.getConfigHolder(ItemSettingsConfig.class).getConfig();

    public static Map<Item, Number[]> VALID_DRINKS = ConfigHelper.getItemsWithValues(itemConfig.DRINKS);
    public static Map<Item, Number[]> VALID_FOODS = ConfigHelper.getItemsWithValues(itemConfig.FOODS);
    public static List<Item> containers = ConfigHelper.getItems(AutoConfig.getConfigHolder(ContainerConfig.class).getConfig().CONTAINER);

    public static void init(){
        for (Item item : containers){
            if(item.equals(Items.AIR))
                continue;
            WaterPurity.addContainer(new ContainerWithPurity(new ItemStack(item)));
        }
    }

    public static void reload() {
        VALID_DRINKS = ConfigHelper.getItemsWithValues(AutoConfig.getConfigHolder(ItemSettingsConfig.class).getConfig().DRINKS);
        VALID_FOODS  = ConfigHelper.getItemsWithValues(AutoConfig.getConfigHolder(ItemSettingsConfig.class).getConfig().FOODS);
        containers   = ConfigHelper.getItems(AutoConfig.getConfigHolder(ContainerConfig.class).getConfig().CONTAINER);
        init();
    }

    public static String keywordBlackList = AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().KEYWORD_BLACKLIST;
    public static String keywordDrink = AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().KEYWORD_DRINK;
    public static String keywordSoup = AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().KEYWORD_SOUP;
    public static String keywordFruit = AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().KEYWORD_FRUIT;

    public static boolean itemRestoresThirst(ItemStack itemStack)
    {
        if (itemConfig.ITEMS_BLACKLIST.contains(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString())){
            return false;
        }

        return isDrink(itemStack) ||
                isFood(itemStack) || checkKeywords(itemStack);
    }

    public static boolean isDrink(ItemStack itemStack)
    {
        return !itemConfig.ITEMS_BLACKLIST.contains(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString()) &&
                VALID_DRINKS.containsKey(itemStack.getItem());
    }

    public static boolean isFood(ItemStack itemStack)
    {
        return !itemConfig.ITEMS_BLACKLIST.contains(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString()) &&
                VALID_FOODS.containsKey(itemStack.getItem());
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
        float totalLevels = (EnchantmentHelper.getDamageProtection((ServerLevel) player.level(),player, player.damageSources().onFire()) / 2);

        return 1.0f - ((totalLevels * perLevelMultiplier) * 0.75f);
    }

    public static int getPurity(ItemStack item)
    {
        if(!hasPurity(item))
            return AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY;
        else {
            CompoundTag tag = NbtHelper.get(item);
            return tag.getInt("Purity");
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

    private static boolean checkKeywords(ItemStack itemStack)
    {
        if(!AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().ENABLE_KEYWORD_CONFIG)
            return false;

        if(!itemStack.has(DataComponents.FOOD))
            return false;

        if (itemConfig.ITEMS_BLACKLIST.contains(itemStack.getItem().toString())){
            return false;
        }

        String pattern = keywordBlackList;
        Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
                .matcher(itemStack.getDescriptionId());

        if(matcher.find())
            return false;

        pattern = keywordDrink;
        matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
                .matcher(itemStack.getDescriptionId());

        boolean hasWater=matcher.find();
        if(hasWater)
        {
            VALID_DRINKS.put(itemStack.getItem(), new Number[]{
                    getDrinkHydration(),
                    getDrinkQuenchness()
            });
            return true;
        }

        pattern = keywordSoup;
        matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
                .matcher(itemStack.getDescriptionId());

        hasWater=matcher.find();
        if(hasWater)
        {
            VALID_FOODS.put(itemStack.getItem(), new Number[]{
                    getSoupHydration(),
                    getSoupQuenchness()
            });
            return true;
        }

        pattern = keywordFruit;
        matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
                .matcher(itemStack.getDescriptionId());

        hasWater = matcher.find();
        if(hasWater)
            VALID_FOODS.put(itemStack.getItem(), new Number[]{
                    getFruitHydration(),
                    getFruitQuenchness()
            });

        return hasWater;
    }

    public static int getDrinkHydration()
    {
        return AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().DEFAULT_DRINK_HYDRATION;
    }

    public static int getDrinkQuenchness()
    {
        return AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().DEFAULT_DRINK_QUENCHNESS;
    }

    public static int getSoupHydration()
    {
        return AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().DEFAULT_SOUP_HYDRATION;
    }

    public static int getSoupQuenchness()
    {
        return AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().DEFAULT_SOUP_QUENCHNESS;
    }

    public static int getFruitHydration()
    {
        return AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().DEFAULT_FRUIT_HYDRATION;
    }

    public static int getFruitQuenchness()
    {
        return AutoConfig.getConfigHolder(KeyWordConfig.class).getConfig().DEFAULT_FRUIT_QUENCHNESS;
    }
}
