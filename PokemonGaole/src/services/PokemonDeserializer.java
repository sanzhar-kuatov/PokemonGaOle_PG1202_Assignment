package services;

import com.google.gson.*;
import domain.*;

import java.lang.reflect.Type;

public class PokemonDeserializer implements JsonDeserializer<Pokemon> {
    @Override
    public Pokemon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        String name = obj.get("name").getAsString();
        int health = obj.get("maxHp").getAsInt(); // or "health" if using that key
        int attack = obj.get("attack").getAsInt(); // or "damage" if using that key
        int speed = obj.get("speed").getAsInt();
        String defenseType = obj.get("defenseType").getAsString();
        String moveType = obj.get("moveType").getAsString().toLowerCase();
        int captureRate = obj.has("captureRate") ? obj.get("captureRate").getAsInt() : 0;

        return switch (moveType) {
            case "electric" -> new ElectricPokemon(name, moveType, health, attack, speed, captureRate);
            case "fire" -> new FirePokemon(name, moveType, health, attack, speed, captureRate);
            case "water" -> new WaterPokemon(name, moveType, health, attack, speed, captureRate);
            case "fighting" -> new FightingPokemon(name, moveType, health, attack, speed, captureRate);
            default -> {
                System.err.println("Unknown moveType: " + moveType + ", using generic fallback.");
                yield new Pokemon(name, defenseType, moveType, health, attack, speed, captureRate) {
                    @Override
                    public double getTypeEffectiveness(Pokemon target) {
                        return 1.0;
                    }
                };
            }
        };
    }
}
