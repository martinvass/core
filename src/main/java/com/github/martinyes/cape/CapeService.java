package com.github.martinyes.cape;

import com.github.martinyes.Main;
import com.github.martinyes.Service;
import com.github.martinyes.cape.dto.Cape;
import com.github.martinyes.wrapper.WrappedResult;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CapeService implements Service<Cape> {

    private final Map<Cape.Category, List<Cape>> capes = new HashMap<>();

    @Getter private boolean loaded;
    private URL rootURL;

    {
        try {
            rootURL = new URL("https://127.0.0.1/api/capes");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long load() {
        Preconditions.checkState(!loaded);

        long started = System.currentTimeMillis();

        try {
            URLConnection connection = rootURL.openConnection();

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.setUseCaches(true);
            connection.connect();

            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(new InputStreamReader((InputStream) connection.getContent()));
            JsonObject obj = root.getAsJsonObject();

            int pages = obj.get("totalPages").getAsInt();
            for (int i = 1; i <= pages; i++)
                fetchByPage(i);

        } catch (IOException e) {
            e.printStackTrace();
        }

        long elapsed = System.currentTimeMillis() - started;
        Main.getInstance().getLogger()
                .info(String.format("Loaded %d capes in %d ms.", this.total(), elapsed));

        loaded = true;
        return elapsed;
    }

    private void fetchByPage(int page) throws IOException {
        URLConnection connection = new URL(String.format("https://127.0.0.1/api/capes?page=%d", page))
                .openConnection();

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.setUseCaches(true);
        connection.connect();

        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(new InputStreamReader((InputStream) connection.getContent()));
        JsonArray capes = root.getAsJsonObject().getAsJsonArray("capes");

        capes.forEach(e -> {
            try {
                new Cape(e.getAsJsonObject());
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });
    }

    @Override
    public Cape create(Cape cape) {
        this.capes.computeIfAbsent(cape.getCategory(), k -> new ArrayList<>()).add(cape);

        return cape;
    }

    public WrappedResult<Cape> find(Predicate<Cape> predicate) {
        return new WrappedResult<>(flatList(), predicate);
    }

    public int totalBy(Predicate<Cape> predicate) {
        return (int) flatList().stream().filter(predicate).count();
    }

    public int total() {
        return flatList().size();
    }

    public List<Cape> flatList() {
        return this.capes.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Map<Cape.Category, List<Cape>> getCapes() {
        return Collections.unmodifiableMap(this.capes);
    }
}