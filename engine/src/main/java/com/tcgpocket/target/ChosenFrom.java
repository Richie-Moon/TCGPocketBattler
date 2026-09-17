package com.tcgpocket.target;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A Pokemon picked by a player out of a group — "1 of your opponent's Benched
 * Basic Pokemon", "1 of your Pokemon".
 *
 * <p>The chooser is a {@link ISideTarget} rather than always the controller,
 * because both readings occur and the difference is the card. Sabrina switches
 * the opponent's Active and lets the <em>opponent</em> pick the replacement;
 * Fragrance Trap drags up a Benched Basic and lets <em>you</em> pick which.
 *
 * <h2>Only inside an effect</h2>
 *
 * <p>Resolving this <b>asks a player a question</b>, which makes it the one
 * target that is not free of side effects. It must never appear anywhere
 * {@code IAction.isLegal} resolves it — legal-move generation calls that on
 * every candidate before anything is chosen, and would prompt once per
 * candidate. Inside an {@code IEffect}, which only ever runs during execute,
 * it is safe.
 *
 * <p>A group of one is not worth asking about, so it resolves straight to that
 * Pokemon. An empty group resolves to empty, which the surrounding effect
 * treats as the failure it is.
 *
 * @param matching narrows the group before offering it; empty offers all of it
 */
public record ChosenFrom(
        IMultiTarget from,
        ISideTarget chooser,
        Optional<ICondition<? super PokemonInPlay>> matching,
        String prompt) implements ITarget {

    public ChosenFrom {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(chooser, "chooser");
        Objects.requireNonNull(matching, "matching");
        Objects.requireNonNull(prompt, "prompt");
    }

    /** Narrowed by a condition — "1 of your opponent's Benched <b>Basic</b> Pokemon". */
    public ChosenFrom(
            IMultiTarget from,
            ISideTarget chooser,
            ICondition<? super PokemonInPlay> matching,
            String prompt) {
        this(from, chooser, Optional.of(matching), prompt);
    }

    /** Any member of the group. */
    public ChosenFrom(IMultiTarget from, ISideTarget chooser, String prompt) {
        this(from, chooser, Optional.empty(), prompt);
    }

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        List<PokemonInPlay> candidates = from.resolve(context).stream()
                .filter(pokemon -> matching
                        .map(condition -> condition.evaluate(pokemon))
                        .orElse(true))
                .toList();

        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        if (candidates.size() == 1) {
            return Optional.of(candidates.get(0));
        }

        Side deciding = chooser.resolve(context);
        return Optional.of(deciding.player()
                .choose(new Decision<>(prompt, candidates, deciding, context)));
    }
}
