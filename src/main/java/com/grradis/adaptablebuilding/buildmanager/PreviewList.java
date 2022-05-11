package com.grradis.adaptablebuilding.buildmanager;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PreviewList {
    private static HashMap<Player, BuildingPreview> map = new HashMap<>(); // Previewing Player list.

    public static BuildingPreview get(Player p) {
        return map.get(p);
    }

    public static void insert(Player p, BuildingPreview preview) {
        map.put(p, preview);
    }

    public static boolean contain(Player p) {
        return map.containsKey(p);
    }

    public static void remove(Player p) {
        map.remove(p);
    }
}
