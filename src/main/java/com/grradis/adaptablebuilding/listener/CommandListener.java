package com.grradis.adaptablebuilding.listener;

import com.grradis.adaptablebuilding.buildmanager.BuildingHandler;
import com.grradis.adaptablebuilding.buildmanager.BuildingPreview;
import com.grradis.adaptablebuilding.buildmanager.PreviewList;
import com.grradis.adaptablebuilding.miscellaneous.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("preview")) { // Start building preview.
                if (PreviewList.contain(p)) { // Check player is previewing.
                    BuildingPreview preview = PreviewList.get(p);
                    preview.stopPreview();
                    p.sendMessage(Util.translate("&c[AB-DEBUG] &fPreview Stop!"));
                } else { // Player isn't previewing.
                    BuildingPreview preview = new BuildingPreview("test4.schem", p);
                    preview.startPreview();
                    p.sendMessage(Util.translate("&c[AB-DEBUG] &fPreview Start!"));
                }
            } else if (label.equalsIgnoreCase("test-preview")) {
                if (PreviewList.contain(p)) {
                    BuildingPreview preview = PreviewList.get(p);
                    preview.repeatTask();
                }
            }
        }
        return false;
    }
}
