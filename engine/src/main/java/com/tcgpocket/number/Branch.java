package com.tcgpocket.number;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.List;
import java.util.Objects;

/**
 * The first matching case, or {@code otherwise} if none match.
 *
 * <p>For the genuinely tabular card text — an attack doing 20 / 50 / 90
 * depending on how many heads, where the steps are irregular. Regular steps
 * need no branch: "30 damage for each heads" is already
 * {@code new Product(new NumberHeads(), new Literal(30))}.
 *
 * <p>This lives at the {@code INumber} level rather than inside one damage
 * effect, so the same node serves heal amounts, draw counts and durations.
 */
public record Branch(List<Case> cases, INumber otherwise) implements INumber {

    /**
     * One arm of a {@link Branch}.
     *
     * @param when tested against the whole context, so it can ask about
     *             anything on the board
     */
    public record Case(ICondition<ResolutionContext> when, INumber then) {

        public Case {
            Objects.requireNonNull(when, "when");
            Objects.requireNonNull(then, "then");
        }
    }

    public Branch {
        cases = List.copyOf(cases);
        Objects.requireNonNull(otherwise, "otherwise");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        for (Case candidate : cases) {
            if (candidate.when().evaluate(context)) {
                return candidate.then().evaluate(context);
            }
        }
        return otherwise.evaluate(context);
    }
}
