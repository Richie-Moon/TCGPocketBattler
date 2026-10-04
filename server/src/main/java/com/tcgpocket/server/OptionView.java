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
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.IPlayableCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.engine.OpeningPlacement;
import com.tcgpocket.player.Decision;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * One decision option as the browser sees it: text to show, plus what it acts on.
 *
 * <p>Options are engine objects (actions, Pokemon, cards) and are never sent
 * as they are, and only to the player being asked. {@code card} and
 * {@code target} are instance ids, the same ones {@link BoardView} uses, so a
 * client can turn "dragged card 31 onto Pokemon 12" into this option's index.
 * The browser still answers with the index, never with the ids.
 *
 * @param kind   attack, endTurn, attach, retreat, evolve, play, ability, pokemon, card, setup, none or other
 * @param card   the card being played, evolved with or used (for an attack, the attacking Pokemon; for
 *               setup, the Active), or null
 * @param target the Pokemon it lands on or brings up, or null
 * @param bench  for setup, the card from hand for each Bench slot, null for an empty one; empty otherwise. A
 *               setup client builds a board from clicks and looks up the option with that Active and that Bench.
 * @param shown  for a play, the printed id of a card that does not stay on a Pokemon (an Item, Supporter or
 *               Stadium, not a Basic, Fossil or Tool), which the browser flashes before its effect; null otherwise
 * @param slot   for a Basic played to the Bench, the slot it goes into; null otherwise
 */
record OptionView(String label, String kind, Integer card, Integer target, List<Integer> bench, String shown,
                  Integer slot) {

    OptionView(String label, String kind, Integer card, Integer target) {
        this(label, kind, card, target, List.of(), null, null);
    }

    static OptionView of(Object option, Decision<?> decision) {
        return switch (option) {
            case Action attack -> new OptionView("Attack: " + attack.name(), "attack",
                    decision.context().source().map(OptionView::id).orElse(null), null);
            case EndTurnAction ignored -> new OptionView("End turn", "endTurn", null, null);
            case AttachEnergyAction attach -> new OptionView(
                    "Attach energy to " + label(attach.target(), decision), "attach",
                    null, id(attach.target(), decision));
            case RetreatAction retreat -> new OptionView(
                    "Retreat, bringing up " + label(retreat.replacement(), decision), "retreat",
                    null, id(retreat.replacement(), decision));
            case EvolveAction evolve -> new OptionView(
                    "Evolve " + label(evolve.onto(), decision) + " into " + evolve.evolution().definition().name(),
                    "evolve", id(evolve.evolution()), id(evolve.onto(), decision));
            case PlayCardAction play -> new OptionView(
                    "Play " + play.card().definition().name()
                            + play.onto().map(onto -> " on " + label(onto, decision)).orElse("")
                            + (play.benchSlot().isPresent() ? " to Bench slot " + (play.benchSlot().getAsInt() + 1) : ""),
                    "play", id(play.card()), play.onto().map(OptionView::id).orElse(null), List.of(),
                    shown(play.card().definition()),
                    play.benchSlot().isPresent() ? play.benchSlot().getAsInt() : null);
            case UseAbilityAction use -> new OptionView(
                    "Use " + use.ability().name() + " (" + use.source().definition().name() + ")",
                    "ability", id(use.source()), null);
            case OpeningPlacement opening -> new OptionView(
                    "Active " + opening.active().definition().name()
                            + (opening.benched().isEmpty() ? "" : ", Bench " + opening.benched().stream()
                                    .map(card -> card.definition().name()).collect(Collectors.joining(", "))),
                    "setup", id(opening.active()), null,
                    opening.bench().stream().map(slot -> slot.map(OptionView::id).orElse(null)).toList(), null, null);
            case PlainAction plain -> other(plain.description());
            case PlayedOnto played -> other(played.description());
            case WithPrecondition guarded -> of(guarded.action(), decision);
            case ChoiceAction choice -> other(choice.prompt());
            case PokemonInPlay pokemon -> new OptionView(label(pokemon, decision), "pokemon", null, id(pokemon));
            case CardInstance card -> new OptionView(card.definition().name(), "card", id(card), null);
            case Optional<?> maybe -> maybe.map(inner -> of(inner, decision))
                    .orElse(new OptionView("None", "none", null, null));
            // ponytail: a list (DistributeEnergy's placements) is only text; give it ids once a UI drags energy.
            case List<?> many -> other(many.stream()
                    .map(each -> of(each, decision).label())
                    .collect(Collectors.joining(", ")));
            default -> other(option.toString());
        };
    }

    private static String shown(ICard card) {
        return card instanceof IPlayableCard || card instanceof ToolCard ? null : card.id();
    }

    private static OptionView other(String label) {
        return new OptionView(label, "other", null, null);
    }

    private static Integer id(CardInstance card) {
        return card.instanceId();
    }

    private static Integer id(ITarget target, Decision<?> decision) {
        return target.resolve(decision.context()).map(OptionView::id).orElse(null);
    }

    private static String label(ITarget target, Decision<?> decision) {
        return target.resolve(decision.context()).map(pokemon -> label(pokemon, decision)).orElse("?");
    }

    private static String label(PokemonInPlay pokemon, Decision<?> decision) {
        String whose = pokemon.owner() == decision.chooser() ? "your " : "opponent's ";
        return pokemon.definition().name() + " (" + whose + pokemon.zone().name().toLowerCase()
                + ", " + pokemon.currentHp() + " HP)";
    }
}
