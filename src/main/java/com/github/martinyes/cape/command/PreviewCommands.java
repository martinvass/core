package com.github.martinyes.cape.command;

import com.github.martinyes.cape.position.Position;
import com.github.martinyes.cape.position.PositionType;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PreviewCommands {

    @Command(name = "setloc", desc = "")
    public static void setPreviewLocation(@Sender Player player) {
        Location loc = player.getLocation();

        new Position(null, PositionType.SINGLE_PREVIEW, loc);
        player.sendMessage(ChatColor.BLUE + "Cape preview point updated!");
    }
}