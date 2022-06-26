package com.github.martinyes.command;

import com.github.martinyes.Main;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PermsCommand {

    @Command(name = "add", desc = "Add permission")
    @Require("core.admin")
    public void addPerm(@Sender Player player, Player target, String perm) {
        target.addAttachment(Main.getInstance(), perm, true);

        player.sendMessage(ChatColor.BLUE + "Updated permissions for " + target.getName());
    }

    @Command(name = "remove", desc = "Remove permission")
    @Require("core.admin")
    public void removePerm(@Sender Player player, Player target, String perm) {
        player.sendMessage(ChatColor.BLUE + "Updated permissions for " + target.getName());
    }

    @Command(name = "check", desc = "Check permission")
    @Require("core.admin")
    public void checkPerm(@Sender Player player, Player target, String perm) {
        String msg = (target.hasPermission(perm) ? "has" : "doesn't have");

        player.sendMessage(ChatColor.WHITE + target.getName() + ChatColor.RED +
                " " + msg + " permission for " + ChatColor.WHITE + perm);
    }
}