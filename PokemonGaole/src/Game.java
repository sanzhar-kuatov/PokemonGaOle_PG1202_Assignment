import domain.Player;
import domain.Pokemon;
import domain.PokemonFactory;
import services.Battle;
import services.Database;

import java.util.List;
import java.util.Scanner;

public class Game {
    private final Database storage = new Database();
    private final Scanner scanner = new Scanner(System.in);
    private Player currentPlayer;

    public void start() {
        showWelcome();
        selectPlayer();
        gameIntroduction();

        while (true) {
            showMenu();
            String input = getUserInput();

            switch (input) {
                case "1" -> startBattle();
                case "2" -> viewBackpack();
                case "3" -> viewLeaderboard();
                case "4" -> {
                    printMessage("Exiting game.");
                    return;
                }
                default -> printMessage("Invalid input. Try again.");
            }
        }
    }

    private void showWelcome() {
        System.out.println("=== Welcome to the Battle Game ===");
    }

    private void showMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Start Battle");
        System.out.println("2. View Backpack");
        System.out.println("3. View Leaderboard");
        System.out.println("4. Exit");
        System.out.print("> ");
    }

    private String getUserInput() {
        return scanner.nextLine();
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private void gameIntroduction() {
        if (currentPlayer.getInventory().isEmpty()) {
            Pokemon pokemon = newUserWelcomePokemon(PokemonFactory.createRandomPokemons(3));
            currentPlayer.addPokemon(pokemon);
            storage.updatePlayer(currentPlayer);
        }
    }

    private void selectPlayer() {
        List<Player> players = storage.getAllPlayers();
        String name;

        do {
            name = promptForUserSelection(players);

            if (name == null || name.trim().isEmpty()) {
                printMessage("Player selection cancelled or invalid input. Please try again.");
                continue;
            }

            if (storage.playerExists(name)) {
                printMessage("Now playing as the user " + name + ".");
                this.currentPlayer = storage.getPlayerByName(name);
                return;
            }

            if (!isValidPlayerName(name)) {
                printMessage("Invalid player name. Name must be between 2–20 characters and contain only letters and numbers.");
                continue;
            }

            Player newPlayer = new Player(name);
            storage.addPlayer(newPlayer);
            this.currentPlayer = newPlayer;
            printMessage("Player created. Now playing as the user " + name + ".");
            return;

        } while (true);
    }

    private String promptForUserSelection(List<Player> players) {
        System.out.println("\nAvailable players:");
        if (players.isEmpty()) {
            System.out.println("(No existing players)");
        } else {
            for (int i = 0; i < players.size(); i++) {
                System.out.printf("%d. %s%n", (i + 1), players.get(i).getUsername());
            }
        }

        System.out.println("\nOptions:");
        System.out.println("- Enter a number to select an existing player");
        System.out.println("- Enter 'new' to create a new player");
        System.out.print("> ");

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("new")) {
            System.out.print("Enter new player name: ");
            return scanner.nextLine().trim();
        }

        try {
            int selection = Integer.parseInt(input);
            if (selection > 0 && selection <= players.size()) {
                return players.get(selection - 1).getUsername();
            }
        } catch (NumberFormatException ignored) {}

        return "";
    }

    private boolean isValidPlayerName(String name) {
        return name != null && name.length() >= 2 && name.length() <= 20 && name.matches("[a-zA-Z0-9]+");
    }

    private void viewBackpack() {
        List<Pokemon> inventory = currentPlayer.getInventory();

        System.out.printf("%nCoins: %d%n", currentPlayer.getCoins());

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Inventory: (empty)");
            return;
        }

        System.out.println("Inventory:");
        for (int i = 0; i < inventory.size(); i++) {
            Pokemon p = inventory.get(i);
            System.out.printf("%d. %s | HP: %d | Attack: %d | Speed: %d | Defense Type: %s | Move Type: %s%n",
                    i + 1, p.getName(), p.getCurrentHp(), p.getAttack(), p.getSpeed(), p.getDefenseType(), p.getMoveType());
        }
    }

    private void viewLeaderboard() {
        List<Player> players = storage.getAllPlayers();
        players.sort((p1, p2) -> p2.getHighestScore() - p1.getHighestScore());

        if (players.isEmpty()) {
            System.out.println("\nLeaderboard is empty.");
            return;
        }

        System.out.println("\n=== LEADERBOARD ===");
        System.out.println("Rank | Player Name | Score");
        System.out.println("-------------------------");

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("%-4d | %-10s | %d%n", i + 1, player.getUsername(), player.getHighestScore());
        }
    }

    private Pokemon newUserWelcomePokemon(List<Pokemon> pokemonList) {
        System.out.println("\nWelcome to the Pokemon Gaole game! Here's your welcome gift!");
        System.out.println("Choose a Pokemon to receive:");
        for (int i = 0; i < pokemonList.size(); i++) {
            Pokemon pokemon = pokemonList.get(i);
            System.out.printf("%d. %s%n", i + 1, pokemon.getName());
        }

        while (true) {
            System.out.print("> ");
            try {
                int selection = Integer.parseInt(scanner.nextLine().trim());
                if (selection > 0 && selection <= pokemonList.size()) {
                    Pokemon selected = pokemonList.get(selection - 1);
                    System.out.println("You received " + selected.getName() + "!");
                    return selected;
                } else {
                    System.out.printf("Invalid selection. Enter a number between 1 and %d.%n", pokemonList.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void startBattle() {
        Battle battle = new Battle(currentPlayer);
        battle.startBattle();
        storage.updatePlayer(currentPlayer);
    }
}
