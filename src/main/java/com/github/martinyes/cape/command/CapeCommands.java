package com.github.martinyes.cape.command;

import com.github.martinyes.cape.dto.Cape;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CapeCommands {

    @Command(name = "info", desc = "")
    public static void info(@Sender Player player, Cape cape) {
        player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 30));
        player.sendMessage(ChatColor.DARK_RED + cape.getName() + " Cape");
        player.sendMessage("");
        player.sendMessage(ChatColor.WHITE + "Category: " + ChatColor.RED + cape.getCategory().getSlug());
        player.sendMessage(ChatColor.WHITE + "Price: " + ChatColor.RED + ((cape.getPrice() == 0.0D) ? "Free" : cape.getPrice() + " USD"));
        player.sendMessage(ChatColor.WHITE + "Salad pay: " + ChatColor.RED + cape.isSaladPay());

        if (cape.isSaladPay())
            player.sendMessage(ChatColor.WHITE + "Salad link: " + ChatColor.RED + cape.getSaladLink());

        player.sendMessage(ChatColor.WHITE + "Link: " + ChatColor.RED + cape.getLink());
        player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 30));
    }
}