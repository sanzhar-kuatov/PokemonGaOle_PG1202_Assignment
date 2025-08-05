package domain;

public class WaterPokemon extends Pokemon {
    public WaterPokemon(String name, String moveType, int maxHp, int attack, int speed, int captureRate) {
        super(name, "water", moveType, maxHp, attack, speed, captureRate);
    }

    @Override
    public double getTypeEffectiveness(Pokemon target) {
        return switch (target.getDefenseType()) {
            case "fire" -> 2.0;
            case "water" -> 0.5;
            default -> 1.0;
        };
    }

}
