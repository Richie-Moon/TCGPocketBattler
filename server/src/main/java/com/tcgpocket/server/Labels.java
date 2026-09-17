package com.tcgpocket.server;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.AttachEnergyAction;
import com.tcgpocket.action.ChoiceAction;
import com.tcgpocket.action.EndTurnAction;
import com.tcgpocket.action.EvolveAction;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.action.PlayedOnto;
import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.player.Decision;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Turns decision options into text a person can pick from.
 *
 * <p>Options are engine objects (actions, Pokemon, cards) and are never sent
 * as they are. Only the label goes out, and only to the player being asked.
 */
final class Labels {

    private Labels() {
    }

    static String of(Object option, Decision<?> decision) {
        return switch (option) {
            case Action attack -> "Attack: " + attack.name();
            case EndTurnAction ignored -> "End turn";
            case AttachEnergyAction attach -> "Attach energy to " + target(attach.target(), decision);
            case RetreatAction retreat -> "Retreat, bringing up " + target(retreat.replacement(), decision);
            case EvolveAction evolve ->
                    "Evolve " + target(evolve.onto(), decision) + " into " + evolve.evolution().definition().name();
            case PlayCardAction play -> "Play " + play.card().definition().name()
                    + play.onto().map(onto -> " on " + pokemon(onto, decision)).orElse("");
            case UseAbilityAction use ->
                    "Use " + use.ability().name() + " (" + use.source().definition().name() + ")";
            case PlainAction plain -> plain.description();
            case PlayedOnto played -> played.description();
            case WithPrecondition guarded -> of(guarded.action(), decision);
            case ChoiceAction choice -> choice.prompt();
            case PokemonInPlay pokemon -> pokemon(pokemon, decision);
            case CardInstance card -> card.definition().name();
            case Optional<?> maybe -> maybe.map(inner -> of(inner, decision)).orElse("None");
            case List<?> many -> many.stream().map(each -> of(each, decision)).collect(Collectors.joining(", "));
            default -> option.toString();
        };
    }

    private static String target(ITarget target, Decision<?> decision) {
        return target.resolve(decision.context()).map(pokemon -> pokemon(pokemon, decision)).orElse("?");
    }

    private static String pokemon(PokemonInPlay pokemon, Decision<?> decision) {
        String whose = pokemon.owner() == decision.chooser() ? "your " : "opponent's ";
        return pokemon.definition().name() + " (" + whose + pokemon.zone().name().toLowerCase()
                + ", " + pokemon.currentHp() + " HP)";
    }
}
