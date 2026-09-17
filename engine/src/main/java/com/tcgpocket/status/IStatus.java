package com.tcgpocket.status;

import com.tcgpocket.trigger.ITrigger;

import java.util.List;

/**
 * A special condition afflicting a Pokemon.
 *
 * <p>Every implementation is a component-less record, so two instances of the
 * same status compare equal. That is what lets a Pokemon hold them in a plain
 * {@code Set} and lets {@code IsPoisoned} ask a simple membership question.
 *
 * <p>A status has no behaviour of its own beyond {@link #triggers()}: poison
 * damage, burn's recovery flip and confusion's missed attack are all ordinary
 * triggers on ordinary events, found by the ordinary dispatcher. Nothing in the
 * engine knows what "Poisoned" means, and that is the point — a card inventing
 * a new condition would need no engine change at all.
 */
public sealed interface IStatus
        permits PoisonStatus, BurnStatus, SleepStatus, ParalysisStatus, ConfusionStatus {

    StatusCategory category();

    /**
     * What this condition does, and when.
     *
     * <p>Fired with the afflicted Pokemon as the source, so every one of these
     * is written in terms of {@code Self}.
     */
    List<ITrigger> triggers();
}
