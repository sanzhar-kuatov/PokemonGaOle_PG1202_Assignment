package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import domain.Player;
import domain.Pokemon;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Database {
    private final Path DATABASE_PATH = Paths.get("resources/database.json");
    private final Gson gson;
    private final Map<String, Player> playerMap;

    public Database() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Pokemon.class, new PokemonDeserializer())
                .setPrettyPrinting()
                .create();
        this.playerMap = new LinkedHashMap<>();
        loadFromFile();
    }

    public List<Player> getAllPlayers() {
        return new ArrayList<>(playerMap.values());
    }

    public Player getPlayerByName(String name) {
        return playerMap.getOrDefault(name.toLowerCase(), null);
    }

    public boolean playerExists(String name) {
        return playerMap.containsKey(name.toLowerCase());
    }

    public void addPlayer(Player player) {
        String key = player.getUsername().toLowerCase();
        if (!playerMap.containsKey(key)) {
            playerMap.put(key, player);
            saveToFile();
        }
    }

    public void updatePlayer(Player updated) {
        String key = updated.getUsername().toLowerCase();
        if (playerMap.containsKey(key)) {
            playerMap.put(key, updated);
            saveToFile();
        }
    }

    public void deletePlayer(String name) {
        if (playerMap.remove(name.toLowerCase()) != null) {
            saveToFile();
        }
    }

    private void saveToFile() {
        try {
            Files.createDirectories(DATABASE_PATH.getParent());
            String json = gson.toJson(playerMap);
            Files.writeString(DATABASE_PATH, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        if (!Files.exists(DATABASE_PATH)) return;

        try {
            String json = Files.readString(DATABASE_PATH);
            Type type = new TypeToken<Map<String, Player>>() {}.getType();
            Map<String, Player> loaded = gson.fromJson(json, type);
            if (loaded != null) playerMap.putAll(loaded);
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
}
