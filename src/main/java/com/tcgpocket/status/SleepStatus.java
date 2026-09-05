package com.tcgpocket.status;

/** Asleep: cannot attack or retreat; a flip between turns to wake up. */
public record SleepStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.SPECIAL;
    }
}
