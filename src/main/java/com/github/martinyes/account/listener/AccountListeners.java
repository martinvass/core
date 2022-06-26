package com.github.martinyes.account.listener;

import com.github.martinyes.Main;
import com.github.martinyes.account.Account;
import com.github.martinyes.account.AccountService;
import com.github.martinyes.account.link.command.LinkCommand;
import com.github.martinyes.cape.preview.PreviewService;
import com.github.martinyes.command.SpawnCommands;
import com.github.martinyes.util.BasicUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountListeners implements Listener {

    private static final Map<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (!Main.isLoaded()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    ChatColor.RED + "The server hasn't been loaded yet. Please try again!");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        AccountService accountService = ((AccountService) Main.getService(AccountService.class));
        PreviewService previewService = ((PreviewService) Main.getService(PreviewService.class));

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false));

        Account account = accountService.create(new Account(player.getUniqueId()));
        previewService.setupCapeModels(account);

        SpawnCommands.spawn(player);
        LinkCommand.link(player);
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        AccountService accountService = ((AccountService) Main.getService(AccountService.class));

        System.out.println("destroying: " + accountService.total());
        accountService.destroy(accountService.find(a -> a.matches(player.getUniqueId())).get());
        System.out.println("destroyed: " + accountService.total());

        event.setQuitMessage(null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCooldown(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("cooldown.bypass"))
            return;

        if (cooldown.containsKey(player.getUniqueId()) && cooldown.get(player.getUniqueId()) + 2000L > System.currentTimeMillis()) {
            player.sendMessage(ChatColor.RED + "Please slow down!");

            event.setCancelled(true);
            return;
        }

        cooldown.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatColor color = (player.hasPermission("core.admin") ?
                ChatColor.BLUE : ChatColor.GREEN);

        String message = event.getMessage();

        if (BasicUtils.isURL(message)) {
            String prefix = ChatColor.DARK_RED + "[Filter]";
            String fullMessage = color + player.getName() + ChatColor.RESET + ": " + ChatColor.WHITE + message;

            Bukkit.getServer().getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("core.admin"))
                    .forEach(p -> p.sendMessage(prefix + " " + fullMessage));

            player.sendMessage(fullMessage);
            event.setCancelled(true);
        }

        event.setFormat(color + "%1$s" + ChatColor.RESET + ": %2$s");
    }
}