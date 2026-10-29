package com.tcgpocket.status;

import com.tcgpocket.trigger.ITrigger;

import java.util.List;

/**
 * A special condition afflicting a Pokemon.
 *
 * <p>A status is identified by its class: a Pokemon holds at most one of each,
 * and {@code IsPoisoned} or {@code RemoveStatus} ask about the kind, not the
 * details. Every implementation is a component-less record except
 * {@link PoisonStatus}, whose damage varies.
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
