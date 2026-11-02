package com.tcgpocket.pool.A4a;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.Action;
import com.tcgpocket.action.EvolveAction;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.engine.TurnEngine;
import com.tcgpocket.player.Decision;
import com.tcgpocket.player.IPlayer;
import com.tcgpocket.player.RandomPlayer;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.SeededRandom;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** One assertion per card that needed a new node, or whose tree is not obvious. */
class SecludedSpringsTest {

    private static final PokemonCard BIG_TARGET = (PokemonCard) CardPool.get("A3b-057");

    private final TestBoard board = new TestBoard();

    private static Action attack(PokemonInPlay pokemon) {
        return (Action) pokemon.definition().actions().get(0);
    }

    /** Takes the option at {@code pick} of every decision, keeping them to assert on. */
    private static final class Recording implements IPlayer {
        final List<Decision<?>> decisions = new ArrayList<>();
        final int pick;

        Recording(int pick) {
            this.pick = pick;
        }

        @Override
        public String name() {
            return "you";
        }

        @Override
        public <T> T choose(Decision<T> decision) {
            decisions.add(decision);
            return decision.options().get(pick);
        }
    }

    private static TestBoard boardFor(IPlayer you) {
        return new TestBoard(you, new RandomPlayer("them", new SeededRandom(1)));
    }

    /** Poliwhirl over Poliwag, on the given side. */
    private PokemonInPlay evolvedActive(TestBoard on, boolean yours) {
        var side = yours ? on.you : on.them;
        PokemonInPlay pokemon = on.active(side, Water.POLIWAG);
        pokemon.evolveInto(on.loose(side, Water.POLIWHIRL), on.battle.turn());
        return pokemon;
    }

    private void endOpponentsNextTurn() {
        board.battle.switchSides();
        TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.battle.attacker()));
    }

    @Test
    @DisplayName("A4a-001 to A4a-105 are in the pool")
    void wholeSet() {
        assertEquals(105, SecludedSprings.CARDS.size());
        for (int number = 1; number <= 105; number++) {
            assertTrue(CardPool.find("A4a-%03d".formatted(number)).isPresent(), "A4a-" + number);
        }
        assertEquals("Hoppip", CardPool.get("A4a-001").name());
        assertEquals("Fletchling", CardPool.get("A4a-066").name());
        assertEquals("Morty", CardPool.get("A4a-071").name());
        assertEquals(CardRarity.ILLUSTRATION_RARE, CardPool.get("A4a-072").rarity());
        assertEquals("Azurill", CardPool.get("A4a-077").name());
        assertEquals(CardRarity.ILLUSTRATION_RARE, CardPool.get("A4a-077").rarity());
        for (int number = 78; number <= 85; number++) {
            assertEquals(CardRarity.ULTRA_RARE, CardPool.get("A4a-%03d".formatted(number)).rarity());
        }
        assertEquals("Jumpluff ex", CardPool.get("A4a-078").name());
        assertEquals("Morty", CardPool.get("A4a-085").name());
        for (int number = 86; number <= 89; number++) {
            assertEquals(CardRarity.SPECIAL_ILLUSTRATION_RARE, CardPool.get("A4a-%03d".formatted(number)).rarity());
        }
        assertEquals(CardRarity.IMMERSIVE, CardPool.get("A4a-090").rarity());
        for (int number = 91; number <= 100; number++) {
            assertEquals(CardRarity.SHINY_RARE, CardPool.get("A4a-%03d".formatted(number)).rarity());
        }
        for (int number = 101; number <= 104; number++) {
            assertEquals(CardRarity.SHINY_ULTRA_RARE, CardPool.get("A4a-%03d".formatted(number)).rarity());
        }
        assertEquals(CardRarity.HYPER_RARE, CardPool.get("A4a-105").rarity());
        assertEquals("Pyukumuku", CardPool.get("A4a-097").name());
        assertEquals("Mantyke", CardPool.get("A4a-105").name());
    }

    @Test
    @DisplayName("Baby Pokémon and Altaria have no Weakness, whatever the API says")
    void noWeakness() {
        for (ICard card : List.of(Water.MANTYKE, Colorless.IGGLYBUFF, Colorless.AZURILL, Dragon.ALTARIA)) {
            assertTrue(((PokemonCard) card).weakness().isEmpty(), card.name());
        }
        assertTrue(Colorless.AZURILL.retreatCost().isFree());
        assertTrue(((Action) Colorless.AZURILL.actions().get(0)).cost().isFree());
        assertTrue(Lightning.TYNAMO.retreatCost().isFree());
    }

    @Test
    @DisplayName("Temporal Leaves devolves: damage, Energy and Tool stay, the top card goes to hand")
    void devolves() {
        PokemonInPlay celebi = board.active(board.you, Grass.CELEBI);
        PokemonInPlay target = evolvedActive(board, false);
        target.attachEnergy(Type.WATER, 2);
        target.attachTool(board.loose(board.them, Trainers.MEMORY_LIGHT));
        target.addStatus(new PoisonStatus());
        target.addModifier(new com.tcgpocket.state.ActiveModifier(ModifierKind.CANNOT_ATTACK, 0, 99));

        attack(celebi).execute(board.contextFor(celebi));

        assertEquals("Poliwag", target.definition().name());
        assertEquals(40, target.damage());
        assertEquals(2, target.totalEnergy());
        assertTrue(target.hasTool());
        assertTrue(target.statuses().isEmpty());
        assertTrue(target.modifiers().isEmpty());
        assertEquals("Poliwhirl", board.them.hand().get(0).definition().name());
    }

    @Test
    @DisplayName("Temporal Leaves leaves a Basic alone")
    void basicStays() {
        PokemonInPlay celebi = board.active(board.you, Grass.CELEBI);
        PokemonInPlay target = board.active(board.them, Water.POLIWAG);

        attack(celebi).execute(board.contextFor(celebi));

        assertEquals(40, target.damage());
        assertTrue(board.them.hand().isEmpty());
    }

    @Test
    @DisplayName("Temporal Leaves doesn't return a card from a Pokémon its damage knocked out")
    void knockedOutKeepsItsStack() {
        PokemonInPlay celebi = board.active(board.you, Grass.CELEBI);
        PokemonInPlay target = evolvedActive(board, false);
        target.takeDamage(70);

        attack(celebi).execute(board.contextFor(celebi));

        assertTrue(target.isKnockedOut());
        assertEquals("Poliwhirl", target.definition().name());
        assertTrue(board.them.hand().isEmpty());
    }

    @Test
    @DisplayName("Devolving knocks out a Pokémon whose damage reaches the lower HP")
    void devolvingCanKnockOut() {
        PokemonInPlay celebi = board.active(board.you, Grass.CELEBI);
        PokemonInPlay target = evolvedActive(board, false);
        target.takeDamage(30);

        attack(celebi).execute(board.contextFor(celebi));

        assertEquals("Poliwag", target.definition().name());
        assertTrue(target.isKnockedOut());
        assertEquals(1, board.them.hand().size());
    }

    @Test
    @DisplayName("Cursed Prose lands a plain 90, ignoring Weakness, at the end of the opponent's next turn")
    void cursedProse() {
        PokemonInPlay mismagius = board.active(board.you, Psychic.MISMAGIUS);
        // Weak to Psychic, so attack damage would be 110.
        PokemonInPlay target = board.active(board.them, Fighting.POLIWRATH_EX);

        attack(mismagius).execute(board.contextFor(mismagius));
        TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.battle.attacker()));
        assertEquals(0, target.damage(), "not at the end of the attacking turn");

        endOpponentsNextTurn();
        assertEquals(90, target.damage());
    }

    @Test
    @DisplayName("Cursed Prose survives evolving")
    void cursedProseSurvivesEvolving() {
        PokemonInPlay mismagius = board.active(board.you, Psychic.MISMAGIUS);
        PokemonInPlay target = board.active(board.them, Psychic.FRILLISH);
        attack(mismagius).execute(board.contextFor(mismagius));

        target.evolveInto(board.loose(board.them, Psychic.JELLICENT), board.battle.turn());
        endOpponentsNextTurn();

        assertEquals(90, target.damage());
    }

    @Test
    @DisplayName("Leaving the Active Spot ends Cursed Prose")
    void cursedProseEndsOnLeaving() {
        PokemonInPlay mismagius = board.active(board.you, Psychic.MISMAGIUS);
        PokemonInPlay target = board.active(board.them, Psychic.JELLICENT);
        attack(mismagius).execute(board.contextFor(mismagius));

        target.clearTemporaryState();
        endOpponentsNextTurn();

        assertEquals(0, target.damage());
    }

    @Test
    @DisplayName("Defiant Spark costs 1 Lightning only while Boltund has damage on it")
    void defiantSpark() {
        PokemonInPlay boltund = board.active(board.you, Lightning.BOLTUND);
        boltund.attachEnergy(Type.LIGHTNING, 1);
        assertFalse(attack(boltund).isLegal(board.contextFor(boltund)));

        boltund.takeDamage(10);
        assertTrue(attack(boltund).isLegal(board.contextFor(boltund)));

        boltund.heal(10);
        boltund.attachEnergy(Type.COLORLESS, 2);
        assertTrue(attack(boltund).isLegal(board.contextFor(boltund)), "the printed cost still works");
    }

    @Test
    @DisplayName("Rolling Frenzy stacks +30 until Miltank leaves the Active Spot")
    void rollingFrenzy() {
        PokemonInPlay miltank = board.active(board.you, Colorless.MILTANK);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);

        attack(miltank).execute(board.contextFor(miltank));
        assertEquals(10, target.damage());
        attack(miltank).execute(board.contextFor(miltank));
        assertEquals(10 + 40, target.damage());
        attack(miltank).execute(board.contextFor(miltank));
        assertEquals(10 + 40 + 70, target.damage());

        miltank.clearTemporaryState();
        attack(miltank).execute(board.contextFor(miltank));
        assertEquals(10 + 40 + 70 + 10, target.damage());
    }

    @Test
    @DisplayName("Litter does 50 per Tool discarded, and nothing with none to discard")
    void litter() {
        Recording you = new Recording(3); // options: none, A, B, A+B
        TestBoard own = boardFor(you);
        PokemonInPlay slowking = own.active(own.you, Water.SLOWKING);
        PokemonInPlay target = own.active(own.them, BIG_TARGET);
        own.inHand(own.you, Trainers.MEMORY_LIGHT);
        own.inHand(own.you, Trainers.INFLATABLE_BOAT);
        own.inHand(own.you, Trainers.MORTY);

        attack(slowking).execute(own.contextFor(slowking));

        assertEquals(100, target.damage());
        assertEquals(1, own.you.hand().size());
        assertEquals(2, own.you.discardPile().size());

        attack(slowking).execute(own.contextFor(slowking));
        assertEquals(100, target.damage());
        assertEquals(1, you.decisions.size(), "no Tool left, so nothing to ask");
    }

    @Test
    @DisplayName("Cliff Crumbler adds 60 only for a Fighting Pokémon off the top")
    void cliffCrumbler() {
        PokemonInPlay dugtrio = board.active(board.you, Fighting.DUGTRIO);
        PokemonInPlay target = board.active(board.them, Fighting.POLIWRATH_EX);
        board.inDeck(board.you, Fighting.DIGLETT);
        board.inDeck(board.you, Water.POLIWAG);

        attack(dugtrio).execute(board.contextFor(dugtrio));
        assertEquals(100, target.damage());

        attack(dugtrio).execute(board.contextFor(dugtrio));
        assertEquals(140, target.damage());

        attack(dugtrio).execute(board.contextFor(dugtrio));
        assertEquals(180, target.damage(), "an empty deck gives no bonus");
    }

    @Test
    @DisplayName("Dragon Arcana counts types of Energy, not Energy")
    void dragonArcana() {
        PokemonInPlay altaria = board.active(board.you, Dragon.ALTARIA);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);
        altaria.attachEnergy(Type.WATER, 2);

        attack(altaria).execute(board.contextFor(altaria));
        assertEquals(40, target.damage());

        altaria.attachEnergy(Type.FIRE, 1);
        attack(altaria).execute(board.contextFor(altaria));
        assertEquals(140, target.damage());
    }

    @Test
    @DisplayName("Crystal Waltz counts both Benches")
    void crystalWaltz() {
        PokemonInPlay suicune = board.active(board.you, Water.SUICUNE_EX);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);
        board.bench(board.you, Water.POLIWAG);
        board.bench(board.them, Water.POLIWAG);
        board.bench(board.them, Water.POLIWAG);

        attack(suicune).execute(board.contextFor(suicune));

        assertEquals(60, target.damage());
    }

    @Test
    @DisplayName("Legendary Pulse draws at the end of your own turn, from the Active Spot only")
    void legendaryPulse() {
        board.active(board.you, Fire.ENTEI_EX);
        board.bench(board.you, Water.SUICUNE_EX);
        board.inDeck(board.you, Water.POLIWAG);
        board.inDeck(board.you, Water.POLIWAG);

        TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));
        assertTrue(board.you.hand().isEmpty());

        TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
        assertEquals(1, board.you.hand().size());
    }

    @Test
    @DisplayName("Illusive Trickery shields Zoroark after it knocks out an opposing Pokémon")
    void illusiveTrickery() {
        PokemonInPlay zoroark = board.active(board.you, Darkness.ZOROARK);
        PokemonInPlay ownBench = board.bench(board.you, Water.POLIWAG);
        PokemonInPlay target = board.active(board.them, Water.POLIWAG);
        int turn = board.battle.turn();

        TriggerDispatcher.dispatch(board.battle, new Knockout(target, Optional.empty()));
        TriggerDispatcher.dispatch(board.battle, new Knockout(ownBench, Optional.of(zoroark)));
        assertFalse(zoroark.hasModifier(ModifierKind.PREVENT_DAMAGE, turn));

        TriggerDispatcher.dispatch(board.battle, new Knockout(target, Optional.of(zoroark)));
        assertTrue(zoroark.hasModifier(ModifierKind.PREVENT_DAMAGE, turn + 1));
        assertTrue(zoroark.hasModifier(ModifierKind.PREVENT_EFFECTS, turn + 1));
    }

    @Test
    @DisplayName("Perish Body knocks out the attacker on heads, from the Active Spot only")
    void perishBody() {
        TestBoard heads = new TestBoard(ScriptedRandom.alwaysHeads());
        PokemonInPlay attacker = heads.active(heads.you, BIG_TARGET);
        PokemonInPlay cursola = heads.active(heads.them, Psychic.GALARIAN_CURSOLA);
        TriggerDispatcher.dispatch(heads.battle, new Knockout(cursola, Optional.of(attacker)));
        assertTrue(attacker.isKnockedOut());

        TestBoard tails = new TestBoard(ScriptedRandom.alwaysTails());
        PokemonInPlay safe = tails.active(tails.you, BIG_TARGET);
        PokemonInPlay flipped = tails.active(tails.them, Psychic.GALARIAN_CURSOLA);
        TriggerDispatcher.dispatch(tails.battle, new Knockout(flipped, Optional.of(safe)));
        assertEquals(0, safe.damage());

        TestBoard benched = new TestBoard(ScriptedRandom.alwaysHeads());
        PokemonInPlay untouched = benched.active(benched.you, BIG_TARGET);
        PokemonInPlay onBench = benched.bench(benched.them, Psychic.GALARIAN_CURSOLA);
        TriggerDispatcher.dispatch(benched.battle, new Knockout(onBench, Optional.of(untouched)));
        assertEquals(0, untouched.damage());
    }

    @Test
    @DisplayName("Counterattack answers attack damage to the Active Zangoose, not poison")
    void counterattack() {
        PokemonInPlay attacker = board.active(board.you, BIG_TARGET);
        PokemonInPlay zangoose = board.active(board.them, Colorless.ZANGOOSE);

        TriggerDispatcher.dispatch(board.battle, new DamageDealt(Optional.empty(), zangoose, 10));
        assertEquals(0, attacker.damage());

        TriggerDispatcher.dispatch(board.battle, new DamageDealt(Optional.of(attacker), zangoose, 10));
        assertEquals(20, attacker.damage());
    }

    @Test
    @DisplayName("Infiltrating Inspection reveals the opponent's hand when Misdreavus is benched from hand")
    void infiltratingInspection() {
        Recording you = new Recording(0);
        TestBoard own = boardFor(you);
        PokemonInPlay active = own.active(own.you, BIG_TARGET);
        own.inHand(own.them, Water.POLIWAG);
        CardInstance misdreavus = own.inHand(own.you, Psychic.MISDREAVUS);

        assertTrue(new PlayCardAction(misdreavus, 1).execute(own.contextFor(active)).succeeded());

        assertEquals(1, own.them.revealedHand().size());
    }

    @Test
    @DisplayName("Healing Ripples heals 60 from a chosen Water Pokémon, asking nothing with none damaged")
    void healingRipples() {
        Recording you = new Recording(0);
        TestBoard own = boardFor(you);
        PokemonInPlay feebas = own.active(own.you, Water.FEEBAS);
        feebas.setTurnPlayed(own.battle.turn() - 2);
        PokemonInPlay hurt = own.bench(own.you, Water.POLIWAG);
        hurt.takeDamage(50);
        CardInstance milotic = own.inHand(own.you, Water.MILOTIC);

        new EvolveAction(milotic, new AttackerActive()).execute(own.contextFor(feebas));

        assertEquals(0, hurt.damage());
    }

    @Test
    @DisplayName("Fantastical Floating frees Latios only with Latias in play")
    void fantasticalFloating() {
        PokemonInPlay latios = board.active(board.you, Psychic.LATIOS);
        assertFalse(RetreatAction.retreatCostOf(board.contextFor(latios), latios).isFree());

        board.bench(board.you, Psychic.LATIAS);
        assertTrue(RetreatAction.retreatCostOf(board.contextFor(latios), latios).isFree());
    }

    @Test
    @DisplayName("Crossing Flights looks for Latios on the Bench")
    void crossingFlights() {
        PokemonInPlay latias = board.active(board.you, Psychic.LATIAS);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);

        attack(latias).execute(board.contextFor(latias));
        assertEquals(20, target.damage());

        board.bench(board.you, Psychic.LATIOS);
        attack(latias).execute(board.contextFor(latias));
        assertEquals(60, target.damage());
    }

    @Test
    @DisplayName("Inflatable Boat takes 1 off a Water holder's Retreat Cost, and nobody else's")
    void inflatableBoat() {
        PokemonInPlay water = board.active(board.you, Water.TENTACRUEL);
        water.attachTool(board.loose(board.you, Trainers.INFLATABLE_BOAT));
        assertEquals(1, RetreatAction.retreatCostOf(board.contextFor(water), water).total());

        PokemonInPlay other = board.active(board.them, Fire.MAGCARGO);
        other.attachTool(board.loose(board.them, Trainers.INFLATABLE_BOAT));
        assertEquals(3, RetreatAction.retreatCostOf(board.contextFor(other), other).total());
    }

    @Test
    @DisplayName("Memory Light offers the attacks beneath, to the turn engine too")
    void memoryLight() {
        PokemonInPlay pokemon = evolvedActive(board, true);
        assertEquals(1, pokemon.attacks().size());

        pokemon.attachTool(board.loose(board.you, Trainers.MEMORY_LIGHT));
        assertEquals(List.of(Water.POLIWHIRL.actions().get(0), Water.POLIWAG.actions().get(0)), pokemon.attacks());

        board.active(board.them, BIG_TARGET);
        pokemon.attachEnergy(Type.WATER, 1);
        assertTrue(new TurnEngine(board.battle).legalActions().contains(Water.POLIWAG.actions().get(0)));
    }

    @Test
    @DisplayName("Whitney needs a Miltank it would help, and leaves Poison on it")
    void whitney() {
        PokemonInPlay active = board.active(board.you, Colorless.MILTANK);
        CardInstance whitney = board.inHand(board.you, Trainers.WHITNEY);
        assertFalse(new PlayCardAction(whitney, active).isLegal(board.contextFor(active)));

        active.takeDamage(70);
        active.addStatus(new SleepStatus());
        active.addStatus(new PoisonStatus());
        assertTrue(new PlayCardAction(whitney, active).execute(board.contextFor(active)).succeeded());

        assertEquals(10, active.damage());
        assertFalse(active.hasStatus(new SleepStatus()));
        assertTrue(active.hasStatus(new PoisonStatus()));
    }

    @Test
    @DisplayName("Traveling Merchant takes every Tool from the top 4, and no deeper")
    void travelingMerchant() {
        PokemonInPlay active = board.active(board.you, BIG_TARGET);
        board.inDeck(board.you, Trainers.MEMORY_LIGHT);
        board.inDeck(board.you, Water.POLIWAG);
        board.inDeck(board.you, Trainers.INFLATABLE_BOAT);
        board.inDeck(board.you, Water.POLIWAG);
        board.inDeck(board.you, Trainers.MEMORY_LIGHT);
        CardInstance merchant = board.inHand(board.you, Trainers.TRAVELING_MERCHANT);

        assertTrue(new PlayCardAction(merchant).execute(board.contextFor(active)).succeeded());

        assertEquals(2, board.you.hand().size());
        assertEquals(3, board.you.deck().size());
    }

    @Test
    @DisplayName("Morty arranges the opponent's deck, one card per Psychic Pokémon, and needs one")
    void morty() {
        Recording you = new Recording(1);
        TestBoard own = boardFor(you);
        PokemonInPlay active = own.active(own.you, Fire.SLUGMA);
        CardInstance first = own.inDeck(own.them, Water.POLIWAG);
        CardInstance second = own.inDeck(own.them, Fire.SLUGMA);
        CardInstance third = own.inDeck(own.them, Grass.HOPPIP);
        CardInstance morty = own.inHand(own.you, Trainers.MORTY);
        assertFalse(new PlayCardAction(morty).isLegal(own.contextFor(active)));

        own.bench(own.you, Psychic.MISDREAVUS);
        own.bench(own.you, Psychic.FRILLISH);
        assertTrue(new PlayCardAction(morty).execute(own.contextFor(active)).succeeded());

        assertEquals(List.of(second, first, third), own.them.deck());
        assertEquals(1, you.decisions.size(), "the last card asks nothing");
        assertEquals(List.of(second, first), own.them.topCardsSeenByOpponent());
        assertTrue(own.them.seenTopCards().isEmpty(), "the deck's owner saw nothing");
    }
}
