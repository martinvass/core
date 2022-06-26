package com.github.martinyes.news;

import com.github.martinyes.Main;
import com.github.martinyes.Service;
import com.github.martinyes.news.dto.New;
import com.github.martinyes.news.task.NewsTask;
import com.github.martinyes.wrapper.WrappedResult;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

// TODO: ability to change news task delay in-game
public class NewsService implements Service<New> {

    private final List<New> news = Lists.newArrayList();

    private boolean loaded;

    @Override
    public long load() {
        Preconditions.checkState(!loaded);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(),
                new NewsTask(this), 10 * 20L, 120L);

        loaded = true;
        return -1;
    }

    @Override
    public New create(New n) {
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }

    @Override
    public WrappedResult<New> find(Predicate<New> predicate) {
        return new WrappedResult<>(this.news, predicate);
    }

    @Override
    public int totalBy(Predicate<New> predicate) {
        return (int) news.stream().filter(predicate).count();
    }

    @Override
    public int total() {
        return news.size();
    }

    public List<New> getNews() {
        return Collections.unmodifiableList(this.news);
    }

    public void print(Player player, New n) {
        String prefix = "&c&lCore &8❘ &r";

        if (n.getAction() != null)
            n.getAction().accept(player);

        n.getText().setText(ChatColor.translateAlternateColorCodes(
                '&', prefix + n.getText().getText()));

        player.sendMessage("");
        player.spigot().sendMessage(n.getText());
        player.sendMessage("");
    }

    public New getRandom() {
        final List<New> clone = new ArrayList<>(news);
        Collections.shuffle(clone);

        return clone.get(0);
    }
}