package com.tcgpocket.server;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tcgpocket.energy.Type;
import com.tcgpocket.state.Zone;

import java.util.List;
import java.util.Set;

/**
 * One edit the sandbox's owner makes, as JSON tagged by {@code command}. The one place the browser describes
 * something itself: {@code /play} only ever takes an index into a list the engine made legal.
 *
 * <p>{@code opponent} picks the side, {@code card} (a String) is a printed id from the pool, and an int
 * {@code card} or {@code pokemon} is an instance id. {@code position} is a deck index (0 the top, null the bottom)
 * or a Bench slot (null the first empty one).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "command")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SandboxCommand.AddCard.class, name = "addCard"),
        @JsonSubTypes.Type(value = SandboxCommand.Move.class, name = "move"),
        @JsonSubTypes.Type(value = SandboxCommand.Remove.class, name = "remove"),
        @JsonSubTypes.Type(value = SandboxCommand.Shuffle.class, name = "shuffle"),
        @JsonSubTypes.Type(value = SandboxCommand.SetDamage.class, name = "setDamage"),
        @JsonSubTypes.Type(value = SandboxCommand.SetEnergy.class, name = "setEnergy"),
        @JsonSubTypes.Type(value = SandboxCommand.AddStatus.class, name = "addStatus"),
        @JsonSubTypes.Type(value = SandboxCommand.RemoveStatus.class, name = "removeStatus"),
        @JsonSubTypes.Type(value = SandboxCommand.AttachTool.class, name = "attachTool"),
        @JsonSubTypes.Type(value = SandboxCommand.RemoveTool.class, name = "removeTool"),
        @JsonSubTypes.Type(value = SandboxCommand.Evolve.class, name = "evolve"),
        @JsonSubTypes.Type(value = SandboxCommand.SetPlayedThisTurn.class, name = "setPlayedThisTurn"),
        @JsonSubTypes.Type(value = SandboxCommand.SetPoints.class, name = "setPoints"),
        @JsonSubTypes.Type(value = SandboxCommand.SetEnergyZone.class, name = "setEnergyZone"),
        @JsonSubTypes.Type(value = SandboxCommand.ResetTurn.class, name = "resetTurn"),
        @JsonSubTypes.Type(value = SandboxCommand.SetTurn.class, name = "setTurn"),
        @JsonSubTypes.Type(value = SandboxCommand.SetStadium.class, name = "setStadium"),
        @JsonSubTypes.Type(value = SandboxCommand.QueueCoins.class, name = "queueCoins"),
        @JsonSubTypes.Type(value = SandboxCommand.ClearCoins.class, name = "clearCoins"),
})
sealed interface SandboxCommand {

    record AddCard(boolean opponent, String card, Zone zone, Integer position) implements SandboxCommand {
    }

    /** Stays on its owner's side. A Pokemon leaving play takes its evolutions along, discards its Tool and loses its Energy. */
    record Move(int card, Zone zone, Integer position) implements SandboxCommand {
    }

    record Remove(int card) implements SandboxCommand {
    }

    record Shuffle(boolean opponent) implements SandboxCommand {
    }

    record SetDamage(int pokemon, int damage) implements SandboxCommand {
    }

    record SetEnergy(int pokemon, Type type, int count) implements SandboxCommand {
    }

    /** {@code status} is as {@link BoardView.PokemonView} names it: "Poison", "Burn", "Sleep", "Paralysis" or "Confusion". */
    record AddStatus(int pokemon, String status) implements SandboxCommand {
    }

    record RemoveStatus(int pokemon, String status) implements SandboxCommand {
    }

    record AttachTool(int pokemon, String card) implements SandboxCommand {
    }

    record RemoveTool(int pokemon) implements SandboxCommand {
    }

    /** Puts {@code card} on top, whatever it evolves from. Fires no trigger: only playing an evolution from hand does. */
    record Evolve(int pokemon, String card) implements SandboxCommand {
    }

    /** Played this turn it can't evolve yet; otherwise it can, even on its owner's first turn, which this ends for good. */
    record SetPlayedThisTurn(int pokemon, boolean played) implements SandboxCommand {
    }

    record SetPoints(boolean opponent, int points) implements SandboxCommand {
    }

    /** {@code current} and {@code next} may be null; a {@code current} can be attached even after an attach this turn. {@code types} are what the zone generates from next turn on. */
    record SetEnergyZone(boolean opponent, Type current, Type next, Set<Type> types) implements SandboxCommand {
    }

    /** Clears this turn's flags (Energy attached, Supporter played, retreated, Abilities used) and every lock. */
    record ResetTurn(boolean opponent) implements SandboxCommand {
    }

    record SetTurn(int turn) implements SandboxCommand {
    }

    /** A null {@code card} clears it. */
    record SetStadium(String card) implements SandboxCommand {
    }

    /** The next coins flipped land these ways, in order, true for heads; after them they are random again. */
    record QueueCoins(List<Boolean> coins) implements SandboxCommand {
    }

    record ClearCoins() implements SandboxCommand {
    }
}
