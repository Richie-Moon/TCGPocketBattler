package com.tcgpocket.status;

/**
 * A special condition afflicting a Pokemon.
 *
 * <p>Every implementation is a component-less record, so two instances of the
 * same status compare equal. That is what lets a Pokemon hold them in a plain
 * {@code Set} and lets {@code IsPoisoned} ask a simple membership question.
 */
public sealed interface IStatus
        permits PoisonStatus, BurnStatus, SleepStatus, ParalysisStatus, ConfusionStatus {

    // TODO: List<ITrigger> triggers(), onApplied(PokemonInPlay, Battle) and
    //       onRemoved(...) — await the ITrigger hierarchy. Each status is meant
    //       to be implemented purely as triggers, with no engine special-casing.

    StatusCategory category();
}
