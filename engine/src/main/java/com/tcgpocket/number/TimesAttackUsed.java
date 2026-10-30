package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * How many times the controller's Pokemon have used a named attack this game —
 * Alcremie's Sweets Overload.
 */
public record TimesAttackUsed(String attackName) implements INumber {

    public TimesAttackUsed {
        Objects.requireNonNull(attackName, "attackName");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return context.controller().timesUsed(attackName);
    }
}
