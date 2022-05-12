package com.grradis.adaptablebuilding.buildmanager;

import com.grradis.adaptablebuilding.miscellaneous.GlobalVariable;
import com.grradis.adaptablebuilding.miscellaneous.Util;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class BuildChecker {
    private String schematicName;
    private byte[] blockDatas;
    // BlockData looping algorithm
    // stdLocation = targetLocation + offset vector
    // stdLocation loop start
    // x -> z -> y looping
    private Integer offsetX, offsetY, offsetZ; // offset from loaded location
    private Integer width, height, length; // width: X axis, height: y axis, length: z axis
    private Player builder;

    public BuildChecker(String schematicName, Player builder) {
        this.schematicName = schematicName;
        this.builder = builder;
        cachingSchematic();
    }

    private boolean checkCompatible() {
        return false;
    }

    public boolean testLoopAllBlock(Location loc) {
        double stdX = loc.getX() + offsetX;
        double stdY = loc.getY() + offsetY;
        double stdZ = loc.getZ() + offsetZ;
        boolean validate = true;
        for (int i = (int) stdX; i <= (int) stdX + width - 1; i++) {
            for (int j = (int) stdY; j <= (int) stdY + height - 1; j++) {
                for (int k = (int) stdZ; k <= (int) stdZ + length - 1; k++) {
                    Location target = new Location(loc.getWorld(), i, j, k);
                    Block targetBlock = target.getBlock();
                    if (!targetBlock.getBlockData().getMaterial().equals(Material.AIR)) {
                        validate = false;
                    }
                }
            }
        }
        if (!validate) {
            builder.sendMessage(Util.translate("&c[AB-DEBUG] &f건축물을 건축할 수 없습니다."));
        }
        return validate;
    }

    private void cachingSchematic() { // Maybe working only version 1 schematic file format.
        File file = new File(GlobalVariable.dataFolder + "/test4.schem");
        try {
            InputStream inputStream = new FileInputStream(file); // Read schematic file
            NBTTagCompound nbtData = NBTCompressedStreamTools.a(inputStream); // Load NBTTagCompound located at the top
            inputStream.close();
            blockDatas = nbtData.m("BlockData"); // Read BlockData byte array. Recorded block info referenced by palette

            NBTTagCompound metaData = nbtData.p("Metadata"); // Read Metadata NBTTagCompound.
            // Read offset vector
            offsetX = metaData.h("WEOffsetX");
            offsetY = metaData.h("WEOffsetY");
            offsetZ = metaData.h("WEOffsetZ");

            width = nbtData.h("Width");
            height = nbtData.h("Height");
            length = nbtData.h("Length");

            Bukkit.broadcastMessage(String.format(Util.translate("&c[AB-DEBUG] &f offsetX: %d offsetY: %d offsetZ: %d Width: %d Height: %d Length: %d"), offsetX, offsetY, offsetZ, width, height, length));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
