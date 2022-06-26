package com.github.martinyes.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class New {

    private final TextComponent text;
    private final long expireAt;
    private final Consumer<Player> action;
}