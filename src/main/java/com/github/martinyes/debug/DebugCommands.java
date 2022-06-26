package com.github.martinyes.debug;

import com.github.martinyes.Main;
import com.github.martinyes.account.Account;
import com.github.martinyes.account.AccountService;
import com.github.martinyes.cape.CapeService;
import com.github.martinyes.cape.dto.Cape;
import com.github.martinyes.cape.position.Position;
import com.github.martinyes.news.NewsService;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DebugCommands {

    @Command(name = "", desc = "")
    @Require("core.admin")
    public void debug(@Sender Player player) {
        AccountService accountService = ((AccountService) Main.getService(AccountService.class));
        CapeService capeService = ((CapeService) Main.getService(CapeService.class));
        NewsService newsService = ((NewsService) Main.getService(NewsService.class));

        Account account = accountService.find(a -> a.getUuid().equals(player.getUniqueId())).get();

        player.sendMessage("Account: " + account.toString());
        player.sendMessage("Accounts count: " + accountService.total());
        player.sendMessage("Capes count: " + capeService.total());
        player.sendMessage("Cape models count: " + capeService.totalBy(c -> c.getModel() != null));

        for (Map.Entry<Cape.Category, List<Cape>> entry : capeService.getCapes().entrySet())
            player.sendMessage(" - " + entry.getKey().getSlug() + " capes count: " + entry.getValue().size());

        player.sendMessage("News count: " + newsService.total());
        player.sendMessage("Positions count: " + Position.getPositions().size());
        Position.getPositions().keySet().forEach(p -> System.out.println(" - " + p.getCategory().getSlug()));
    }

    @Command(name = "capes", desc = "")
    @Require("core.admin")
    public void listCapes(@Sender Player player, Cape.Category category) {
        CapeService capeService = ((CapeService) Main.getService(CapeService.class));

        Collection<Cape> capes = capeService.find(c -> c.getCategory().equals(category)).getData();
        player.sendMessage(Arrays.toString(capes.stream().map(Cape::getName).toArray()));
    }

    @Command(name = "show models", desc = "")
    @Require("core.admin")
    public void showModels(@Sender Player player) {
        CapeService capeService = ((CapeService) Main.getService(CapeService.class));

        Collection<Cape> capes = capeService.find(c -> c.getModel() != null).getData();
        player.sendMessage("size: " + capes.size());
        //for (Cape cape : capes) Main.getInstance().getEntityUtils().showEntity(player, cape.getModel().getEntity());

        player.sendMessage("All cape models are now visible.");
    }

    @Command(name = "hide models", desc = "")
    @Require("core.admin")
    public void hideModels(@Sender Player player) {
        CapeService capeService = ((CapeService) Main.getService(CapeService.class));

        Collection<Cape> capes = capeService.find(c -> c.getModel() != null).getData();
        player.sendMessage("size: " + capes.size());
        //for (Cape cape : capes) Main.getInstance().getEntityUtils().hideEntity(player, cape.getModel().getEntity());

        player.sendMessage("All cape models are now invisible.");
    }
}