package domain;

public class FirePokemon extends Pokemon {
    public FirePokemon(String name, String moveType, int maxHp, int attack, int speed, int captureRate) {
        super(name, "fire", moveType, maxHp, attack, speed, captureRate);
    }

    @Override
    public double getTypeEffectiveness(Pokemon target) {
        return switch (target.getDefenseType().toLowerCase()) {
            case "water", "fire" -> 0.5;
            default -> 1.0;
        };
    }
}
