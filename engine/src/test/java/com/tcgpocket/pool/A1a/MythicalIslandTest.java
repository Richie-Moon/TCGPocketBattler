package com.tcgpocket.pool.A1a;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.action.EvolveAction;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.HasEnergy;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBenchSpecific;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBenchSpecific;
import com.tcgpocket.target.Self;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** One assertion per card that the tree does what the printed text says. */
class MythicalIslandTest {

    @Nested
    @DisplayName("Fighting — Aerodactyl ex's Primeval Law")
    class PrimevalLaw {

        private final TestBoard board = new TestBoard();
        private final ResolutionContext context = board.contextWithoutSource();
        private final CardInstance ivysaur = board.inHand(board.you, com.tcgpocket.pool.A1.Grass.IVYSAUR);

        private boolean canEvolve(ITarget onto) {
            return new EvolveAction(ivysaur, onto).isLegal(context);
        }

        @Test
        @DisplayName("their Aerodactyl ex, even Benched, stops you evolving your Active")
        void locksYourActive() {
            board.active(board.you, com.tcgpocket.pool.A1.Grass.BULBASAUR);
            assertTrue(canEvolve(new AttackerActive()), "free before Aerodactyl ex arrives");

            board.bench(board.them, Fighting.AERODACTYL_EX);

            assertFalse(canEvolve(new AttackerActive()));
        }

        @Test
        @DisplayName("your Benched Pokémon still evolve")
        void benchIsFree() {
            board.active(board.you, com.tcgpocket.pool.A1.Grass.BULBASAUR);
            board.bench(board.you, com.tcgpocket.pool.A1.Grass.BULBASAUR);
            board.active(board.them, Fighting.AERODACTYL_EX);

            assertTrue(canEvolve(new AttackerBenchSpecific(0)));
        }

        @Test
        @DisplayName("your own Aerodactyl ex never locks you")
        void neverLocksItsOwner() {
            board.active(board.you, com.tcgpocket.pool.A1.Grass.BULBASAUR);
            board.bench(board.you, Fighting.AERODACTYL_EX);

            assertTrue(canEvolve(new AttackerActive()));
        }
    }

    @Nested
    @DisplayName("Water — Vaporeon's Wash Out")
    class WashOut {

        private static final PokemonCard SPLASH = PokemonCard.basic("test-splash", "Splash", 100, Type.WATER, 1);
        private static final PokemonCard EMBER = PokemonCard.basic("test-ember", "Ember", 100, Type.FIRE, 1);

        /** Picks the second Benched Pokémon whenever there is a choice. */
        private final TestBoard board = new TestBoard(new ScriptedPlayer("you", 1), new ScriptedPlayer("them"));
        private final ResolutionContext context = board.contextWithoutSource();

        private IAction washOut(PokemonInPlay vaporeon) {
            return new UseAbilityAction(vaporeon, Water.VAPOREON.ability().orElseThrow());
        }

        @Test
        @DisplayName("as often as you like, until the Bench runs dry")
        void usableRepeatedly() {
            PokemonInPlay vaporeon = board.active(board.you, Water.VAPOREON);
            PokemonInPlay splash = board.bench(board.you, SPLASH);
            splash.attachEnergy(Type.WATER, 2);

            assertTrue(washOut(vaporeon).execute(context).succeeded());
            assertTrue(washOut(vaporeon).execute(context).succeeded());

            assertEquals(2, vaporeon.energyOf(Type.WATER));
            assertEquals(0, splash.energyOf(Type.WATER));
            assertFalse(washOut(vaporeon).isLegal(context), "nothing left to move");
        }

        @Test
        @DisplayName("you choose which Benched Pokémon gives the Energy")
        void youChooseTheSource() {
            PokemonInPlay vaporeon = board.active(board.you, Water.VAPOREON);
            PokemonInPlay first = board.bench(board.you, SPLASH);
            PokemonInPlay second = board.bench(board.you, SPLASH);
            first.attachEnergy(Type.WATER, 1);
            second.attachEnergy(Type.WATER, 1);

            washOut(vaporeon).execute(context);

            assertEquals(1, first.energyOf(Type.WATER));
            assertEquals(0, second.energyOf(Type.WATER));
        }

        @Test
        @DisplayName("moves only Water, even from a Pokémon with other Energy")
        void movesOnlyWater() {
            PokemonInPlay vaporeon = board.active(board.you, Water.VAPOREON);
            PokemonInPlay splash = board.bench(board.you, SPLASH);
            splash.attachEnergy(Type.FIRE, 3);
            splash.attachEnergy(Type.WATER, 1);

            washOut(vaporeon).execute(context);

            assertEquals(Map.of(Type.WATER, 1), vaporeon.attachedEnergy());
            assertEquals(Map.of(Type.FIRE, 3), splash.attachedEnergy());
        }

        @Test
        @DisplayName("not offered unless both ends are Water Pokémon")
        void bothEndsMustBeWater() {
            PokemonInPlay ember = board.active(board.you, EMBER);
            PokemonInPlay vaporeon = board.bench(board.you, Water.VAPOREON);
            vaporeon.attachEnergy(Type.WATER, 1);
            assertFalse(washOut(vaporeon).isLegal(context), "Active is Fire");

            TestBoard other = new TestBoard();
            PokemonInPlay active = other.active(other.you, Water.VAPOREON);
            other.bench(other.you, EMBER).attachEnergy(Type.WATER, 1);
            assertFalse(washOut(active).isLegal(other.contextWithoutSource()), "Benched source is Fire");
        }
    }

    @Nested
    @DisplayName("Grass — Serperior's Jungle Totem")
    class JungleTotem {

        private static final PokemonCard LEAF = PokemonCard.basic("test-leaf", "Leaf", 100, Type.GRASS, 2);
        private static final PokemonCard ROCK = PokemonCard.basic("test-rock", "Rock", 100, Type.FIGHTING, 2);
        private static final HasEnergy TWO_GRASS = new HasEnergy(EnergyCost.of(Type.GRASS, 2));

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("from the Bench, one Grass Energy pays for two")
        void oneGrassPaysForTwo() {
            PokemonInPlay leaf = board.active(board.you, LEAF);
            leaf.attachEnergy(Type.GRASS, 1);
            assertFalse(TWO_GRASS.evaluate(leaf), "not without Serperior");

            board.bench(board.you, Grass.SERPERIOR);

            assertTrue(TWO_GRASS.evaluate(leaf));
            assertEquals(Map.of(Type.GRASS, 1), leaf.attachedEnergy(), "still one card attached");
        }

        @Test
        @DisplayName("only Grass Pokémon benefit")
        void onlyGrassPokemon() {
            PokemonInPlay rock = board.active(board.you, ROCK);
            rock.attachEnergy(Type.GRASS, 1);
            board.bench(board.you, Grass.SERPERIOR);

            assertEquals(Map.of(Type.GRASS, 1), rock.providedEnergy());
        }

        @Test
        @DisplayName("the opponent's Serperior does nothing for you")
        void onlyYourOwnSide() {
            PokemonInPlay leaf = board.active(board.you, LEAF);
            leaf.attachEnergy(Type.GRASS, 1);
            board.active(board.them, Grass.SERPERIOR);

            assertFalse(TWO_GRASS.evaluate(leaf));
        }

        @Test
        @DisplayName("two Serperior do not stack")
        void doesNotStack() {
            PokemonInPlay leaf = board.active(board.you, LEAF);
            leaf.attachEnergy(Type.GRASS, 1);
            board.bench(board.you, Grass.SERPERIOR);
            board.bench(board.you, Grass.SERPERIOR);

            assertEquals(Map.of(Type.GRASS, 2), leaf.providedEnergy());
        }

        @Test
        @DisplayName("a retreat cost of 2 is paid by discarding one Grass Energy")
        void paysRetreatCost() {
            PokemonInPlay leaf = board.active(board.you, LEAF);
            leaf.attachEnergy(Type.GRASS, 2);
            board.bench(board.you, Grass.SERPERIOR);
            IAction retreat = new RetreatAction(new AttackerBenchSpecific(0));

            assertTrue(retreat.execute(board.contextWithoutSource()).succeeded());

            assertEquals(Map.of(Type.GRASS, 1), leaf.attachedEnergy());
        }
    }

    @Nested
    @DisplayName("Dragon — Druddigon's Rough Skin")
    class RoughSkin {

        private static final PokemonCard HITTER = PokemonCard.basic("test-hitter", "Hitter", 100, Type.FIGHTING, 1);

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("in the Active Spot, 20 damage back at the attacker")
        void hitsBack() {
            PokemonInPlay hitter = board.active(board.you, HITTER);
            PokemonInPlay druddigon = board.active(board.them, Dragon.DRUDDIGON);

            new DealDamage(new Literal(30), new OpponentActive()).apply(board.contextFor(hitter));

            assertEquals(30, druddigon.damage());
            assertEquals(20, hitter.damage());
        }

        @Test
        @DisplayName("not from the Bench")
        void notFromBench() {
            PokemonInPlay hitter = board.active(board.you, HITTER);
            board.active(board.them, HITTER);
            PokemonInPlay druddigon = board.bench(board.them, Dragon.DRUDDIGON);

            new DealDamage(new Literal(30), new OpponentBenchSpecific(0)).apply(board.contextFor(hitter));

            assertEquals(30, druddigon.damage());
            assertEquals(0, hitter.damage());
        }

        @Test
        @DisplayName("damage nobody dealt, like Poison, is not answered")
        void ignoresSourcelessDamage() {
            PokemonInPlay hitter = board.active(board.you, HITTER);
            PokemonInPlay druddigon = board.active(board.them, Dragon.DRUDDIGON);

            new PlaceDamage(new Literal(10), new Self()).apply(board.contextFor(druddigon));

            assertEquals(0, hitter.damage());
        }
    }
}
