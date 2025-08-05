package domain;

public class FightingPokemon extends Pokemon {

    public FightingPokemon(String name, String moveType, int maxHp, int attack, int speed, int captureRate) {
        super(name, "fighting", moveType, maxHp, attack, speed, captureRate);
    }

    @Override
    public double getTypeEffectiveness(Pokemon target) {
        return 1.0;
    }
}
