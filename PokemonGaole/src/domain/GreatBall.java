package domain;

public class GreatBall extends PokeBall {
    @Override
    public String getName() {
        return "Great Ball";
    }

    @Override
    public double getCatchRate() {
        return 0.70;
    }
}
