package com.github.martinyes.command;

import com.github.martinyes.Main;
import com.github.martinyes.account.Account;
import com.github.martinyes.account.AccountService;
import com.github.martinyes.cape.position.Position;
import com.github.martinyes.cape.position.PositionType;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnCommands {

    @Command(name = "", desc = "Spawn command")
    public static void spawn(@Sender Player player) {
        AccountService accountService = ((AccountService) Main.getService(AccountService.class));

        Account account = accountService.find(a -> a.matches(player.getUniqueId())).get();
        /*Location loc = Main.getGson().fromJson(
                Main.getInstance().getConfig().getString("spawn"),
                Location.class
        );*/
        Location loc = Position.ofPositionType(PositionType.SPAWN);

        if (loc == null) {
            player.sendMessage(ChatColor.RED + "There is no spawn point.");
            return;
        }

        if (account.getPreviewNpc() != null)
            account.destroyPreviewNPC(true);

        player.teleport(loc);
    }

    @Command(name = "set", desc = "Set spawn command")
    public void setSpawn(@Sender Player player) {
        Location loc = player.getLocation();

        /*Main.getInstance().getConfig().set("spawn", Main.getGson().toJson(loc));
        Main.getInstance().saveConfig();*/

        new Position(null, PositionType.SPAWN, loc);

        player.sendMessage(ChatColor.BLUE + "Spawn point updated!");
    }
}