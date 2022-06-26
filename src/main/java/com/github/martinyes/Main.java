package com.github.martinyes;

import com.github.martinyes.account.AccountService;
import com.github.martinyes.account.link.command.LinkCommand;
import com.github.martinyes.account.listener.AccountListeners;
import com.github.martinyes.cape.CapeService;
import com.github.martinyes.cape.adapter.CapeCategoryTypeAdapter;
import com.github.martinyes.cape.adapter.CapeSlugTypeAdapter;
import com.github.martinyes.cape.command.CapeCommands;
import com.github.martinyes.cape.command.PositionCommands;
import com.github.martinyes.cape.command.PreviewCommands;
import com.github.martinyes.cape.command.SearchCapeCommand;
import com.github.martinyes.cape.dto.Cape;
import com.github.martinyes.cape.position.Position;
import com.github.martinyes.cape.preview.PreviewService;
import com.github.martinyes.command.PermsCommand;
import com.github.martinyes.command.SpawnCommands;
import com.github.martinyes.debug.DebugCommands;
import com.github.martinyes.listener.ServerListeners;
import com.github.martinyes.news.NewsService;
import com.github.martinyes.serialization.LocationAdapter;
import com.github.martinyes.util.SSLUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import lombok.Getter;
import me.lucko.helper.Services;
import me.lucko.helper.npc.CitizensNpcFactory;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public class Main extends JavaPlugin {

    @Getter private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
            .setPrettyPrinting().serializeNulls().create();
    @Getter private static final Map<Service<?>, Long> initTimes = Maps.newHashMap();
    @Getter private static final List<Service<?>> services = Lists.newArrayList();

    @Getter private static Main instance;
    @Getter private static boolean loaded;

    private CitizensNpcFactory npcFactory;

    public static Service<?> getService(Class<? extends Service<?>> clazz) {
        return services.stream().filter(s -> s.getClass().equals(clazz)).findFirst().orElse(null);
    }

    @Override
    public void onEnable() {
        instance = this;

        SSLUtil.disableSslVerification();

        if (Services.get(CitizensNpcFactory.class).isPresent()) {
            this.npcFactory = Services.get(CitizensNpcFactory.class).get();
        }

        services.addAll(Arrays.asList(
                new NewsService(),
                new CapeService(),
                new PreviewService(),
                new AccountService()
        ));
        services.forEach(this::initService);

        Arrays.asList(new AccountListeners(), new ServerListeners()).forEach(this::addListener);
        addCommands();

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setTime(6_000L);
        }

        loaded = true;
    }

    @Override
    public void onDisable() {
        instance = null;

        Position.save();

        this.npcFactory.closeAndReportException();
    }

    private void addCommands() {
        CommandService service = Drink.get(this);

        service.bind(Cape.Category.class).toProvider(new CapeCategoryTypeAdapter());
        service.bind(Cape.class).toProvider(new CapeSlugTypeAdapter());

        service.register(new LinkCommand(), "link", "verify");
        service.register(new SpawnCommands(), "spawn");
        service.register(new CapeCommands(), "cape");
        service.register(new PreviewCommands(), "preview", "showcase");
        service.register(new PositionCommands(), "pos");
        service.register(new SearchCapeCommand(), "search", "find", "searchcape", "findcape");
        service.register(new PermsCommand(), "perms", "permission", "permissions");
        service.register(new DebugCommands(), "debug");

        service.registerCommands();
    }

    private void addListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void initService(Service<?> service) {
        initTimes.put(service, service.load());
    }
}