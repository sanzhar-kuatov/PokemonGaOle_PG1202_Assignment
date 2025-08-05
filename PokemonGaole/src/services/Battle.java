package services;

import domain.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Battle {
	private List<Pokemon> playerTeam;
	private List<Pokemon> enemyTeam;

	private final Player currentPlayer;
	private final Random random = new Random();
	private final Scanner scanner = new Scanner(System.in);

	private int totalDamageDealt = 0;
	private int totalDamageReceived = 0;

	public Battle(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.playerTeam = new ArrayList<>();
		this.enemyTeam = new ArrayList<>();
	}

	public void startBattle() {
		if (!setupTeams()) {
			return;
		}

		List<Pokemon> turnOrder = setTurnOrder();
		System.out.println("\n--- BATTLE START! ---");
		System.out.println("Turn order is determined by speed!");

		while (true) {
			System.out.println("\n--- NEW ROUND ---");
			for (Pokemon attacker : turnOrder) {
				if (attacker.isFainted())
					continue;
				if (isTeamFainted(playerTeam) || isTeamFainted(enemyTeam))
					break;

				displayBattleStatus();
				System.out.println("It's " + attacker.getName() + "'s turn!");

				if (playerTeam.contains(attacker)) {
					performPlayerAttack(attacker);
				} else {
					performEnemyAttack(attacker);
				}

				System.out.println("(Press Enter to continue...)");
				scanner.nextLine();
			}

			if (isTeamFainted(playerTeam)) {
				handlePlayerDefeat();
				break;
			}
			if (isTeamFainted(enemyTeam)) {
				handlePlayerVictory();
				break;
			}
		}

		reviveAllPokemons();
	}

	private boolean setupTeams() {
		if (!choosePlayerTeam())
			return false;
		chooseWildEnemyTeam();
		return true;
	}

	private boolean choosePlayerTeam() {
		List<Pokemon> playerRoster = currentPlayer.getInventory();
		if (playerRoster.isEmpty()) {
			System.out.println("You have no Pokémon to battle with! Go catch some first.");
			return false;
		}

		if (playerRoster.size() < 2) {
			System.out.println("You only have one Pokémon, so a random ally will join you for this fight!");
			Pokemon yourPokemon = playerRoster.get(0);
			Pokemon rentalPokemon = PokemonFactory.createRandomPokemon();
			playerTeam.add(yourPokemon);
			playerTeam.add(rentalPokemon);
			System.out.println(
					"Your team is: " + yourPokemon.getName() + " and the rental " + rentalPokemon.getName() + "!\n");
			return true;
		}

		System.out.println("Your current Roster of Pokémons:\n");
		for (int i = 0; i < playerRoster.size(); ++i) {
			System.out.printf("[%d] %s%n%n", i + 1, playerRoster.get(i));
		}

		while (playerTeam.size() < 2) {
			System.out.print(
					"Select Pokémon #" + (playerTeam.size() + 1) + " for battle (1-" + playerRoster.size() + "): ");
			try {
				int choice = Integer.parseInt(scanner.nextLine()) - 1;
				if (choice >= 0 && choice < playerRoster.size()) {
					Pokemon selected = playerRoster.get(choice);
					if (playerTeam.contains(selected)) {
						System.out.println("You cannot choose the same Pokémon twice.");
					} else {
						playerTeam.add(selected);
						System.out.println("You selected " + selected.getName() + ".\n");
					}
				} else {
					System.out.println("Invalid choice. Please pick from the list.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input! Please enter a number.\n");
			}
		}
		return true;
	}

	private void chooseWildEnemyTeam() {
		System.out.println("Three wild Pokémon appeared!");

		List<Pokemon> wildOptions = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Pokemon wild = PokemonFactory.createRandomPokemon();
			wildOptions.add(wild);
			System.out.printf("[%d] %s%n%n", i + 1, wild);
		}

		int chosenIndex = -1;
		while (chosenIndex < 0 || chosenIndex >= 3) {
			System.out.print("Choose one Pokémon to fight (1-3): ");
			try {
				chosenIndex = Integer.parseInt(scanner.nextLine()) - 1;
				if (chosenIndex < 0 || chosenIndex >= 3) {
					System.out.println("Invalid choice. Please select 1, 2, or 3.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}

		Pokemon chosenPokemon = wildOptions.get(chosenIndex);
		Pokemon randomAlly = PokemonFactory.createRandomPokemon();

		enemyTeam.add(chosenPokemon);
		enemyTeam.add(randomAlly);

		System.out.println(
				"\nA wild " + chosenPokemon.getName() + " and " + randomAlly.getName() + " are challenging you!");
	}

	private List<Pokemon> setTurnOrder() {
		return Stream.concat(playerTeam.stream(), enemyTeam.stream())
				.sorted(Comparator.comparingInt(Pokemon::getSpeed).reversed())
				.collect(Collectors.toList());
	}

	private void performPlayerAttack(Pokemon attacker) {
		List<Pokemon> targets = enemyTeam.stream().filter(p -> !p.isFainted()).collect(Collectors.toList());
		if (targets.isEmpty())
			return;

		Pokemon target;
		if (targets.size() == 1) {
			target = targets.get(0);
			System.out.println(attacker.getName() + " only has one target left!");
		} else {
			System.out.println("Choose a target for " + attacker.getName() + ":");
			for (int i = 0; i < targets.size(); i++) {
				System.out.println(
						(i + 1) + ". " + targets.get(i).getName() + " [HP: " + targets.get(i).getCurrentHp() + "]");
			}

			int choice = -1;
			while (choice < 1 || choice > targets.size()) {
				try {
					choice = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a number.");
				}
			}
			target = targets.get(choice - 1);
		}
		attack(attacker, target);
	}

	private void performEnemyAttack(Pokemon attacker) {
		List<Pokemon> targets = playerTeam.stream().filter(p -> !p.isFainted()).collect(Collectors.toList());
		if (targets.isEmpty())
			return;
		Pokemon target = targets.get(random.nextInt(targets.size()));
		System.out.println(attacker.getName() + " has decided to attack " + target.getName() + "!");
		attack(attacker, target);
	}

	private void attack(Pokemon attacker, Pokemon defender) {
		double typeMultiplier = attacker.getTypeEffectiveness(defender);
		int baseDamage = attacker.getAttack() + random.nextInt(5) - 2;
		baseDamage = Math.max(1, baseDamage);

		int finalDamage = (int) Math.round(baseDamage * typeMultiplier);
		finalDamage = Math.max(1, finalDamage);

		defender.takeDamage(finalDamage);

		if (playerTeam.contains(attacker)) {
			totalDamageDealt += finalDamage;
		} else {
			totalDamageReceived += finalDamage;
		}

		System.out.println(attacker.getName() + " attacks " + defender.getName()
				+ " for " + finalDamage + " damage! (" + typeMultiplier + "x effectiveness)");

		if (defender.isFainted()) {
			System.out.println(defender.getName() + " fainted!");
		} else {
			System.out.println(
					defender.getName() + "'s HP is now " + defender.getCurrentHp() + "/" + defender.getMaxHp() + "\n");
		}
	}

	private void handlePlayerVictory() {
		System.out.println("\n*** VICTORY! ***");
		int score = calculateScore(true);
		int coinsWon = 10;
		currentPlayer.setHighestScore(score);
		currentPlayer.addCoins(coinsWon);
		System.out.println("You earned " + score + " points and " + coinsWon + " coins!");

		System.out.println("\nYou win! You may attempt to catch a Pokémon.");

		System.out.println("Choose a Pokémon to catch:");
		for (int i = 0; i < enemyTeam.size(); i++) {
			System.out.println((i + 1) + ". " + enemyTeam.get(i).getName());
		}

		Pokemon chosenPokemon = null;
		while (true) {
			System.out.print("Enter the number of the Pokémon to catch: ");
			String input = scanner.nextLine().trim();

			try {
				int choice = Integer.parseInt(input);
				if (choice >= 1 && choice <= enemyTeam.size()) {
					chosenPokemon = enemyTeam.get(choice - 1);
					break;
				} else {
					System.out.println("Invalid choice. Please select a valid number.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}

		while (true) {
			PokeBall pokeBall = PokeBallFactory.createRandomBall();

			double probability = PokeBall.calculateCatchProbability(chosenPokemon, pokeBall);

			System.out.printf("Press Enter to attempt to catch %s.\n", chosenPokemon.getName());

			// Recalculate hpFactor only for displaying, or create a helper method for full
			// debug info
			double hpFactor = 1.0 - ((double) chosenPokemon.getCurrentHp() / chosenPokemon.getMaxHp());
			System.out.printf("Chances to catch: %.2f%% (Ball: %s, Multiplier: %.2f, HP Factor: %.2f)\n",
					probability * 100,
					pokeBall.getName(),
					pokeBall.getCatchRate(),
					hpFactor);

			scanner.nextLine(); // Wait for player to press Enter

			System.out.printf("You got a %s! Attempting to catch %s...\n\n",
					pokeBall.getName(), chosenPokemon.getName());

			if (pokeBall.tryCatch(chosenPokemon)) {
				System.out.println("You caught " + chosenPokemon.getName() + "!");
				currentPlayer.addPokemon(chosenPokemon);
				break;
			} else {
				System.out.println("The Pokémon escaped!");

				int recatchCost = 20;
				System.out.println("You can spend " + recatchCost + " coins to try again.");
				System.out.print("Do you want to try again? (Y/N): ");
				String input = scanner.nextLine().trim().toLowerCase();

				if (!input.equals("y"))
					break;

				if (currentPlayer.checkBalance(recatchCost)) {
					currentPlayer.spendCoins(recatchCost);
					System.out.println("You spent " + recatchCost + " coins to try again...");
				} else {
					System.out.println("You don't have enough coins to try again.");
					break;
				}
			}
		}
	}

	private void handlePlayerDefeat() {
		System.out.println("\n--- DEFEAT ---");
		System.out.println("All your Pokémon have fainted...");
		int score = calculateScore(false);
		int coinsGained = 5;
		currentPlayer.setHighestScore(score);
		currentPlayer.addCoins(coinsGained);
		System.out.println("You earned " + score + " points and a consolation prize of " + coinsGained + " coins.");
		System.out.println("Better luck next time!");
	}

	private boolean isTeamFainted(List<Pokemon> team) {
		return team.stream().allMatch(Pokemon::isFainted);
	}

	private int calculateScore(boolean playerWon) {
		int baseScore = (totalDamageDealt * 2) + totalDamageReceived;
		return playerWon ? baseScore * 3 : baseScore;
	}

	private void reviveAllPokemons() {
		for (Pokemon p : currentPlayer.getInventory()) {
			p.revive();
		}
	}

	private void displayBattleStatus() {
		System.out.println("----------------------------------------");
		System.out.println("YOUR TEAM:");
		for (Pokemon p : playerTeam) {
			System.out.println("  " + p.getName() + " [HP: " + p.getCurrentHp() + "/" + p.getMaxHp() + "]");
		}
		System.out.println("ENEMY TEAM:");
		for (Pokemon p : enemyTeam) {
			System.out.println("  " + p.getName() + " [HP: " + p.getCurrentHp() + "/" + p.getMaxHp() + "]");
		}
		System.out.println("----------------------------------------");
	}
}
