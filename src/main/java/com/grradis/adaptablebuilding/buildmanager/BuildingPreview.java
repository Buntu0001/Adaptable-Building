package com.grradis.adaptablebuilding.buildmanager;

import com.grradis.adaptablebuilding.miscellaneous.GlobalVariable;
import com.grradis.adaptablebuilding.miscellaneous.Util;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;

public class BuildingPreview {
    private String schematicName;
    private Player builder;
    private Location targetLocation;
    private EditSession lastSession;
    private Integer repeatingTask = 0;
    private Clipboard previewClipboard;

    public BuildingPreview(String schematicName, Player builder) {
        this.schematicName = schematicName;
        this.builder = builder;

        cachingSchematic(); // Caching schematic into clipboard.
    }

    private void cachingSchematic() {
        // Load schematic file.
        File schemFile = new File(GlobalVariable.plugin.getDataFolder() + "/" + this.schematicName);
        ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
        try {
            // Load schematic file on clipboard.
            ClipboardReader reader = format.getReader(new FileInputStream(schemFile));
            previewClipboard = reader.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startPreview() {
        PreviewList.insert(builder, this);
    }

    public void stopPreview() {
        PreviewList.remove(builder);
    }

    public boolean isLocationChanged(Location location) {
        if (this.targetLocation == null) {
            builder.sendMessage(Util.translate("&c[AB-DEBUG] &fBuildingPreview location is nulled!"));
            this.targetLocation = builder.getTargetBlock(null, 10).getLocation();
        }
        // Remove decimal point.
        String preX = String.valueOf(this.targetLocation.getX());
        String preY = String.valueOf(this.targetLocation.getY());
        String preZ = String.valueOf(this.targetLocation.getZ());
        preX = preX.substring(0, preX.indexOf("."));
        preY = preY.substring(0, preY.indexOf("."));
        preZ = preZ.substring(0, preZ.indexOf("."));

        String aftX = String.valueOf(location.getX());
        String aftY = String.valueOf(location.getY());
        String aftZ = String.valueOf(location.getZ());
        aftX = aftX.substring(0, aftX.indexOf("."));
        aftY = aftY.substring(0, aftY.indexOf("."));
        aftZ = aftZ.substring(0, aftZ.indexOf("."));

        //builder.sendMessage(Util.translate(String.format("&c[AB-DEBUG] &fpreX: %s preY: %s preZ: %s", preX, preY, preZ)));
        //builder.sendMessage(Util.translate(String.format("&c[AB-DEBUG] &faftX: %s aftY: %s aftZ: %s", aftX, aftY, aftZ)));
        if (preX.equals(aftX) && preY.equals(aftY) && preZ.equals(aftZ)) {
            return false;
        } else {
            this.targetLocation = location;
            return true;
        }
    }

    private void pasteOperation() {
        World world = BukkitAdapter.adapt(targetLocation.getWorld()); // Adapt bukkit world -> worldedit world
        try {
            // Paste schematic file on world.
            EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
            Operation operation = new ClipboardHolder(previewClipboard)
                    .createPaste(session)
                    .to(BlockVector3.at(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation); // worldedit operation dispose.
            session.flushSession(); // worldedit session flush.
            lastSession = session;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void cancelOperation() {
        try {
            World world = BukkitAdapter.adapt(targetLocation.getWorld());
            EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
            lastSession.undo(session); // Undo session.
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Get targeted block's location ignore building.
    private Location getTransparentLocation() {
        Location loc;
        if (lastSession != null) {
            cancelOperation();
            loc = builder.getTargetBlock(null, 30).getLocation();
            pasteOperation();
        } else {
            loc = builder.getTargetBlock(null, 30).getLocation();
        }
        return loc;
    }

    public void showPreview() {
        if (lastSession != null) {
            cancelOperation();
            pasteOperation();
        } else {
            pasteOperation();
        }
    }

    public void testPreview() {
        pasteOperation();
        builder.sendMessage("pasted!");
        Bukkit.getScheduler().scheduleSyncDelayedTask(GlobalVariable.plugin, new Runnable() {
            @Override
            public void run() {
                builder.sendMessage("canceled!");
                cancelOperation();
            }
        }, 7 * 20L);
    }

    public void repeatTask() {
        if (repeatingTask == 0) {
            repeatingTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(GlobalVariable.plugin, new Runnable() {
                @Override
                public void run() {
                    if (isLocationChanged(getTransparentLocation())) {
                        showPreview();
                    }
                }
            }, 0, 5L);
        } else {
            Bukkit.getScheduler().cancelTask(repeatingTask);
            cancelOperation();
            repeatingTask = 0;
        }
    }
}
