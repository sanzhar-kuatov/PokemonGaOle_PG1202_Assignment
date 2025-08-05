package domain;

import static services.Utils.capitalize;

public abstract class Pokemon {
    private String name;
    private int maxHp;
    private int currentHp;
    private int attack;
    private int speed;
    private String defenseType;
    private String moveType;
    private int level = 1;
    private int captureRate;

    public Pokemon(String name, String defenseType, String moveType, int maxHp, int attack, int speed,
            int captureRate) {
        this.name = name;
        this.defenseType = defenseType;
        this.moveType = moveType;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.speed = speed;
        this.captureRate = captureRate;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getAttack() {
        return attack;
    }

    public String getDefenseType() {
        return defenseType;
    }

    public String getMoveType() {
        return moveType;
    }

    public int getLevel() {
        return level;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCaptureRate() {
        return captureRate;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.min(currentHp, maxHp);
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefenseType(String defenseType) {
        this.defenseType = defenseType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setCaptureRate(int captureRate) {
        this.captureRate = captureRate;
    }

    // Gameplay logic
    public void takeDamage(int damage) {
        this.currentHp = Math.max(this.currentHp - damage, 0);
    }

    public void heal(int amount) {
        this.currentHp = Math.min(this.currentHp + amount, maxHp);
    }

    public void revive() {
        this.currentHp = this.maxHp;
    }

    public boolean isFainted() {
        return currentHp <= 0;
    }

    public abstract double getTypeEffectiveness(Pokemon target);

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Type: " + capitalize(defenseType) + "\n" +
                "Level: " + level + "\n" +
                "Speed: " + speed + "\n" +
                "Defense Type: " + capitalize(defenseType) + "\n" +
                "Move Type: " + capitalize(moveType) + "\n" +
                "HP: " + currentHp + " / " + maxHp + "\n" +
                "Attack: " + attack;
    }
}
