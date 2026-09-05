package com.tcgpocket.status;

/** Confused: flip when attacking, and on tails the attack fails. */
public record ConfusionStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.SPECIAL;
    }
}
