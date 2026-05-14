# PokemonGaOle Terminal Game

A Java-based terminal game developed as a group project for the **Object-Oriented Programming Fundamentals** course at university.

## Overview

PokemonGaOle is a simple turn-based Pokémon battle game that runs in the terminal. The game allows users to create a new account or log in using an existing username. User data is stored locally in a `database.json` file, which is automatically created when the program starts.

New users are given a random set of Pokémon. Players can then battle against randomly generated enemy Pokémon in a 2v2 battle system. Each Pokémon has different health points and maximum damage values, making every battle slightly different.

## Purpose

The main purpose of this project was to apply object-oriented programming concepts in a practical game-based system.

This project helped us practice:

- Class and object design
- Encapsulation
- Inheritance / polymorphism, if used
- File handling
- JSON-based data storage
- Turn-based game logic
- Randomized gameplay mechanics
- User account management

## How the Game Works

1. When the program starts, it creates a `database.json` file if it does not already exist.
2. The player can either:
   - Create a new user account
   - Log in with an existing username
3. New users receive a random set of Pokémon.
4. The player can battle against randomly selected enemy Pokémon.
5. Battles are played in a 2v2 Pokémon format.
6. Each Pokémon has:
   - HP
   - Maximum damage
   - Random attack damage
7. The player and enemy Pokémon take turns attacking each other.
8. The battle continues until all Pokémon on one side are defeated.
9. If the player is losing, they can use in-game money to get another chance.
10. If the player wins, they receive a chance to capture one of the enemy Pokémon.
11. The chance of capturing the desired Pokémon depends on probability.

## Key Features

- Terminal-based gameplay
- User account creation and login
- Local JSON database storage
- Random Pokémon assignment for new users
- Turn-based 2v2 battle system
- Randomized attack damage
- Different HP and damage values for each Pokémon
- In-game money system
- Second-chance mechanic during battle
- Probability-based Pokémon capture system

## Tech Stack

- Java
- Object-Oriented Programming
- JSON file handling
- Terminal / Command Line Interface
- Random number generation
- Local file-based database

## Project Type

University group project for **OOP Fundamentals**.

## Example Gameplay Flow

```text
Start Program
↓
database.json is created or loaded
↓
User enters username
↓
New user receives random Pokémon
↓
User starts a battle
↓
Player Pokémon vs Enemy Pokémon
↓
Turn-based attacks continue
↓
Winner is decided
↓
If the player wins, they may capture an enemy Pokémon
```
## About the Project

This project was developed to demonstrate basic object-oriented programming principles through a simple game system. The game combines user management, local data storage, randomized battles, and probability-based rewards in a terminal environment.
