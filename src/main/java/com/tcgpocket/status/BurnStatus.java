package com.tcgpocket.status;

/** Burned: 20 damage between turns, then a flip to shake it off. */
public record BurnStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.DAMAGE_OVER_TIME;
    }
}
