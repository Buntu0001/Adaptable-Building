package com.grradis.adaptablebuilding.filemanager;

import com.grradis.adaptablebuilding.AdaptableBuilding;
import com.grradis.adaptablebuilding.miscellaneous.GlobalVariable;
import com.grradis.adaptablebuilding.miscellaneous.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SettingManager {
    private static File settingFile;
    private static YamlConfiguration settingYaml;

    public static void initSettingFile() {
        // Check plugin folder is exist. if isn't, make new folder.
        File pluginFolder = new File(String.valueOf(GlobalVariable.dataFolder));
        if (!pluginFolder.exists()) {
            Bukkit.getLogger().info(Util.translate("&c플러그인 폴더가 존재하지 않습니다."));
            Bukkit.getLogger().info(Util.translate("&a폴더를 생성합니다."));
            pluginFolder.mkdir();
        }
        // Check config.yml is exist. if isn't, copy it from the jar's internal files.
        settingFile = new File(GlobalVariable.dataFolder + "/config.yml");
        if (!settingFile.exists()) {
            Bukkit.getLogger().info(Util.translate("&cconfig.yml 파일이 존재하지 않습니다."));
            Bukkit.getLogger().info(Util.translate("&aconfig.yml 파일을 생성합니다."));
            GlobalVariable.plugin.saveResource("config.yml", true); // Extract config.yml resource from jar.
        }

        reloadSetting();
    }

    private static void readSetting() {

    }

    private static void applySetting() {

    }

    public static void reloadSetting() { // Reload setting file. readSetting() -> applySetting()

    }
}
