package com.tcgpocket.pool.A2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.MoveEnergy;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBenchSpecific;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;
import com.tcgpocket.trigger.TurnStart;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** One assertion per card that the tree does what the printed text says. */
class SpaceTimeSmackdownTest {

    @Nested
    @DisplayName("Grass — Torterra's Frenzy Plant")
    class FrenzyPlant {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay torterra = board.active(board.you, Grass.TORTERRA);
        private final PokemonInPlay turtwig = board.bench(board.you, Grass.TURTWIG);
        private final IAction frenzyPlant = Grass.TORTERRA.actions().getFirst();
        private final IAction bite = Grass.TURTWIG.actions().getFirst();

        @Test
        @DisplayName("locks Torterra on your next turn, but not the Pokémon that replaces it")
        void locksOnlyTorterraOnYourNextTurn() {
            board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            torterra.attachEnergy(Type.GRASS, 4);
            turtwig.attachEnergy(Type.GRASS, 2);

            assertTrue(frenzyPlant.execute(board.contextFor(torterra)).succeeded());

            board.battle.switchSides();
            board.battle.switchSides();
            assertFalse(frenzyPlant.isLegal(board.contextFor(torterra)),
                    "Frenzy Plant is locked on your next turn");

            assertTrue(new RetreatAction(new AttackerBenchSpecific(0))
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(turtwig, board.you.active().orElseThrow());
            assertTrue(bite.isLegal(board.contextFor(turtwig)), "Turtwig attacks in its place");
        }
    }

    @Nested
    @DisplayName("Water — Piloswine's Thick Fat")
    class ThickFat {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay piloswine = board.active(board.them, Water.PILOSWINE);

        @Test
        @DisplayName("takes 20 less from a Fire attacker")
        void reducesFire() {
            PokemonInPlay monferno = board.active(board.you, Fire.MONFERNO);
            Fire.MONFERNO.actions().getFirst().execute(board.contextFor(monferno));
            assertEquals(10, piloswine.damage());
        }

        @Test
        @DisplayName("takes full damage from a Grass attacker")
        void ignoresGrass() {
            PokemonInPlay turtwig = board.active(board.you, Grass.TURTWIG);
            Grass.TURTWIG.actions().getFirst().execute(board.contextFor(turtwig));
            assertEquals(30, piloswine.damage());
        }
    }

    @Nested
    @DisplayName("Water — Regice's Crystal Body")
    class CrystalBody {

        @Test
        @DisplayName("blocks the opponent's attack effects but not the damage")
        void blocksEffects() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay regice = board.active(board.them, Water.REGICE);
            PokemonInPlay glaceon = board.active(board.you, Water.GLACEON);
            TriggerDispatcher.dispatch(board.battle, new TurnStart(board.you));

            Water.GLACEON.actions().getFirst().execute(board.contextFor(glaceon));

            assertEquals(60, regice.damage());
            assertFalse(regice.hasStatus(new ParalysisStatus()));
        }
    }

    @Nested
    @DisplayName("Water — Manaphy's Oceanic Gift")
    class OceanicGift {

        @Test
        @DisplayName("gives a lone Benched Pokémon just 1 Water Energy")
        void capsAtBenchSize() {
            TestBoard board = new TestBoard();
            PokemonInPlay manaphy = board.active(board.you, Water.MANAPHY);
            PokemonInPlay benched = board.bench(board.you, Water.BUIZEL);

            assertTrue(Water.MANAPHY.actions().getFirst().execute(board.contextFor(manaphy)).succeeded());
            assertEquals(1, benched.energyOf(Type.WATER));
        }
    }

    @Nested
    @DisplayName("Psychic — Togekiss's Overdrive Smash")
    class OverdriveSmash {

        @Test
        @DisplayName("does +60 on your next turn")
        void boostsNextTurn() {
            TestBoard board = new TestBoard();
            PokemonInPlay togekiss = board.active(board.you, Psychic.TOGEKISS);
            PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            IAction smash = Psychic.TOGEKISS.actions().getFirst();

            smash.execute(board.contextFor(togekiss));
            board.battle.switchSides();
            board.battle.switchSides();
            smash.execute(board.contextFor(togekiss));

            assertEquals(60 + 120, wall.damage());
        }
    }

    @Nested
    @DisplayName("Psychic — Dusknoir's Shadow Void")
    class ShadowVoid {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay dusknoir = board.active(board.you, Psychic.DUSKNOIR);
        private final PokemonInPlay hurt = board.bench(board.you, Psychic.RALTS);
        private final UseAbilityAction shadowVoid =
                new UseAbilityAction(dusknoir, Psychic.DUSKNOIR.ability().orElseThrow());

        @Test
        @DisplayName("moves all of another Pokémon's damage onto Dusknoir")
        void movesDamage() {
            dusknoir.takeDamage(10);
            hurt.takeDamage(40);

            assertTrue(shadowVoid.execute(board.contextWithoutSource()).succeeded());
            assertEquals(0, hurt.damage());
            assertEquals(50, dusknoir.damage());
        }

        @Test
        @DisplayName("is not offered when only Dusknoir itself is damaged")
        void notForItself() {
            dusknoir.takeDamage(10);
            assertFalse(shadowVoid.isLegal(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("Psychic — Mesprit's Supreme Blast")
    class SupremeBlast {

        @Test
        @DisplayName("needs both Uxie and Azelf on the Bench")
        void needsTheTrio() {
            TestBoard board = new TestBoard();
            PokemonInPlay mesprit = board.active(board.you, Psychic.MESPRIT);
            mesprit.attachEnergy(Type.PSYCHIC, 3);
            IAction blast = Psychic.MESPRIT.actions().getFirst();

            board.bench(board.you, Psychic.UXIE);
            assertFalse(blast.isLegal(board.contextFor(mesprit)));

            board.bench(board.you, Psychic.AZELF);
            assertTrue(blast.isLegal(board.contextFor(mesprit)));
        }
    }

    @Nested
    @DisplayName("Psychic — Giratina's Levitate")
    class Levitate {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay giratina = board.active(board.you, Psychic.GIRATINA);
        private final RetreatAction retreat = new RetreatAction(new AttackerBenchSpecific(0));

        @Test
        @DisplayName("retreats for free with an Energy attached, keeping the Energy")
        void freeWithEnergy() {
            board.bench(board.you, Psychic.RALTS);
            giratina.attachEnergy(Type.PSYCHIC, 1);

            assertTrue(retreat.execute(board.contextWithoutSource()).succeeded());
            assertEquals(1, giratina.energyOf(Type.PSYCHIC));
        }

        @Test
        @DisplayName("cannot retreat with no Energy")
        void notWithout() {
            board.bench(board.you, Psychic.RALTS);
            assertFalse(retreat.isLegal(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("Fighting — Rhyperior's Mountain Swing")
    class MountainSwing {

        @Test
        @DisplayName("discards the top 3 cards of your deck, or what there is")
        void discardsTopThree() {
            TestBoard board = new TestBoard();
            PokemonInPlay rhyperior = board.active(board.you, Fighting.RHYPERIOR);
            board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            board.inDeck(board.you, Fighting.RIOLU);
            board.inDeck(board.you, Fighting.RIOLU);

            Fighting.RHYPERIOR.actions().getFirst().execute(board.contextFor(rhyperior));

            assertTrue(board.you.deck().isEmpty());
            assertEquals(2, board.you.discardPile().size());
        }
    }

    @Nested
    @DisplayName("Fighting — Rampardos's Head Smash")
    class HeadSmash {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay rampardos = board.active(board.you, Fighting.RAMPARDOS);
        private final IAction headSmash = Fighting.RAMPARDOS.actions().getFirst();

        @Test
        @DisplayName("does 50 to itself when it Knocks Out the opponent's Pokémon")
        void recoilOnKnockout() {
            board.active(board.them, TestBoard.card("Small", 100, Type.COLORLESS));
            headSmash.execute(board.contextFor(rampardos));
            assertEquals(50, rampardos.damage());
        }

        @Test
        @DisplayName("does nothing to itself otherwise")
        void noRecoilOtherwise() {
            board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            headSmash.execute(board.contextFor(rampardos));
            assertEquals(0, rampardos.damage());
        }
    }

    @Nested
    @DisplayName("Fighting — Lucario's Fighting Coach")
    class FightingCoach {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));

        @Test
        @DisplayName("gives your Fighting Pokémon +20, once per Lucario")
        void stacksPerLucario() {
            PokemonInPlay riolu = board.active(board.you, Fighting.RIOLU);
            board.bench(board.you, Fighting.LUCARIO);
            board.bench(board.you, Fighting.LUCARIO);

            Fighting.RIOLU.actions().getFirst().execute(board.contextFor(riolu));

            assertEquals(20 + 40, wall.damage());
        }

        @Test
        @DisplayName("does nothing for a Pokémon of another type")
        void onlyFighting() {
            PokemonInPlay kirlia = board.active(board.you, Psychic.KIRLIA);
            board.bench(board.you, Fighting.LUCARIO);

            Psychic.KIRLIA.actions().getFirst().execute(board.contextFor(kirlia));

            assertEquals(20, wall.damage());
        }
    }

    @Nested
    @DisplayName("Darkness — Toxicroak's Group Beatdown")
    class GroupBeatdown {

        @Test
        @DisplayName("flips once for each of your Pokémon in play")
        void flipsPerPokemon() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay toxicroak = board.active(board.you, Darkness.TOXICROAK);
            board.bench(board.you, Darkness.CROAGUNK);
            board.bench(board.you, Darkness.CROAGUNK);
            PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));

            Darkness.TOXICROAK.actions().getFirst().execute(board.contextFor(toxicroak));

            assertEquals(3 * 40, wall.damage());
        }
    }

    @Nested
    @DisplayName("Darkness — Darkrai ex's Nightmare Aura")
    class NightmareAura {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay darkrai = board.active(board.you, Darkness.DARKRAI_EX);
        private final PokemonInPlay opponent = board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));

        @Test
        @DisplayName("does 20 to the opponent's Active for each Darkness Energy from the Energy Zone")
        void firesFromTheZone() {
            new AttachEnergy(Type.DARKNESS, new Literal(2), new Self()).apply(board.contextFor(darkrai));
            assertEquals(40, opponent.damage());
        }

        @Test
        @DisplayName("ignores other types, and Energy moved from another Pokémon")
        void ignoresOthers() {
            new AttachEnergy(Type.GRASS, new Self()).apply(board.contextFor(darkrai));
            PokemonInPlay benched = board.bench(board.you, Darkness.MURKROW);
            benched.attachEnergy(Type.DARKNESS, 1);
            new MoveEnergy(new AttackerBenchSpecific(0), new AttackerActive(), new Literal(1))
                    .apply(board.contextWithoutSource());

            assertEquals(1, darkrai.energyOf(Type.DARKNESS));
            assertEquals(0, opponent.damage());
        }
    }

    @Nested
    @DisplayName("Metal — Bastiodon's Guarded Grill")
    class GuardedGrill {

        private int heavyHitInto(ScriptedRandom coin) {
            TestBoard board = new TestBoard(coin);
            PokemonInPlay heatran = board.active(board.you, Metal.HEATRAN);
            PokemonInPlay bastiodon = board.active(board.them, Metal.BASTIODON);
            Metal.HEATRAN.actions().getFirst().execute(board.contextFor(heatran));
            return bastiodon.damage();
        }

        @Test
        @DisplayName("heads takes 100 less from the attack")
        void headsReduces() {
            assertEquals(10, heavyHitInto(ScriptedRandom.alwaysHeads()));
        }

        @Test
        @DisplayName("tails takes it all")
        void tailsDoesNothing() {
            assertEquals(110, heavyHitInto(ScriptedRandom.alwaysTails()));
        }

        @Test
        @DisplayName("flips only for an attack that would damage it")
        void noFlipWithoutDamage() {
            TestBoard board = new TestBoard(ScriptedRandom.flipping(false, true));
            PokemonInPlay bastiodon = board.active(board.them, Metal.BASTIODON);
            PokemonInPlay piplup = board.active(board.you, Water.PIPLUP);
            Water.PIPLUP.actions().getFirst().execute(board.contextFor(piplup));

            PokemonInPlay heatran = board.active(board.you, Metal.HEATRAN);
            Metal.HEATRAN.actions().getFirst().execute(board.contextFor(heatran));

            assertEquals(110, bastiodon.damage(), "Nap did not use up the tails");
        }
    }

    @Nested
    @DisplayName("Dragon — Garchomp's Reckless Shearing")
    class RecklessShearing {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay garchomp = board.active(board.you, Dragon.GARCHOMP);
        private final UseAbilityAction shearing =
                new UseAbilityAction(garchomp, Dragon.GARCHOMP.ability().orElseThrow());

        @Test
        @DisplayName("discards a card from hand, then draws one")
        void discardsThenDraws() {
            board.inHand(board.you, Dragon.GIBLE);
            board.inDeck(board.you, Dragon.GABITE);

            assertTrue(shearing.execute(board.contextWithoutSource()).succeeded());
            assertEquals(Dragon.GABITE, board.you.hand().getFirst().definition());
            assertEquals(1, board.you.discardPile().size());
        }

        @Test
        @DisplayName("can't be used with an empty hand")
        void needsACardToDiscard() {
            board.inDeck(board.you, Dragon.GABITE);

            assertFalse(shearing.isLegal(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("Colorless")
    class ColorlessCards {

        @Test
        @DisplayName("Starly's Pluck discards the Tool before the damage")
        void pluckDiscardsTool() {
            TestBoard board = new TestBoard();
            PokemonInPlay starly = board.active(board.you, Colorless.STARLY);
            PokemonInPlay target = board.active(board.them, Colorless.EEVEE);
            target.attachTool(board.loose(board.them, ToolCard.named("giant-cape", "Giant Cape")));

            assertTrue(Colorless.STARLY.actions().getFirst().execute(board.contextFor(starly)).succeeded());
            assertTrue(target.tool().isEmpty());
            assertEquals(1, board.them.discardPile().size());
            assertEquals(20, target.damage());
        }

        @Test
        @DisplayName("Porygon-Z's Buggy Beam rerolls the opponent's next Energy")
        void buggyBeamChangesNextEnergy() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay porygonZ = board.active(board.you, Colorless.PORYGON_Z);
            board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            porygonZ.attachEnergy(Type.COLORLESS, 3);
            board.them.registerTypes(Type.WATER);
            board.them.generateEnergy(board.battle.rng());

            assertTrue(Colorless.PORYGON_Z.actions().getFirst().execute(board.contextFor(porygonZ)).succeeded());
            assertEquals(Type.GRASS, board.them.nextEnergy().orElseThrow(), "the scripted roll picks the first listed type");
        }

        @Test
        @DisplayName("Bidoof's Super Fang leaves half the remaining HP, rounded down to 10")
        void superFangHalvesRemainingHp() {
            TestBoard board = new TestBoard();
            PokemonInPlay bidoof = board.active(board.you, Colorless.BIDOOF);
            PokemonInPlay target = board.active(board.them, TestBoard.card("Wall", 100, Type.COLORLESS));
            target.takeDamage(30);
            bidoof.attachEnergy(Type.COLORLESS, 2);

            assertTrue(Colorless.BIDOOF.actions().getFirst().execute(board.contextFor(bidoof)).succeeded());
            assertEquals(30, target.currentHp());
        }

        @Test
        @DisplayName("Purugly's Interrupt shuffles the card you choose into their deck")
        void interruptShufflesChosenCard() {
            TestBoard board = new TestBoard(new ScriptedPlayer("you", 1), new ScriptedPlayer("them"));
            PokemonInPlay purugly = board.active(board.you, Colorless.PURUGLY);
            board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            board.inHand(board.them, Colorless.EEVEE);
            board.inHand(board.them, Colorless.AIPOM);
            purugly.attachEnergy(Type.COLORLESS, 3);

            assertTrue(Colorless.PURUGLY.actions().getFirst().execute(board.contextFor(purugly)).succeeded());
            assertEquals(Colorless.EEVEE, board.them.hand().getFirst().definition());
            assertEquals(Colorless.AIPOM, board.them.deck().getFirst().definition());
        }
    }

    @Nested
    @DisplayName("Trainers")
    class TrainerCards {

        @Test
        @DisplayName("Giant Cape is played onto a Pokémon without a Tool and adds 20 HP")
        void giantCapeAddsHp() {
            TestBoard board = new TestBoard();
            PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
            PokemonInPlay aipom = board.bench(board.you, Colorless.AIPOM);
            aipom.attachTool(board.loose(board.you, Trainers.ROCKY_HELMET));
            CardInstance cape = board.inHand(board.you, Trainers.GIANT_CAPE);

            List<PlayCardAction> moves = PlayCardAction.all(cape, board.contextWithoutSource());
            assertEquals(List.of(Optional.of(eevee)), moves.stream().map(PlayCardAction::onto).toList(),
                    "Aipom already holds a Tool");

            assertTrue(moves.getFirst().execute(board.contextWithoutSource()).succeeded());
            assertEquals(80, eevee.maxHp());
            assertFalse(board.you.hand().contains(cape));
            assertTrue(board.you.discardPile().isEmpty(), "the Tool stays attached, not discarded");
        }

        @Test
        @DisplayName("Rocky Helmet does 20 back to the attacker")
        void rockyHelmetStrikesBack() {
            TestBoard board = new TestBoard();
            PokemonInPlay aipom = board.active(board.you, Colorless.AIPOM);
            PokemonInPlay wearer = board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            wearer.attachTool(board.loose(board.them, Trainers.ROCKY_HELMET));
            aipom.attachEnergy(Type.COLORLESS, 1);

            assertTrue(Colorless.AIPOM.actions().getFirst().execute(board.contextFor(aipom)).succeeded());
            assertEquals(20, aipom.damage());
        }

        @Test
        @DisplayName("Lum Berry cures every status at turn end and is discarded")
        void lumBerryCuresAndGoes() {
            TestBoard board = new TestBoard();
            PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
            eevee.attachTool(board.loose(board.you, Trainers.LUM_BERRY));
            eevee.addStatus(new PoisonStatus());
            eevee.addStatus(new ParalysisStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertTrue(eevee.statuses().isEmpty());
            assertFalse(eevee.hasTool());
            assertEquals(Trainers.LUM_BERRY, board.you.discardPile().getFirst().definition());
        }

        @Test
        @DisplayName("Volkner attaches Lightning Energy that an attack discarded")
        void volknerReattachesDiscardedEnergy() {
            TestBoard board = new TestBoard();
            PokemonInPlay luxray = board.active(board.you, Lightning.LUXRAY);
            board.active(board.them, TestBoard.card("Wall", 500, Type.COLORLESS));
            luxray.attachEnergy(Type.LIGHTNING, 3);
            assertTrue(Lightning.LUXRAY.actions().getFirst().execute(board.contextFor(luxray)).succeeded());
            assertEquals(3, board.you.discardedEnergy().get(Type.LIGHTNING));

            CardInstance volkner = board.inHand(board.you, Trainers.VOLKNER);
            assertTrue(new PlayCardAction(volkner, luxray).execute(board.contextWithoutSource()).succeeded());
            assertEquals(2, luxray.energyOf(Type.LIGHTNING));
            assertEquals(1, board.you.discardedEnergy().get(Type.LIGHTNING));
        }

        @Test
        @DisplayName("moved Energy does not land in the discard pile")
        void movedEnergyIsNotDiscarded() {
            TestBoard board = new TestBoard();
            PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
            PokemonInPlay aipom = board.bench(board.you, Colorless.AIPOM);
            aipom.attachEnergy(Type.WATER, 1);

            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.DAWN))
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(1, eevee.energyOf(Type.WATER));
            assertTrue(board.you.discardedEnergy().isEmpty());
        }

        @Test
        @DisplayName("Dawn lets you choose which type of Energy moves")
        void dawnChoosesTheType() {
            ScriptedPlayer picking = new ScriptedPlayer("you", 1);
            TestBoard board = new TestBoard(picking, new ScriptedPlayer("them"));
            PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
            PokemonInPlay aipom = board.bench(board.you, Colorless.AIPOM);
            aipom.attachEnergy(Type.FIRE, 1);
            aipom.attachEnergy(Type.WATER, 1);

            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.DAWN))
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(1, eevee.energyOf(Type.WATER));
            assertEquals(1, aipom.energyOf(Type.FIRE));
            assertEquals(0, picking.remaining(), "asked exactly once");
        }

        @Test
        @DisplayName("Pokémon Communication swaps a Pokémon in hand for one in the deck")
        void communicationSwaps() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            board.active(board.you, Colorless.EEVEE);
            CardInstance gible = board.inHand(board.you, Dragon.GIBLE);
            CardInstance gabite = board.inDeck(board.you, Dragon.GABITE);

            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.POKEMON_COMMUNICATION))
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(List.of(gabite), board.you.hand());
            assertEquals(List.of(gible), board.you.deck());
        }

        @Test
        @DisplayName("Cyrus can't be played without a damaged Benched Pokémon")
        void cyrusNeedsDamagedBench() {
            TestBoard board = new TestBoard();
            board.active(board.you, Colorless.EEVEE);
            board.active(board.them, Colorless.AIPOM);
            PokemonInPlay benched = board.bench(board.them, Colorless.STARLY);
            CardInstance cyrus = board.inHand(board.you, Trainers.CYRUS);

            assertFalse(new PlayCardAction(cyrus).isLegal(board.contextWithoutSource()));
            benched.takeDamage(10);
            assertTrue(new PlayCardAction(cyrus).execute(board.contextWithoutSource()).succeeded());
            assertEquals(benched, board.them.active().orElseThrow());
        }

        @Test
        @DisplayName("Mars leaves the opponent one card per point they still need")
        void marsRedrawsByPoints() {
            TestBoard board = new TestBoard();
            board.active(board.you, Colorless.EEVEE);
            board.inHand(board.them, Colorless.AIPOM);
            for (int i = 0; i < 5; i++) {
                board.inDeck(board.them, Colorless.STARLY);
            }
            board.them.awardPoints(1);

            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.MARS))
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(2, board.them.hand().size());
        }
    }
}
