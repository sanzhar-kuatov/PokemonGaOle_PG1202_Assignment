package domain;

public class MasterBall extends PokeBall {
    @Override
    public String getName() {
        return "Master Ball";
    }

    @Override
    public double getCatchRate() {
        return 1.00;
    }
}
