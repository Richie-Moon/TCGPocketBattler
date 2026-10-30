package com.tcgpocket.pool.A3b;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.Action;
import com.tcgpocket.action.EvolveAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.player.Decision;
import com.tcgpocket.player.IPlayer;
import com.tcgpocket.player.RandomPlayer;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.SeededRandom;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** One assertion per card that needed a new node, or whose tree is not obvious. */
class EeveeGroveTest {

    private static final ICard ERIKA = CardPool.get("A1-219");

    private final TestBoard board = new TestBoard();

    private static Action attack(PokemonInPlay pokemon, int index) {
        return (Action) pokemon.definition().actions().get(index);
    }

    /** Turns forward one player's turn pair, so "last turn" is the turn just left. */
    private void nextOwnTurn() {
        board.battle.switchSides();
        board.battle.switchSides();
    }

    @Test
    @DisplayName("the whole set is in the pool, with the 2-star cards Ultra Rare")
    void setIsInThePool() {
        assertEquals(107, EeveeGrove.CARDS.size());
        assertEquals("Penny", CardPool.get("A3b-086").name());
        assertEquals(CardRarity.ULTRA_RARE, ((PokemonCard) CardPool.get("A3b-079")).rarity());
        assertEquals(CardRarity.ILLUSTRATION_RARE, ((PokemonCard) CardPool.get("A3b-070")).rarity());
    }

    @Test
    @DisplayName("Trainers are Uncommon unless a rarer printing says otherwise")
    void trainerRarities() {
        assertEquals(CardRarity.UNCOMMON, CardPool.get("A3b-066").rarity());
        assertEquals(CardRarity.UNCOMMON, CardPool.get("A3b-067").rarity());
        assertEquals(CardRarity.ULTRA_RARE, CardPool.get("A3b-085").rarity());
        assertEquals(CardRarity.HYPER_RARE, CardPool.get("A3b-107").rarity());
        assertEquals(CardRarity.HYPER_RARE, CardPool.get("A2b-111").rarity());
    }

    @Test
    @DisplayName("Sweets Relay hits harder only if one of yours used it on your last turn")
    void sweetsRelayReadsYourLastTurn() {
        PokemonInPlay appletun = board.active(board.you, Grass.APPLETUN);
        PokemonInPlay target = board.active(board.them, Colorless.SNORLAX_EX);
        appletun.attachEnergy(Type.GRASS, 1);

        attack(appletun, 0).execute(board.contextFor(appletun));
        assertEquals(40, target.damage());

        nextOwnTurn();
        attack(appletun, 0).execute(board.contextFor(appletun));
        assertEquals(40 + 70, target.damage());
    }

    @Test
    @DisplayName("Sweets Overload counts every Sweets Relay your side has used")
    void sweetsOverloadCountsTheGame() {
        PokemonInPlay milcery = board.active(board.you, Psychic.MILCERY);
        PokemonInPlay target = board.active(board.them, Colorless.SNORLAX_EX);
        milcery.attachEnergy(Type.COLORLESS, 1);
        attack(milcery, 0).execute(board.contextFor(milcery));
        nextOwnTurn();
        attack(milcery, 0).execute(board.contextFor(milcery));
        int before = target.damage();

        PokemonInPlay alcremie = board.active(board.you, Psychic.ALCREMIE);
        alcremie.attachEnergy(Type.COLORLESS, 1);
        attack(alcremie, 0).execute(board.contextFor(alcremie));

        assertEquals(80, target.damage() - before);
    }

    @Test
    @DisplayName("Beginning Bolt adds 20 only on the turn Jolteon evolved")
    void beginningBoltOnlyOnEvolutionTurn() {
        PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
        eevee.setTurnPlayed(board.battle.turn() - 2);
        PokemonInPlay target = board.active(board.them, Colorless.SNORLAX_EX);
        CardInstance jolteon = board.inHand(board.you, Lightning.JOLTEON);
        new EvolveAction(jolteon, new AttackerActive()).execute(board.contextFor(eevee));
        eevee.attachEnergy(Type.LIGHTNING, 1);

        attack(eevee, 0).execute(board.contextFor(eevee));
        assertEquals(60, target.damage());

        board.you.resetTurnFlags();
        attack(eevee, 0).execute(board.contextFor(eevee));
        assertEquals(60 + 40, target.damage());
    }

    @Test
    @DisplayName("Veevee 'volve lets Eevee ex evolve into anything that evolves from Eevee, and nothing else")
    void veeveeVolve() {
        PokemonInPlay eeveeEx = board.active(board.you, Colorless.EEVEE_EX);
        eeveeEx.setTurnPlayed(board.battle.turn() - 2);
        CardInstance leafeon = board.inHand(board.you, Grass.LEAFEON);
        CardInstance steenee = board.inHand(board.you, Grass.STEENEE);

        assertFalse(new EvolveAction(steenee, new AttackerActive()).isLegal(board.contextFor(eeveeEx)));
        assertTrue(new EvolveAction(leafeon, new AttackerActive()).isLegal(board.contextFor(eeveeEx)));
    }

    @Test
    @DisplayName("Happy Ribbon draws 2 if you accept")
    void happyRibbonAccepted() {
        assertEquals(2, evolveIntoSylveonEx(new ScriptedPlayer("you", 0), 2));
    }

    @Test
    @DisplayName("Happy Ribbon draws nothing if you decline")
    void happyRibbonDeclined() {
        assertEquals(0, evolveIntoSylveonEx(new ScriptedPlayer("you", 1), 2));
    }

    @Test
    @DisplayName("Happy Ribbon asks nothing with an empty deck")
    void happyRibbonEmptyDeck() {
        Recording you = new Recording();
        assertEquals(0, evolveIntoSylveonEx(you, 0));
        assertTrue(you.decisions.isEmpty());
    }

    /** The cards in hand after evolving, with the given number of cards in the deck. */
    private static int evolveIntoSylveonEx(IPlayer you, int deck) {
        TestBoard board = new TestBoard(you, new RandomPlayer("them", new SeededRandom(1)));
        PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
        eevee.setTurnPlayed(board.battle.turn() - 2);
        for (int i = 0; i < deck; i++) {
            board.inDeck(board.you, Grass.TROPIUS);
        }
        CardInstance sylveon = board.inHand(board.you, Psychic.SYLVEON_EX);

        new EvolveAction(sylveon, new AttackerActive()).execute(board.contextFor(eevee));

        return board.you.hand().size();
    }

    @Test
    @DisplayName("Combust can't be used without Fire Energy in the discard pile, and costs 20 damage")
    void combust() {
        PokemonInPlay flareon = board.active(board.you, Fire.FLAREON_EX);
        UseAbilityAction combust = new UseAbilityAction(flareon, flareon.definition().ability().orElseThrow());
        assertFalse(combust.isLegal(board.contextFor(flareon)));

        board.you.addDiscardedEnergy(Type.FIRE, 1);
        combust.execute(board.contextFor(flareon));

        assertEquals(1, flareon.energyOf(Type.FIRE));
        assertEquals(20, flareon.damage());
    }

    @Test
    @DisplayName("Energy Crush counts Energy across the opponent's whole side")
    void energyCrush() {
        PokemonInPlay espeon = board.active(board.you, Psychic.ESPEON);
        PokemonInPlay target = board.active(board.them, Colorless.SNORLAX_EX);
        board.bench(board.them, Colorless.EEVEE).attachEnergy(Type.GRASS, 2);
        target.attachEnergy(Type.COLORLESS, 1);
        espeon.attachEnergy(Type.PSYCHIC, 1);

        attack(espeon, 0).execute(board.contextFor(espeon));

        assertEquals(60, target.damage());
    }

    @Test
    @DisplayName("Try to Imitate never offers another copy attack")
    void mimikyuWontCopyMimikyu() {
        TestBoard board = new TestBoard(ScriptedRandom.flipping(true));
        PokemonInPlay mimikyu = board.active(board.you, Psychic.MIMIKYU);
        board.active(board.them, Psychic.MIMIKYU);
        mimikyu.attachEnergy(Type.PSYCHIC, 2);

        assertFalse(attack(mimikyu, 0).execute(board.contextFor(mimikyu)).failed());
    }

    @Test
    @DisplayName("Leftovers heals its Active holder at the end of its owner's turn only")
    void leftovers() {
        PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
        eevee.attachTool(board.loose(board.you, Trainers.LEFTOVERS));
        eevee.takeDamage(50);

        TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));
        assertEquals(50, eevee.damage());

        TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
        assertEquals(40, eevee.damage());
    }

    @Test
    @DisplayName("Articuno ex's Blizzard does 80, plus 10 to each Benched Pokémon")
    void blizzard() {
        PokemonInPlay articuno = board.active(board.you, (PokemonCard) CardPool.get("A3b-104"));
        PokemonInPlay defender = board.active(board.them, Colorless.SNORLAX_EX);
        PokemonInPlay benched = board.bench(board.them, Colorless.EEVEE);

        articuno.definition().actions().get(1).execute(board.contextFor(articuno));

        assertEquals(80, defender.damage());
        assertEquals(10, benched.damage());
    }

    /** Takes the first option of every decision, keeping them to assert on. */
    private static final class Recording implements IPlayer {
        final List<Decision<?>> decisions = new ArrayList<>();

        @Override
        public String name() {
            return "you";
        }

        @Override
        public <T> T choose(Decision<T> decision) {
            decisions.add(decision);
            return decision.options().get(0);
        }
    }

    @Test
    @DisplayName("Penny shows you a Supporter from the opponent's deck, uses it, and leaves it there")
    void pennyShowsThenUsesTheirSupporter() {
        Recording you = new Recording();
        TestBoard board = new TestBoard(you, new RandomPlayer("them", new SeededRandom(1)));
        PokemonInPlay active = board.active(board.you, Water.PRIMARINA_EX);
        PokemonInPlay defender = board.active(board.them, Colorless.SNORLAX_EX);
        CardInstance hau = board.inDeck(board.them, Trainers.HAU);
        board.inDeck(board.them, Trainers.PENNY);

        Trainers.PENNY.actions().get(0).execute(board.contextFor(active));

        assertEquals(List.of(hau), you.decisions.get(0).options(), "shown, and never Penny");
        assertEquals(2, board.them.deck().size());
        assertEquals(30, board.you.attackBonusFor(active, defender, board.battle.turn()));
    }

    @Test
    @DisplayName("Penny with no other Supporter in the opponent's deck shows nothing and does nothing")
    void pennyFindsNothing() {
        Recording you = new Recording();
        TestBoard board = new TestBoard(you, new RandomPlayer("them", new SeededRandom(1)));
        PokemonInPlay active = board.active(board.you, Water.PRIMARINA_EX);
        PokemonInPlay defender = board.active(board.them, Colorless.SNORLAX_EX);
        board.inDeck(board.them, Trainers.PENNY);

        Trainers.PENNY.actions().get(0).execute(board.contextFor(active));

        assertTrue(you.decisions.isEmpty());
        assertEquals(0, board.you.attackBonusFor(active, defender, board.battle.turn()));
    }

    @Test
    @DisplayName("Penny asks for a target when the Supporter is dragged onto a Pokémon")
    void pennyAsksForADragTarget() {
        Recording you = new Recording();
        TestBoard board = new TestBoard(you, new RandomPlayer("them", new SeededRandom(1)));
        PokemonInPlay active = board.active(board.you, Grass.TROPIUS);
        board.bench(board.you, Grass.BOUNSWEET).takeDamage(10);
        board.active(board.them, Colorless.SNORLAX_EX);
        active.takeDamage(40);
        board.inDeck(board.them, ERIKA);

        Trainers.PENNY.actions().get(0).execute(board.contextFor(active));

        assertEquals(2, you.decisions.size());
        assertEquals(active, you.decisions.get(1).options().get(0));
        assertEquals(0, active.damage());
    }
}
