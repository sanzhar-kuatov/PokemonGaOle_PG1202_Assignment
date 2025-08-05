package domain;

import java.util.Random;

public class PokeBallFactory {
    public static PokeBall createRandomBall() {
        int roll = new Random().nextInt(100); // 0 to 99

        if (roll < 60) {
            return new BasicPokeBall(); // 60%
        } else if (roll < 85) {
            return new GreatBall(); // 25%
        } else if (roll < 95) {
            return new UltraBall(); // 10%
        } else {
            return new MasterBall(); // 5%
        }
    }
}
