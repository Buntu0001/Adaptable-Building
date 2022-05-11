package com.grradis.adaptablebuilding.buildmanager;

import org.bukkit.Location;

public class BuildChecker {
    private String schematicName;
    public BuildChecker(String schematicName, Location location) {
        this.schematicName = schematicName;
    }

    private boolean checkCompatible() {
        return false;
    }

    private void cachingSchematic() {

    }
}
