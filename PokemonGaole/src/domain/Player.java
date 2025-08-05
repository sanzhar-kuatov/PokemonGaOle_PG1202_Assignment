package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private String username;
    private ArrayList<Pokemon> inventory = new ArrayList<>();
    private int highestScore = 0;
    private int coins = 0;

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighestScore() {
        return this.highestScore;
    }

    public void setHighestScore(int newHighestScore) {
        if (newHighestScore > this.highestScore) this.highestScore = newHighestScore;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public boolean checkBalance(int coins) {
        return coins <= this.coins;
    }

    public List<Pokemon> getInventory() {
        return Collections.unmodifiableList(this.inventory); // Prevent modification without using the class function
    }

    public void addPokemon(Pokemon pokemon) {
        this.inventory.add(pokemon);
    }

    @Override
    public String toString() {
        return "Player\nUsername: " + this.username + "\n;Inventory: " + this.inventory
                + "\nScore: " + this.highestScore;
    }


    public void addCoins(int coinsWon) {
        if (coinsWon < 0) {
            throw new IllegalArgumentException("You cannot provide negative coins");
        }
        this.coins += coinsWon;
    }

    public int getCoins() {
        return this.coins;
    }

    public void spendCoins(int coinsSpent) {
        if (coinsSpent < 0) {
            throw new IllegalArgumentException("You cannot provide negative coins");
        }
        if (!checkBalance(coinsSpent)) {
            throw new IllegalArgumentException("You do not have enough coins to spend");
        }
        this.coins -= coinsSpent;
    }
}
