package com.github.martinyes.cape.command;

import com.github.martinyes.cape.dto.Cape;
import com.github.martinyes.cape.position.Position;
import com.github.martinyes.cape.position.PositionType;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PositionCommands {

    @Command(name = "add", desc = "")
    @Require("core.command.pos")
    public void addPos(@Sender Player player, Cape.Category category) {
        new Position(category, PositionType.CATEGORY_PREVIEW, player.getLocation());

        player.sendMessage(ChatColor.RED + "Added a new position for " + ChatColor.WHITE + category.name() + ".");
    }
}