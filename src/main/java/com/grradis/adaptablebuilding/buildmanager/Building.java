package com.grradis.adaptablebuilding.buildmanager;

import org.bukkit.entity.Player;

import javax.xml.stream.Location;

public class Building {

    private String schematicName;
    private String displayName;
    private String builderName;
    private Player builder;
    private Location buildingLocation;

    public Building(String schematicName, String displayName, String builderName, Location location) {
        this.schematicName = schematicName;
        this.displayName = displayName;
        this.builderName = builderName;
        this.buildingLocation = location;
    }

    public void setBuilder(Player builder) {
        this.builder = builder;
    }

    public String getSchematicName() {
        return schematicName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBuilderName() {
        return builderName;
    }

    public Player getBuilder() {
        return builder;
    }

    public Location getBuildingLocation() {
        return buildingLocation;
    }
}
