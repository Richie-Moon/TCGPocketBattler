package com.tcgpocket.status;

/** Paralyzed: cannot attack or retreat; clears at the end of its owner's turn. */
public record ParalysisStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.SPECIAL;
    }
}
