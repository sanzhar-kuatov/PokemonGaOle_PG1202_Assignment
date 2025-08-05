package domain;

public class ElectricPokemon extends Pokemon {
    public ElectricPokemon(String name, String moveType, int maxHp, int attack, int speed, int captureRate) {
        super(name, "electric", moveType, maxHp, attack, speed, captureRate);
    }

    @Override
    public double getTypeEffectiveness(Pokemon target) {
        return switch (target.getDefenseType()) {
            case "water" -> 2.0;
            case "electric" -> 0.5;
            default -> 1.0;
        };
    }
}
