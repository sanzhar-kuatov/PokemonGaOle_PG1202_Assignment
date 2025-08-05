package domain;

import java.util.Random;

public abstract class PokeBall {
    private static final Random random = new Random();

    public abstract double getCatchRate(); // multiplier like 1.0, 1.5, etc.

    public abstract String getName();

    public boolean tryCatch(Pokemon pokemon) {
        int captureRate = pokemon.getCaptureRate(); // e.g., Pikachu = 190
        double hpFactor = 1.0 - ((double) pokemon.getCurrentHp() / pokemon.getMaxHp()); // lower HP → higher chance
        double baseChance = captureRate * getCatchRate() * (0.5 + hpFactor); // realistic formula

        baseChance = Math.min(255, baseChance); // max cap
        int roll = random.nextInt(256); // random 0–255

        return roll < baseChance;
    }

    public static double calculateCatchProbability(Pokemon pokemon, PokeBall pokeBall) {
        double hpFactor = 1.0 - ((double) pokemon.getCurrentHp() / pokemon.getMaxHp());
        double rawChance = pokemon.getCaptureRate() * pokeBall.getCatchRate() * (0.5 + hpFactor);
        double cappedChance = Math.min(255, rawChance);
        return cappedChance / 255.0;
    }

    @Override
    public String toString() {
        return getName() + " (Rate: " + getCatchRate() + ")";
    }
}
