package com.github.martinyes.news.task;

import com.github.martinyes.news.NewsService;
import com.github.martinyes.news.dto.New;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class NewsTask implements Runnable {

    private final NewsService newsService;

    @Override
    public void run() {
        // If there is no player online we just return because there is no one to send the message for
        if (Bukkit.getOnlinePlayers().size() == 0 || newsService.total() == 0)
            return;

        New randomNew = newsService.getRandom();

        // We don't use Bukkit#broadcastMessage because we don't want to spam the console every min
        Bukkit.getOnlinePlayers().forEach(p -> newsService.print(p, randomNew));
    }
}