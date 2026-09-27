package com.thirsty.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigHelper
{
    /**
     * This class was taken from <a href="https://github.com/Momo-Studios/Cold-Sweat/blob/1.18.x-FG/src/main/java/dev/momostudios/coldsweat/util/config/ConfigHelper.java">Cold Sweat</a>
     */

    public static Map<Item, Number[]> getItemsWithValues(List<? extends List<?>> source)
    {
        Map<Item, Number[]> map = new HashMap<>();
        for (List<?> entry : source)
        {
            String itemID = (String) entry.get(0);

            if (itemID.startsWith("#"))
            {
                final String tagID = itemID.replace("#", "");

                try {
                    TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(tagID));
                    for (Item item : getItemsInTag(tagKey)) {
                        map.put(item, new Number[]{(Number) entry.get(1), (Number) entry.get(2)});
                    }
                } catch (Exception e){
                    System.out.print("[Thirst/ERROR] Bad Config:" + e);
                }
            }
            else
            {
                Item newItem = BuiltInRegistries.ITEM.get(new ResourceLocation(itemID));
                map.put(newItem, new Number[]{(Number) entry.get(1), (Number) entry.get(2)});
            }
        }
        return map;
    }

    public static List<Item> getItems(List<? extends String> source){
        List<Item> list = new ArrayList<>();
        for(String itemID : source){
            if (itemID.startsWith("#"))
            {
                final String tagID = itemID.replace("#", "");
                try {
                    TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(tagID));
                    list.addAll(getItemsInTag(tagKey));
                } catch (Exception e) {
                    System.out.print("[Thirst/ERROR] Bad Config:" + e);
                }
            }
            else
            {
                Item newItem = BuiltInRegistries.ITEM.get(new ResourceLocation(itemID));
                list.add(newItem);
            }
        }
        return list;
    }

    public static List<Item> getItemsInTag(TagKey<Item> tagKey) {
        List<Item> items = new ArrayList<>();

        var optionalTag = BuiltInRegistries.ITEM.getTag(tagKey);

        if (optionalTag.isPresent()) {
            for (Holder<Item> holder : optionalTag.get()) {
                items.add(holder.value());
            }
        }

        return items;
    }
}
