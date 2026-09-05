package com.tcgpocket.card;

import com.tcgpocket.action.IAction;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A Pokemon as printed.
 *
 * @param actions        its attacks
 * @param stage          0 for a Basic, 1 and 2 for the evolutions
 * @param evolvesFrom    species this evolves from; empty for a Basic
 * @param weaknessDamage extra damage when weakness applies. Always 20 in
 *                       Pocket, but kept as an {@link INumber} so the model
 *                       does not bake in one format's constant.
 */
public record PokemonCard(
        String id,
        String name,
        String description,
        Set<CardTag> tags,
        List<IAction> actions,
        int maxHp,
        int stage,
        Type type,
        Optional<String> evolvesFrom,
        int retreatCost,
        Optional<Type> weakness,
        Optional<INumber> weaknessDamage,
        Optional<IAbility> ability) implements IPlayableCard {

    public PokemonCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(ability, "ability");
        tags = Set.copyOf(tags);
        actions = List.copyOf(actions);

        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp must be positive, was " + maxHp);
        }
        if (stage < 0) {
            throw new IllegalArgumentException("stage must not be negative, was " + stage);
        }
        if (retreatCost < 0) {
            throw new IllegalArgumentException("retreatCost must not be negative, was " + retreatCost);
        }
        if (stage == 0 && evolvesFrom.isPresent()) {
            throw new IllegalArgumentException("a Basic cannot evolve from anything: " + name);
        }
        if (stage > 0 && evolvesFrom.isEmpty()) {
            throw new IllegalArgumentException("a Stage " + stage + " must evolve from something: " + name);
        }
    }

    public boolean isBasic() {
        return stage == 0;
    }

    /** Minimal Basic, for tests and for cards with nothing unusual about them. */
    public static PokemonCard basic(String id, String name, int maxHp, Type type, int retreatCost) {
        return new PokemonCard(
                id, name, "", Set.of(), List.of(), maxHp, 0, type,
                Optional.empty(), retreatCost, Optional.empty(), Optional.empty(), Optional.empty());
    }

    /** The same, with attacks. */
    public static PokemonCard basic(
            String id, String name, int maxHp, Type type, int retreatCost, List<IAction> actions) {
        return new PokemonCard(
                id, name, "", Set.of(), actions, maxHp, 0, type,
                Optional.empty(), retreatCost, Optional.empty(), Optional.empty(), Optional.empty());
    }

    /** A Stage 1 or 2, which may only be played onto the species it evolves from. */
    public static PokemonCard evolution(
            String id, String name, int stage, String evolvesFrom,
            int maxHp, Type type, int retreatCost, List<IAction> actions) {
        return new PokemonCard(
                id, name, "", Set.of(), actions, maxHp, stage, type,
                Optional.of(evolvesFrom), retreatCost, Optional.empty(), Optional.empty(),
                Optional.empty());
    }

    public PokemonCard withAbility(IAbility newAbility) {
        return new PokemonCard(id, name, description, tags, actions, maxHp, stage, type,
                evolvesFrom, retreatCost, weakness, weaknessDamage, Optional.of(newAbility));
    }

    public PokemonCard withTags(CardTag... newTags) {
        return new PokemonCard(id, name, description, Set.of(newTags), actions, maxHp, stage, type,
                evolvesFrom, retreatCost, weakness, weaknessDamage, ability);
    }

    public PokemonCard withWeakness(Type weakTo) {
        return new PokemonCard(id, name, description, tags, actions, maxHp, stage, type,
                evolvesFrom, retreatCost, Optional.of(weakTo), weaknessDamage, ability);
    }
}
