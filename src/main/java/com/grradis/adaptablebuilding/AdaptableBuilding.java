package com.grradis.adaptablebuilding;

import com.grradis.adaptablebuilding.filemanager.SettingManager;
import com.grradis.adaptablebuilding.listener.CommandListener;
import com.grradis.adaptablebuilding.listener.EventListener;
import com.grradis.adaptablebuilding.miscellaneous.GlobalVariable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdaptableBuilding extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Adaptable Building plugin enabled!");

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        getCommand("preview").setExecutor(new CommandListener());
        getCommand("test-preview").setExecutor(new CommandListener());

        GlobalVariable.plugin = this;
        GlobalVariable.dataFolder = getDataFolder();
        SettingManager.initSettingFile(); // Initialize SettingManager class.

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Adaptable Building plugin disabled!");
    }
}
