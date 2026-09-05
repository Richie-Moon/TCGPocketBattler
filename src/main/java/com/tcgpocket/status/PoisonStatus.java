package com.tcgpocket.status;

/** Poisoned: 10 damage between turns, and it does not wear off on its own. */
public record PoisonStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.DAMAGE_OVER_TIME;
    }
}
