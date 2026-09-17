package com.tcgpocket.engine;

import com.tcgpocket.action.AttachEnergyAction;
import com.tcgpocket.action.EndTurnAction;
import com.tcgpocket.action.EvolveAction;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.resolve.ResolutionScope;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBenchSpecific;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;
import com.tcgpocket.trigger.TurnStart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Runs a game: setup, turns, knockouts and the win check.
 *
 * <p>Everything a card does happens elsewhere — actions and effects mutate the
 * board and announce their own events. What lives here is only what no card
 * owns: the order of a turn, the three events nothing else can announce
 * ({@link TurnStart}, {@link TurnEnd}, {@link Knockout}), and deciding when
 * the game is over.
 *
 * <p>There is no {@code Phase} enum. No card reads the phase, and the steps of
 * {@link #playTurn} already are the phases.
 */
public final class TurnEngine {

    /** After this many turns the game is a tie. */
    public static final int MAX_TURNS = 30;

    /** Cards in an opening hand. */
    static final int OPENING_HAND = 5;

    /** Points at which a side holds the points win condition. */
    static final int POINTS_TO_WIN = 3;

    private final Battle battle;
    private boolean over;
    private Side winner;

    public TurnEngine(Battle battle) {
        this.battle = Objects.requireNonNull(battle, "battle");
    }

    public Battle battle() {
        return battle;
    }

    public boolean isOver() {
        return over;
    }

    /** The winner once the game is over; empty while it runs, and for a tie. */
    public Optional<Side> winner() {
        return Optional.ofNullable(winner);
    }

    /** Plays a whole game from empty boards. Returns the winner, or empty for a tie. */
    public Optional<Side> playGame() {
        setup();
        while (!over && battle.turn() <= MAX_TURNS) {
            playTurn();
        }
        over = true;
        return winner();
    }

    /* ------------------------------------------------------------------ */
    /* Setup                                                               */
    /* ------------------------------------------------------------------ */

    /**
     * Deals opening hands and has each player place their Pokemon.
     *
     * <p>The caller has already filled each deck, registered its energy types,
     * and passed the sides to {@code Battle} in turn order — so the coin flip
     * for who goes first is the caller's.
     */
    public void setup() {
        for (Side side : List.of(battle.attacker(), battle.defender())) {
            dealOpeningHand(side);
            placeOpeningPokemon(side);
        }
    }

    /** Pocket guarantees a Basic in the opening hand, so there is no mulligan. */
    private void dealOpeningHand(Side side) {
        if (side.deck().stream().noneMatch(TurnEngine::isBasic)) {
            throw new IllegalStateException(side.name() + "'s deck holds no Basic Pokemon");
        }

        side.shuffleDeck(battle.rng());
        for (int i = 0; i < OPENING_HAND; i++) {
            side.drawCard();
        }

        if (side.hand().stream().noneMatch(TurnEngine::isBasic)) {
            // ponytail: swaps in the first Basic in the deck, not a random one; biased, fine until someone measures openings.
            CardInstance basic = side.deck().stream().filter(TurnEngine::isBasic).findFirst().orElseThrow();
            CardInstance returned = side.hand().getFirst();
            side.removeFromDeck(basic);
            side.removeFromHand(returned);
            side.addToHand(basic);
            side.addToDeck(returned);
            side.shuffleDeck(battle.rng());
        }
    }

    private void placeOpeningPokemon(Side side) {
        CardInstance active = choose(side, "Choose your Active Pokemon", basicsInHand(side));
        side.removeFromHand(active);
        side.setActive(enterPlay(active, side, Zone.ACTIVE));

        while (!side.benchIsFull() && !basicsInHand(side).isEmpty()) {
            List<Optional<CardInstance>> options = new ArrayList<>();
            options.add(Optional.empty());
            basicsInHand(side).forEach(card -> options.add(Optional.of(card)));

            Optional<CardInstance> picked = side.player().choose(
                    new Decision<>("Choose a Benched Pokemon, or none to finish", options, side, contextFor(side)));
            if (picked.isEmpty()) {
                return;
            }
            side.removeFromHand(picked.get());
            side.addToBench(enterPlay(picked.get(), side, Zone.BENCH));
        }
    }

    /** Same identity as the card in hand, as {@code PlayCardAction} does. */
    private static PokemonInPlay enterPlay(CardInstance card, Side side, Zone zone) {
        return new PokemonInPlay(card.instanceId(), (PokemonCard) card.definition(), side, zone, 0);
    }

    private static List<CardInstance> basicsInHand(Side side) {
        return side.hand().stream().filter(TurnEngine::isBasic).toList();
    }

    private static boolean isBasic(CardInstance card) {
        return card.definition() instanceof PokemonCard pokemon && pokemon.isBasic();
    }

    /* ------------------------------------------------------------------ */
    /* A turn                                                              */
    /* ------------------------------------------------------------------ */

    /** Plays one turn for {@code battle.attacker()}, ending in {@code switchSides} unless the game ends first. */
    public void playTurn() {
        Side side = battle.attacker();

        TriggerDispatcher.dispatch(battle, new TurnStart(side));
        side.drawCard();
        if (battle.turn() > 1) {
            side.generateEnergy(battle.rng());
        }

        boolean turnOver = false;
        while (!turnOver) {
            IAction chosen = choose(side, "Your move", legalActions());

            Optional<PokemonInPlay> attacker = side.active();
            boolean attacked = attacker
                    .map(pokemon -> pokemon.definition().actions().contains(chosen))
                    .orElse(false);

            chosen.execute(contextFor(side));
            checkKnockouts(attacked ? attacker : Optional.empty());
            if (over) {
                return;
            }
            // An attack ends the turn even when Confusion cancelled it.
            turnOver = attacked || chosen instanceof EndTurnAction;
        }

        TriggerDispatcher.dispatch(battle, new TurnEnd(side));
        checkKnockouts(Optional.empty());
        if (over) {
            return;
        }

        int nextTurn = battle.turn() + 1;
        for (Side each : List.of(battle.attacker(), battle.defender())) {
            each.inPlay().forEach(pokemon -> pokemon.expireModifiers(nextTurn));
            each.resetTurnFlags();
        }
        battle.switchSides();
    }

    /**
     * Every move the side whose turn it is may make right now.
     *
     * <p>Only ever for {@code battle.attacker()}: every {@code ITarget} used
     * here is turn-relative, so there is deliberately no side parameter. Each
     * candidate answers its own {@code isLegal}; the one rule added here is
     * that nobody evolves on their first turn.
     */
    public List<IAction> legalActions() {
        Side side = battle.attacker();
        ResolutionContext context = contextFor(side);

        List<ITarget> ownPokemon = new ArrayList<>();
        side.active().ifPresent(active -> ownPokemon.add(new AttackerActive()));
        for (int i = 0; i < side.bench().size(); i++) {
            ownPokemon.add(new AttackerBenchSpecific(i));
        }

        List<IAction> candidates = new ArrayList<>();
        side.active().ifPresent(active -> candidates.addAll(active.definition().actions()));
        ownPokemon.forEach(target -> candidates.add(new AttachEnergyAction(target)));
        for (int i = 0; i < side.bench().size(); i++) {
            candidates.add(new RetreatAction(new AttackerBenchSpecific(i)));
        }
        if (battle.turn() > 2) {
            for (CardInstance card : side.hand()) {
                ownPokemon.forEach(target -> candidates.add(new EvolveAction(card, target)));
            }
        }
        for (PokemonInPlay pokemon : side.inPlay()) {
            pokemon.definition().ability()
                    .ifPresent(ability -> candidates.add(new UseAbilityAction(pokemon, ability)));
        }

        List<IAction> legal = new ArrayList<>(candidates.stream().filter(action -> action.isLegal(context)).toList());
        for (CardInstance card : side.hand()) {
            legal.addAll(PlayCardAction.all(card, context));
        }
        legal.add(new EndTurnAction());
        return legal;
    }

    /* ------------------------------------------------------------------ */
    /* Knockouts and winning                                               */
    /* ------------------------------------------------------------------ */

    /**
     * Scores, announces and discards every knocked-out Pokemon, checks for a
     * winner, and otherwise has each side promote a new Active.
     *
     * <p>{@link Knockout} is dispatched while the Pokemon is still in play, so
     * its own Tool and ability can react to it. Repeats until nothing is left
     * knocked out, since a knockout trigger may deal damage of its own.
     *
     * @param by the attacking Pokemon, when an attack caused this
     */
    public void checkKnockouts(Optional<PokemonInPlay> by) {
        List<PokemonInPlay> knockedOut = knockedOut();
        if (knockedOut.isEmpty()) {
            return;
        }

        while (!knockedOut.isEmpty()) {
            for (PokemonInPlay pokemon : knockedOut) {
                battle.opponentOf(pokemon.owner()).awardPoints(pointsFor(pokemon.definition()));
                TriggerDispatcher.dispatch(battle, new Knockout(pokemon, by));
                leavePlay(pokemon);
            }
            knockedOut = knockedOut();
        }

        decideWinner();
        if (over) {
            return;
        }

        for (Side side : List.of(battle.defender(), battle.attacker())) {
            if (side.active().isEmpty() && !side.bench().isEmpty()) {
                PokemonInPlay replacement = choose(side, "Choose a new Active Pokemon", side.bench());
                side.removeFromBench(replacement);
                side.setActive(replacement);
            }
        }
    }

    private List<PokemonInPlay> knockedOut() {
        List<PokemonInPlay> found = new ArrayList<>();
        for (Side side : List.of(battle.attacker(), battle.defender())) {
            side.inPlay().stream().filter(PokemonInPlay::isKnockedOut).forEach(found::add);
        }
        return found;
    }

    static int pointsFor(PokemonCard card) {
        if (card.tags().contains(CardTag.MEGA_EX)) {
            return 3;
        }
        return card.tags().contains(CardTag.EX) ? 2 : 1;
    }

    /** Attached energy is not a card in Pocket, so it simply goes. */
    private static void leavePlay(PokemonInPlay pokemon) {
        Side owner = pokemon.owner();
        if (owner.active().filter(pokemon::equals).isPresent()) {
            owner.setActive(null);
        } else {
            owner.removeFromBench(pokemon);
        }
        pokemon.cards().forEach(owner::addToDiscard);
        pokemon.removeTool().ifPresent(owner::addToDiscard);
    }

    /**
     * Ends the game once either side holds a win condition.
     *
     * <p>There are two, counted once each: at least {@value #POINTS_TO_WIN}
     * points, and being the only side with Pokemon left in play. The side with
     * more wins; an equal count is a tie. So a double knockout that brings you
     * to three points but leaves you nothing to promote, against an opponent
     * who still has a bench, is a tie.
     */
    private void decideWinner() {
        Side first = battle.attacker();
        Side second = battle.defender();
        int firstHolds = winConditions(first, second);
        int secondHolds = winConditions(second, first);

        if (firstHolds == 0 && secondHolds == 0) {
            return;
        }
        over = true;
        if (firstHolds != secondHolds) {
            winner = firstHolds > secondHolds ? first : second;
        }
    }

    private static int winConditions(Side side, Side opponent) {
        int held = side.points() >= POINTS_TO_WIN ? 1 : 0;
        if (side.hasPokemonInPlay() && !opponent.hasPokemonInPlay()) {
            held++;
        }
        return held;
    }

    /* ------------------------------------------------------------------ */
    /* Plumbing                                                            */
    /* ------------------------------------------------------------------ */

    /** Asks only when there is a choice, as {@code ChosenFrom} does. */
    private <T> T choose(Side side, String prompt, List<T> options) {
        if (options.size() == 1) {
            return options.getFirst();
        }
        return side.player().choose(new Decision<>(prompt, options, side, contextFor(side)));
    }

    /** A side's own turn context: its Active is the source, so its attacks resolve against it. */
    private ResolutionContext contextFor(Side side) {
        return new ResolutionContext(
                battle, side, side.active(), Optional.empty(), Optional.empty(), new ResolutionScope());
    }
}
