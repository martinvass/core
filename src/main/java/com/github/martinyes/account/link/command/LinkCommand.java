package com.github.martinyes.account.link.command;

import com.github.martinyes.account.link.JwtService;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class LinkCommand {

    @Command(name = "", desc = "Link account")
    public static void link(@Sender Player player) {
        String prefix = "§c§lCore §8❘ §r";
        String link = JwtService.getOrCreateToken(player.getName());

        TextComponent text = new TextComponent(prefix + "Click here to link "
                + ChatColor.DARK_RED + player.getName() + ChatColor.WHITE + " to your account.");

        text.setColor(ChatColor.WHITE);
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Link your account!")
                        .color(ChatColor.GRAY)
                        .color(ChatColor.ITALIC)
                        .create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));

        player.spigot().sendMessage(text);
    }
}