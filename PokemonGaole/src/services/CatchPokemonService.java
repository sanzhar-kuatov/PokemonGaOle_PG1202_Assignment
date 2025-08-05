package services;

import domain.Player;
import domain.PokeBall;
import domain.PokeBallFactory;
import domain.Pokemon;

public class CatchPokemonService {
    private final Pokemon pokemon;
    private final Player player;
    private final PokeBall pokeBall;
    private Boolean caught = null;

    public CatchPokemonService(Pokemon pokemon, Player player) {
        this.pokemon = pokemon;
        this.player = player;
        this.pokeBall = PokeBallFactory.createRandomBall();
    }

    public boolean tryCatch() {
        if (caught != null) {
            return caught;
        }

        caught = Math.random() <= pokeBall.getCatchRate();
        if (caught) {
            player.addPokemon(pokemon);
        }

        return caught;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Player getPlayer() {
        return player;
    }

    public PokeBall getPokeBall() {
        return pokeBall;
    }

    public Boolean isCaught() {
        return caught;
    }

    @Override
    public String toString() {
        return String.format(
                "You have %.0f%% chance to catch the Pokémon %s using a %s.",
                pokeBall.getCatchRate() * 100,
                pokemon.getName(),
                pokeBall.getClass().getSimpleName()
        );
    }
}
