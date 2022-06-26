package com.github.martinyes.cape.command;

import com.github.martinyes.Main;
import com.github.martinyes.account.Account;
import com.github.martinyes.account.AccountService;
import com.github.martinyes.cape.dto.Cape;
import com.github.martinyes.cape.position.Position;
import com.github.martinyes.cape.position.PositionType;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SearchCapeCommand {

    @Command(name = "", desc = "")
    public void search(@Sender Player player, Cape cape) {
        AccountService accountService = ((AccountService) Main.getService(AccountService.class));

        Account account = accountService.find(a -> a.matches(player.getUniqueId())).get();
        Location loc = Position.ofPositionType(PositionType.SINGLE_PREVIEW);

        if (loc == null) {
            player.sendMessage(ChatColor.RED + "There is no preview location.");
            return;
        }

        Location modified = loc.clone();
        modified.setZ(loc.getZ() + 2);

        player.teleport(modified);

        account.createPreviewNPC(cape, loc);
        player.sendMessage(ChatColor.GREEN + "You are viewing " + ChatColor.RED + cape.getName() + ChatColor.GREEN + " cape.");
    }
}