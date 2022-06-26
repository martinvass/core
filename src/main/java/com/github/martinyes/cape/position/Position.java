package com.github.martinyes.cape.position;

import com.github.martinyes.Main;
import com.github.martinyes.cape.dto.Cape;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Position {

    private static final Map<PositionData, Location> positions = new HashMap<>();
    private static final File data = new File(Main.getInstance().getDataFolder() + "/positions.json");

    static {
        try {
            if (!data.exists()) {
                Files.createFile(data.toPath());
                Main.getInstance().getDataFolder().mkdir();
            }

            if (data.length() != 0) {
                JsonParser parser = new JsonParser();
                Reader reader = Files.newBufferedReader(data.toPath());

                JsonElement root = parser.parse(reader);
                JsonArray array = root.getAsJsonArray();

                array.forEach(e -> {
                    JsonArray data = e.getAsJsonArray();

                    positions.put(Main.getGson().fromJson(data.get(0), PositionData.class),
                            Main.getGson().fromJson(data.get(1), Location.class));
                });

                System.out.println("Loaded " + array.size() + " positions.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Position(Cape.Category category, PositionType type, Location location) {
        positions.put(new PositionData(category, type), location);
    }

    public static Location ofPositionType(PositionType type) {
        return positions.entrySet().stream()
                .filter(e -> e.getKey().getType().equals(type))
                .map(Map.Entry::getValue)
                .findFirst().orElse(null);
    }

    public static Location ofCategory(Cape.Category category) {
        return positions.entrySet().stream()
                .filter(e -> e.getKey().getCategory() != null)
                .filter(e -> e.getKey().getCategory().equals(category))
                .map(Map.Entry::getValue)
                .findFirst().orElse(null);
    }

    public static void save() {
        if (!data.exists() || positions.isEmpty())
            return;

        try {
            Writer writer = Files.newBufferedWriter(data.toPath());

            Main.getGson().toJson(positions, writer);
            writer.close();

            System.out.println("Saved " + positions.size() + " positions.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<PositionData, Location> getPositions() {
        return Collections.unmodifiableMap(positions);
    }

    @AllArgsConstructor
    @Getter
    public static class PositionData {
        private final Cape.Category category;
        private final PositionType type;
    }
}