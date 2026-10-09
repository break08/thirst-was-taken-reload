package com.thirsty.api.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "thirst_keyword")
public class KeyWordConfig implements ConfigData {
    public boolean ENABLE_KEYWORD_CONFIG = false;

    public int DEFAULT_DRINK_HYDRATION = 10;

    public int DEFAULT_DRINK_QUENCHNESS = 14;

    public int DEFAULT_SOUP_HYDRATION = 4;

    public int DEFAULT_SOUP_QUENCHNESS = 5;

    public int DEFAULT_FRUIT_HYDRATION = 2;

    public int DEFAULT_FRUIT_QUENCHNESS = 3;

    @Comment("List of keywords for soups. Format: [(keyword1|keyword2|keyword3)]")
    public String KEYWORD_SOUP = "(?:\\b|[^a-zA-Z])(soup|stew|porridge)(?:\\b|[^a-zA-Z])";

    public String KEYWORD_FRUIT = "(?:\\b|[^a-zA-Z])(fruit|berry|berries|grape|orange|peach|pear|coconut|lemon|melon|cherry|apple)(?:\\b|[^a-zA-Z])";

    public String KEYWORD_DRINK = "(?:\\b|[^a-zA-Z])(drink|juice|tea|soda|coffee|wine|beer|cider|yogurt|milkshake|smoothie)(?:\\b|[^a-zA-Z])";

    public String KEYWORD_BLACKLIST = "(?:\\b|[^a-zA-Z])(dried|candied|leaf|leaves|gummy|crate|jam|sauce|bucket|seed|cookie|pie|bush|sapling|bean|curry|cake|candy)(?:\\b|[^a-zA-Z])";

}
