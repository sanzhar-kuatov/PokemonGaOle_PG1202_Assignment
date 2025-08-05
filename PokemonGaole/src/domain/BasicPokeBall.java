package domain;

public class BasicPokeBall extends PokeBall {
    @Override
    public double getCatchRate() {
        return 0.50;
    }

    @Override
    public String getName() {
        return "Basic Ball";
    }
}
