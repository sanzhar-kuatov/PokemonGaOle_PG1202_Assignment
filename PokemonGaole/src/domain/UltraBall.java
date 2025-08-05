package domain;

public class UltraBall extends PokeBall {
    @Override
    public String getName() {
        return "Ultra Ball";
    }

    @Override
    public double getCatchRate() {
        return 0.90;
    }
}
