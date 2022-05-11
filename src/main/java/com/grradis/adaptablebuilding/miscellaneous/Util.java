package com.grradis.adaptablebuilding.miscellaneous;

import org.bukkit.ChatColor;

public class Util {
    public static String translate(String msg) { // Change ChatColor.Color -> &alphabet (color code)
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
