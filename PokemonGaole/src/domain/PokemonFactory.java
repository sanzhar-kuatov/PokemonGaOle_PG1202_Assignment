package domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static services.Utils.capitalize;

public class PokemonFactory {
    private static final String DEFAULT_PATH = "src/resources/data/pokemons.json";

    public static Pokemon createRandomPokemon() {
        List<Pokemon> allPokemons = loadFromJson(DEFAULT_PATH);
        if (allPokemons.isEmpty()) {
            throw new RuntimeException("No Pokémon data loaded from JSON.");
        }

        Random rand = new Random();
        return allPokemons.get(rand.nextInt(allPokemons.size()));
    }

    public static List<Pokemon> createRandomPokemons(int quantity) {
        List<Pokemon> allPokemons = loadFromJson(DEFAULT_PATH);
        if (allPokemons.isEmpty()) {
            throw new RuntimeException("No Pokémon data loaded from JSON.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        Random rand = new Random();
        List<Pokemon> randomPokemons = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Pokemon randomPokemon = allPokemons.get(rand.nextInt(allPokemons.size()));
            // Optional: Clone if needed to avoid modifying shared objects
            randomPokemons.add(randomPokemon);
        }

        return randomPokemons;
    }

    public static List<Pokemon> loadFromJson(String path) {
        try (FileReader reader = new FileReader(path)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<PokemonJson>>() {
            }.getType();
            List<PokemonJson> rawList = gson.fromJson(reader, listType);

            List<Pokemon> pokemons = new ArrayList<>();
            for (PokemonJson raw : rawList) {
                pokemons.add(createTypedPokemon(raw));
            }
            return pokemons;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static Pokemon createTypedPokemon(PokemonJson raw) {
        String moveType = raw.moveType.toLowerCase();
        String name = capitalize(raw.name);

        switch (moveType) {
            case "electric":
                return new ElectricPokemon(name, raw.moveType, raw.health, raw.damage, raw.speed, raw.captureRate);
            case "fire":
                return new FirePokemon(name, raw.moveType, raw.health, raw.damage, raw.speed, raw.captureRate);
            case "water":
                return new WaterPokemon(name, raw.moveType, raw.health, raw.damage, raw.speed, raw.captureRate);
            case "fighting":
                return new FightingPokemon(name, raw.moveType, raw.health, raw.damage, raw.speed, raw.captureRate);
            default:
                throw new IllegalArgumentException("Unknown move type: " + moveType);
        }
    }

    // Internal JSON mapping classes
    private static class PokemonJson {
        String name;
        int health;
        int damage;
        int speed;
        String defenseType;
        String moveType;
        int captureRate;
    }
}
