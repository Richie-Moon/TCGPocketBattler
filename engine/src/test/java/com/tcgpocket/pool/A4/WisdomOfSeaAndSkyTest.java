package com.tcgpocket.pool.A4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.Action;
import com.tcgpocket.action.EvolveAction;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.IsAsleep;
import com.tcgpocket.condition.IsPoisoned;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DistributeMixedEnergy.EnergyPlacement;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.MoveEnergy;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.energy.Type;
import com.tcgpocket.engine.TurnEngine;
import com.tcgpocket.number.Literal;
import com.tcgpocket.player.Decision;
import com.tcgpocket.player.IPlayer;
import com.tcgpocket.player.RandomPlayer;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.resolve.SeededRandom;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** One assertion per card that needed a new node, or whose tree is not obvious. */
class WisdomOfSeaAndSkyTest {

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

    @Test
    @DisplayName("A4-001 to A4-241 are in the pool")
    void setIsInThePool() {
        assertEquals(241, WisdomOfSeaAndSky.CARDS.size());
        assertEquals("A4-001", WisdomOfSeaAndSky.CARDS.get(0).id());
        assertEquals("A4-201", WisdomOfSeaAndSky.CARDS.get(240).id());
        assertEquals(CardRarity.SPECIAL_ILLUSTRATION_RARE, CardPool.get("A4-209").rarity());
        assertEquals(CardRarity.IMMERSIVE, CardPool.get("A4-211").rarity());
        assertEquals(CardRarity.SHINY_RARE, CardPool.get("A4-231").rarity());
        assertEquals(CardRarity.SHINY_ULTRA_RARE, CardPool.get("A4-239").rarity());
        assertEquals(CardRarity.HYPER_RARE, CardPool.get("A4-241").rarity());
        assertEquals("Nidoran♀", CardPool.get("A4-223").name());
        assertEquals("Nidoran♂", CardPool.get("A4-226").name());
        assertEquals(CardRarity.ILLUSTRATION_RARE, CardPool.get("A4-162").rarity());
        assertEquals(CardRarity.ILLUSTRATION_RARE, CardPool.get("A4-185").rarity());
        assertEquals(CardRarity.ULTRA_RARE, CardPool.get("A4-186").rarity());
        assertEquals(CardRarity.ULTRA_RARE, CardPool.get("A4-201").rarity());
        assertEquals("Blissey", CardPool.get("A4-185").name());
        assertEquals("Hiker", CardPool.get("A4-201").name());
        assertEquals("Ho-Oh ex", CardPool.get("A4-034").name());
    }

    @Test
    @DisplayName("Baby Pokémon have a free attack, no Retreat Cost and no Weakness")
    void babies() {
        for (ICard card : List.of(Fire.MAGBY, Lightning.PICHU, Lightning.ELEKID, Psychic.SMOOCHUM, Psychic.CLEFFA, Fighting.TYROGUE)) {
            PokemonCard baby = (PokemonCard) card;
            assertTrue(baby.retreatCost().isFree(), baby.name());
            assertTrue(baby.weakness().isEmpty(), baby.name());
            assertTrue(((Action) baby.actions().get(0)).cost().isFree(), baby.name());
        }
    }

    @Test
    @DisplayName("A Baby Pokémon can attack with no Energy attached")
    void babyAttacksForFree() {
        PokemonInPlay elekid = board.active(board.you, Lightning.ELEKID);
        board.active(board.them, BIG_TARGET);
        PokemonInPlay benched = board.bench(board.them, Grass.ODDISH);

        assertTrue(attack(elekid).isLegal(board.contextFor(elekid)));
        attack(elekid).execute(board.contextFor(elekid));

        assertEquals(20, benched.damage());
    }

    @Test
    @DisplayName("Grass Knot counts the current Retreat Cost, so a free retreat adds nothing")
    void grassKnot() {
        PokemonInPlay tangrowth = board.active(board.you, Grass.TANGROWTH);
        PokemonInPlay target = board.active(board.them, Water.FERALIGATR);

        attack(tangrowth).execute(board.contextFor(tangrowth));
        assertEquals(10 + 3 * 40, target.damage());

        board.bench(board.them, Grass.JUMPLUFF);
        attack(tangrowth).execute(board.contextFor(tangrowth));
        assertEquals(10 + 3 * 40 + 10, target.damage());
    }

    @Test
    @DisplayName("Fluffy Flight frees your Active wherever Jumpluff sits")
    void fluffyFlight() {
        board.active(board.you, Grass.TANGROWTH);
        PokemonInPlay jumpluff = board.bench(board.you, Grass.JUMPLUFF);

        assertTrue(new RetreatAction(new Self()).isLegal(board.contextFor(jumpluff)));
    }

    @Test
    @DisplayName("En-fruits-iastic takes off the Grass cost only while Cherubi holds a Tool")
    void enFruitsIastic() {
        PokemonInPlay cherubi = board.active(board.you, Grass.CHERUBI);
        assertFalse(attack(cherubi).isLegal(board.contextFor(cherubi)));

        cherubi.attachTool(board.loose(board.you, (ToolCard) CardPool.get("A2-148")));

        assertTrue(attack(cherubi).isLegal(board.contextFor(cherubi)));
    }

    @Test
    @DisplayName("Spurt Fire never hits Magcargo itself")
    void spurtFire() {
        PokemonInPlay magcargo = board.active(board.you, Fire.MAGCARGO);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);

        attack(magcargo).execute(board.contextFor(magcargo));

        assertEquals(0, magcargo.damage());
        assertEquals(3 * 50, target.damage());
    }

    @Test
    @DisplayName("Phoenix Turbo offers every placement of the three Energy as one decision")
    void phoenixTurbo() {
        Recording you = new Recording(1);
        TestBoard board = boardFor(you);
        PokemonInPlay hoOh = board.active(board.you, Fire.HO_OH_EX);
        board.active(board.them, BIG_TARGET);
        PokemonInPlay first = board.bench(board.you, Grass.ODDISH);
        PokemonInPlay second = board.bench(board.you, Grass.CHIKORITA);
        board.bench(board.you, Grass.GLOOM);

        attack(hoOh).execute(board.contextFor(hoOh));

        assertEquals(1, you.decisions.size());
        assertEquals(8, you.decisions.get(0).options().size(), "two Basics, three Energy");
        List<?> chosen = (List<?>) you.decisions.get(0).options().get(1);
        assertEquals(new EnergyPlacement(Type.LIGHTNING, second), chosen.get(2));
        assertEquals(1, first.energyOf(Type.FIRE));
        assertEquals(1, first.energyOf(Type.WATER));
        assertEquals(1, second.energyOf(Type.LIGHTNING));
    }

    @Test
    @DisplayName("Lordly Cheering boosts what evolves from Poliwhirl, only from the Bench")
    void lordlyCheering() {
        PokemonInPlay active = board.active(board.you, Water.POLITOED);
        PokemonInPlay defender = board.active(board.them, BIG_TARGET);
        Action hyperVoice = attack(active);

        TriggerDispatcher.dispatch(board.battle, new AttackDeclared(active, hyperVoice));
        assertEquals(0, board.you.attackBonusFor(active, defender, board.battle.turn()));

        board.bench(board.you, Water.POLITOED);
        TriggerDispatcher.dispatch(board.battle, new AttackDeclared(active, hyperVoice));
        assertEquals(40, board.you.attackBonusFor(active, defender, board.battle.turn()));
    }

    @Test
    @DisplayName("Wild Swing does 40 more per Benched Water Pokémon you discard, for no points")
    void wildSwingDiscards() {
        Recording you = new Recording(3);
        TestBoard board = boardFor(you);
        PokemonInPlay gyarados = board.active(board.you, Water.GYARADOS);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);
        board.bench(board.you, Water.POLIWAG);
        board.bench(board.you, Water.HORSEA);
        PokemonInPlay notWater = board.bench(board.you, Grass.ODDISH);

        attack(gyarados).execute(board.contextFor(gyarados));

        assertEquals(4, you.decisions.get(0).options().size(), "only the Water Pokémon are offered");
        assertEquals(20 + 2 * 40, target.damage());
        assertEquals(List.of(notWater), board.you.bench());
        assertEquals(0, board.them.points());
    }

    @Test
    @DisplayName("Wild Swing can decline, and then does its base damage")
    void wildSwingDeclined() {
        Recording you = new Recording(0);
        TestBoard board = boardFor(you);
        PokemonInPlay gyarados = board.active(board.you, Water.GYARADOS);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);
        board.bench(board.you, Water.POLIWAG);

        attack(gyarados).execute(board.contextFor(gyarados));

        assertEquals(20, target.damage());
        assertEquals(1, board.you.bench().size());
    }

    @Test
    @DisplayName("Octazooka outlasts the next turn and does not stack")
    void octazookaLasts() {
        TestBoard board = new TestBoard(ScriptedRandom.flipping(true));
        PokemonInPlay octillery = board.active(board.you, Water.OCTILLERY);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);

        attack(octillery).execute(board.contextFor(octillery));
        attack(octillery).execute(board.contextFor(octillery));
        for (int i = 0; i < 20; i++) {
            board.battle.switchSides();
        }

        assertEquals(2, target.modifiers().size());
        assertTrue(target.hasModifier(com.tcgpocket.state.ModifierKind.ATTACK_NEEDS_HEADS, board.battle.turn()));
    }

    @Test
    @DisplayName("Unruly Claw offers the discard on evolving, and asks nothing if there is no Energy")
    void unrulyClaw() {
        assertEquals(0, evolveCrawdaunt(new ScriptedPlayer("you", 0), 1));
        assertEquals(1, evolveCrawdaunt(new ScriptedPlayer("you", 1), 1));
        Recording you = new Recording(0);
        assertEquals(0, evolveCrawdaunt(you, 0));
        assertTrue(you.decisions.isEmpty());
    }

    /** The Energy left on the opponent's Active after evolving into Crawdaunt. */
    private static int evolveCrawdaunt(IPlayer you, int energy) {
        TestBoard board = boardFor(you);
        PokemonInPlay corphish = board.active(board.you, Water.CORPHISH);
        corphish.setTurnPlayed(board.battle.turn() - 2);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);
        target.attachEnergy(Type.GRASS, energy);
        CardInstance crawdaunt = board.inHand(board.you, Water.CRAWDAUNT);

        new EvolveAction(crawdaunt, new AttackerActive()).execute(board.contextFor(corphish));

        return target.totalEnergy();
    }

    @Test
    @DisplayName("Feathery Cyclone moves every Energy, of every type, asking only where")
    void featheryCyclone() {
        Recording you = new Recording(0);
        TestBoard board = boardFor(you);
        PokemonInPlay swanna = board.active(board.you, Water.SWANNA);
        board.active(board.them, BIG_TARGET);
        PokemonInPlay bench = board.bench(board.you, Water.POLIWAG);
        swanna.attachEnergy(Type.WATER, 2);
        swanna.attachEnergy(Type.GRASS, 1);

        attack(swanna).execute(board.contextFor(swanna));

        assertEquals(0, swanna.totalEnergy());
        assertEquals(2, bench.energyOf(Type.WATER));
        assertEquals(1, bench.energyOf(Type.GRASS));
        assertTrue(you.decisions.isEmpty(), "a bench of one is not worth asking about");
    }

    @Test
    @DisplayName("Flashing Signal paralyzes on heads and confuses on tails")
    void flashingSignal() {
        for (boolean heads : List.of(true, false)) {
            TestBoard board = new TestBoard(ScriptedRandom.flipping(heads));
            PokemonInPlay lanturn = board.active(board.you, Lightning.LANTURN_EX);
            PokemonInPlay target = board.active(board.them, BIG_TARGET);

            attack(lanturn).execute(board.contextFor(lanturn));

            assertEquals(heads, new com.tcgpocket.condition.IsParalyzed().evaluate(target));
            assertEquals(!heads, new com.tcgpocket.condition.IsConfused().evaluate(target));
        }
    }

    @Test
    @DisplayName("Rest sleeps and heals itself")
    void rest() {
        PokemonInPlay slowpoke = board.active(board.you, Psychic.SLOWPOKE);
        slowpoke.takeDamage(40);

        attack(slowpoke).execute(board.contextFor(slowpoke));

        assertEquals(10, slowpoke.damage());
        assertTrue(new IsAsleep().evaluate(slowpoke));
    }

    @Test
    @DisplayName("Dream Dance sleeps both Actives")
    void dreamDance() {
        PokemonInPlay musharna = board.active(board.you, Psychic.MUSHARNA);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);

        attack(musharna).execute(board.contextFor(musharna));

        assertTrue(new IsAsleep().evaluate(musharna));
        assertTrue(new IsAsleep().evaluate(target));
    }

    @Test
    @DisplayName("Life Drain leaves 10 HP on heads, and never heals")
    void lifeDrain() {
        TestBoard board = new TestBoard(ScriptedRandom.flipping(true));
        PokemonInPlay xatu = board.active(board.you, Psychic.XATU);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);

        attack(xatu).execute(board.contextFor(xatu));
        assertEquals(10, target.currentHp());

        target.takeDamage(10);
        attack(xatu).execute(board.contextFor(xatu));
        assertEquals(0, target.currentHp());
    }

    @Test
    @DisplayName("Celestial Blessing prevents the damage on heads only")
    void celestialBlessing() {
        for (boolean heads : List.of(true, false)) {
            TestBoard board = new TestBoard(ScriptedRandom.flipping(heads));
            PokemonInPlay attacker = board.active(board.you, Fighting.GLISCOR);
            PokemonInPlay togekiss = board.active(board.them, Psychic.TOGEKISS);

            attack(attacker).execute(board.contextFor(attacker));

            assertEquals(heads ? 0 : 60, togekiss.damage());
        }
    }

    @Test
    @DisplayName("Unown GUARD needs another Unown with a different Ability, which may be on the Bench")
    void unownGuard() {
        PokemonInPlay attacker = board.active(board.you, Fighting.GLISCOR);
        PokemonInPlay guard = board.active(board.them, Psychic.UNOWN_GUARD);
        board.bench(board.them, Psychic.UNOWN_GUARD);

        attack(attacker).execute(board.contextFor(attacker));
        assertEquals(60, guard.damage());

        guard.heal(60);
        board.bench(board.them, Psychic.UNOWN_POWER);
        attack(attacker).execute(board.contextFor(attacker));
        assertEquals(40, guard.damage());
    }

    @Test
    @DisplayName("Unown POWER needs another Unown with a different Ability, and helps any attacker")
    void unownPower() {
        PokemonInPlay attacker = board.active(board.you, Fighting.GLISCOR);
        PokemonInPlay target = board.active(board.them, Psychic.GRANBULL);
        board.bench(board.you, Psychic.UNOWN_POWER);

        attack(attacker).execute(board.contextFor(attacker));
        assertEquals(60, target.damage());

        target.heal(60);
        board.bench(board.you, Psychic.UNOWN_GUARD);
        attack(attacker).execute(board.contextFor(attacker));
        assertEquals(70, target.damage());
    }

    @Test
    @DisplayName("Reply Strongly needs attack damage taken in the Active Spot last turn")
    void replyStrongly() {
        PokemonInPlay wobbuffet = board.active(board.you, Psychic.WOBBUFFET);
        PokemonInPlay target = board.active(board.them, Psychic.GRANBULL);
        int lastTurn = board.battle.turn() - 1;

        attack(wobbuffet).execute(board.contextFor(wobbuffet));
        assertEquals(30, target.damage());

        target.heal(30);
        wobbuffet.markDamagedByAttackWhileActive(lastTurn);
        attack(wobbuffet).execute(board.contextFor(wobbuffet));
        assertEquals(80, target.damage());
    }

    @Test
    @DisplayName("Only attack damage that lands in the Active Spot stamps the Pokémon")
    void attackDamageStampsTheActive() {
        PokemonInPlay hitmontop = board.active(board.you, Fighting.HITMONTOP);
        PokemonInPlay active = board.active(board.them, Psychic.GRANBULL);
        PokemonInPlay benched = board.bench(board.them, Psychic.WOBBUFFET);
        int turn = board.battle.turn();

        attack(hitmontop).execute(board.contextFor(hitmontop));

        assertEquals(20, benched.damage());
        assertTrue(active.damagedByAttackWhileActiveOn(turn));
        assertFalse(benched.damagedByAttackWhileActiveOn(turn));
    }

    @Test
    @DisplayName("Dark Chase only offers a damaged Benched Pokémon, and only from the Active Spot")
    void darkChase() {
        TestBoard board = new TestBoard(new Recording(0), new Recording(0));
        PokemonInPlay umbreon = board.active(board.you, Darkness.UMBREON_EX);
        board.active(board.them, Psychic.GRANBULL);
        board.bench(board.them, Psychic.WOBBUFFET);
        PokemonInPlay damaged = board.bench(board.them, Psychic.SNUBBULL);
        ActivatedAbility ability = (ActivatedAbility) umbreon.definition().ability().orElseThrow();

        assertFalse(ability.usableWhen().evaluate(board.contextFor(umbreon)));

        damaged.takeDamage(10);
        assertTrue(ability.usableWhen().evaluate(board.contextFor(umbreon)));

        ability.action().execute(board.contextFor(umbreon));
        assertEquals(damaged, board.them.active().orElseThrow());
    }

    @Test
    @DisplayName("Energy Plunder gathers only Darkness Energy from the rest of your side")
    void energyPlunder() {
        PokemonInPlay tyranitar = board.bench(board.you, Darkness.TYRANITAR);
        PokemonInPlay active = board.active(board.you, Darkness.ZUBAT);
        ActivatedAbility ability = (ActivatedAbility) tyranitar.definition().ability().orElseThrow();
        tyranitar.attachEnergy(Type.DARKNESS, 1);

        assertFalse(ability.usableWhen().evaluate(board.contextFor(tyranitar)));

        active.attachEnergy(Type.DARKNESS, 2);
        active.attachEnergy(Type.GRASS, 1);
        ability.action().execute(board.contextFor(tyranitar));

        assertEquals(3, tyranitar.energyOf(Type.DARKNESS));
        assertEquals(0, active.energyOf(Type.DARKNESS));
        assertEquals(1, active.energyOf(Type.GRASS));
    }

    @Test
    @DisplayName("Gear Spinner does +70 on the attacker's next use")
    void gearSpinner() {
        PokemonInPlay klinklang = board.active(board.you, Metal.KLINKLANG);
        PokemonInPlay target = board.active(board.them, Fighting.DONPHAN_EX);

        attack(klinklang).execute(board.contextFor(klinklang));
        assertEquals(70, target.damage());

        target.heal(70);
        attack(klinklang).execute(board.contextFor(klinklang));
        assertEquals(140, target.damage());
    }

    private static void play(ITrainerCard card, ResolutionContext context) {
        ((IAction) card.actions().get(0)).execute(context);
    }

    @Test
    @DisplayName("Buggy Evolution fires on Energy from the zone, not on moved Energy")
    void buggyEvolution() {
        TestBoard board = new TestBoard(new Recording(0), new Recording(0));
        PokemonInPlay porygon2 = board.active(board.you, Colorless.PORYGON2);
        board.bench(board.you, Colorless.SPEAROW).attachEnergy(Type.FIRE, 1);
        board.inDeck(board.you, Fire.MAGBY);
        board.inDeck(board.you, Colorless.PORYGON_Z);

        new MoveEnergy(new ChosenFrom(new AttackerBench(), new AttackerSide(), "Choose"), new Self(), new Literal(1))
                .apply(board.contextFor(porygon2));
        assertEquals("Porygon2", porygon2.definition().name());

        new AttachEnergy(Type.FIRE, new Literal(1), new Self()).apply(board.contextFor(porygon2));
        assertEquals("Porygon-Z", porygon2.definition().name());
        assertEquals(1, board.you.deck().size());
    }

    @Test
    @DisplayName("Insomnia refuses and clears Asleep only")
    void insomnia() {
        PokemonInPlay hoothoot = board.active(board.you, Colorless.HOOTHOOT);

        new AddStatus(new SleepStatus(), new Self()).apply(board.contextFor(hoothoot));
        new AddStatus(new PoisonStatus(), new Self()).apply(board.contextFor(hoothoot));
        assertFalse(new IsAsleep().evaluate(hoothoot));
        assertTrue(new IsPoisoned().evaluate(hoothoot));

        hoothoot.addStatus(new SleepStatus());
        board.you.recoverProtected();
        assertFalse(new IsAsleep().evaluate(hoothoot));
        assertTrue(new IsPoisoned().evaluate(hoothoot));
    }

    @Test
    @DisplayName("Imitate draws up to the size of the opponent's hand, and no further")
    void imitate() {
        PokemonInPlay aipom = board.active(board.you, Colorless.AIPOM);
        board.active(board.them, BIG_TARGET);
        for (int i = 0; i < 5; i++) {
            board.inDeck(board.you, Fire.MAGBY);
        }
        board.inHand(board.you, Fire.MAGBY);
        board.inHand(board.them, Fire.MAGBY);
        board.inHand(board.them, Fire.MAGBY);
        board.inHand(board.them, Fire.MAGBY);

        attack(aipom).execute(board.contextFor(aipom));
        assertEquals(3, board.you.hand().size());

        attack(aipom).execute(board.contextFor(aipom));
        assertEquals(3, board.you.hand().size());
    }

    @Test
    @DisplayName("Elemental Blast discards one each of its three types and keeps the rest")
    void elementalBlast() {
        PokemonInPlay lugia = board.active(board.you, Colorless.LUGIA_EX);
        board.active(board.them, BIG_TARGET);
        lugia.attachEnergy(Type.FIRE, 2);
        lugia.attachEnergy(Type.WATER, 1);
        lugia.attachEnergy(Type.LIGHTNING, 1);

        attack(lugia).execute(board.contextFor(lugia));

        assertEquals(1, lugia.totalEnergy());
        assertEquals(1, lugia.energyOf(Type.FIRE));
    }

    @Test
    @DisplayName("Splatter Coating changes one Energy without changing the count")
    void splatterCoating() {
        PokemonInPlay smeargle = board.active(board.you, Colorless.SMEARGLE);
        PokemonInPlay target = board.active(board.them, BIG_TARGET);

        attack(smeargle).execute(board.contextFor(smeargle));
        assertEquals(0, target.totalEnergy());

        target.attachEnergy(Type.GRASS, 3);
        attack(smeargle).execute(board.contextFor(smeargle));
        assertEquals(3, target.totalEnergy());
    }

    @Test
    @DisplayName("Elemental Switch moves only a listed type")
    void elementalSwitch() {
        TestBoard board = new TestBoard(new Recording(0), new Recording(0));
        PokemonInPlay active = board.active(board.you, Colorless.EEVEE);
        board.bench(board.you, Colorless.SPEAROW).attachEnergy(Type.GRASS, 1);
        PokemonInPlay source = board.bench(board.you, Colorless.PORYGON);
        source.attachEnergy(Type.GRASS, 1);
        source.attachEnergy(Type.WATER, 1);

        play(Trainers.ELEMENTAL_SWITCH, board.contextWithoutSource());

        assertEquals(1, active.energyOf(Type.WATER));
        assertEquals(0, active.energyOf(Type.GRASS));
        assertEquals(1, source.energyOf(Type.GRASS));
    }

    @Test
    @DisplayName("Steel Apron softens the opponent's attacks on a Metal holder, and stops Special Conditions")
    void steelApron() {
        PokemonInPlay attacker = board.active(board.you, Fighting.GLISCOR);
        PokemonInPlay klinklang = board.active(board.them, Metal.KLINKLANG);
        klinklang.attachTool(board.loose(board.them, Trainers.STEEL_APRON));

        attack(attacker).execute(board.contextFor(attacker));
        assertEquals(50, klinklang.damage());

        new AddStatus(new PoisonStatus(), new OpponentActive()).apply(board.contextFor(attacker));
        assertFalse(new IsPoisoned().evaluate(klinklang));
    }

    @Test
    @DisplayName("Steel Apron does nothing for a Pokémon that isn't Metal")
    void steelApronOnOtherTypes() {
        PokemonInPlay attacker = board.active(board.you, Fighting.GLISCOR);
        PokemonInPlay target = board.active(board.them, Psychic.GRANBULL);
        target.attachTool(board.loose(board.them, Trainers.STEEL_APRON));

        attack(attacker).execute(board.contextFor(attacker));
        assertEquals(60, target.damage());

        new AddStatus(new PoisonStatus(), new OpponentActive()).apply(board.contextFor(attacker));
        assertTrue(new IsPoisoned().evaluate(target));
    }

    @Test
    @DisplayName("Dark Pendant answers the opponent's attack, but not damage with no attacker")
    void darkPendant() {
        PokemonInPlay attacker = board.active(board.you, Fighting.GLISCOR);
        PokemonInPlay absol = board.active(board.them, Darkness.ABSOL);
        absol.attachTool(board.loose(board.them, Trainers.DARK_PENDANT));
        board.inHand(board.you, Fire.MAGBY);
        board.inHand(board.you, Fire.MAGBY);
        board.inHand(board.them, Fire.MAGBY);

        new PlaceDamage(new Literal(10), new OpponentActive()).apply(board.contextFor(attacker));
        assertEquals(2, board.you.hand().size(), "nobody dealt it");

        attack(attacker).execute(board.contextFor(attacker));
        assertEquals(1, board.you.hand().size());
        assertEquals(1, board.them.hand().size());
    }

    @Test
    @DisplayName("Dark Pendant ignores its holder's own recoil")
    void darkPendantRecoil() {
        PokemonInPlay absol = board.active(board.you, Darkness.ABSOL);
        board.active(board.them, BIG_TARGET);
        absol.attachTool(board.loose(board.you, Trainers.DARK_PENDANT));
        board.inHand(board.you, Fire.MAGBY);
        board.inHand(board.them, Fire.MAGBY);

        new DealDamage(new Literal(10), new Self()).apply(board.contextFor(absol));

        assertEquals(1, board.you.hand().size());
        assertEquals(1, board.them.hand().size());
    }

    @Test
    @DisplayName("Rescue Scarf returns the Pokémon when the opponent's attack Knocks it Out; the Tool and Energy are discarded")
    void rescueScarf() {
        PokemonInPlay attacker = board.active(board.you, Fighting.GLISCOR);
        PokemonInPlay target = board.active(board.them, Psychic.WOBBUFFET);
        board.bench(board.them, Psychic.SNUBBULL);
        target.attachTool(board.loose(board.them, Trainers.RESCUE_SCARF));
        target.attachEnergy(Type.PSYCHIC, 1);
        target.takeDamage(90);

        new TurnEngine(board.battle).checkKnockouts(Optional.of(attacker));

        assertEquals(List.of("Wobbuffet"), board.them.hand().stream().map(card -> card.definition().name()).toList());
        assertEquals(List.of("Rescue Scarf"), board.them.discardPile().stream().map(card -> card.definition().name()).toList());
        assertEquals(1, board.them.discardedEnergy().get(Type.PSYCHIC));
        assertEquals(1, board.you.points());
    }

    @Test
    @DisplayName("Rescue Scarf does nothing when its holder's own side did the damage")
    void rescueScarfNeedsTheOpponent() {
        PokemonInPlay holder = board.active(board.you, Psychic.WOBBUFFET);
        board.bench(board.you, Psychic.SNUBBULL);
        board.active(board.them, BIG_TARGET);
        holder.attachTool(board.loose(board.you, Trainers.RESCUE_SCARF));
        holder.takeDamage(90);

        new TurnEngine(board.battle).checkKnockouts(Optional.of(holder));

        assertTrue(board.you.hand().isEmpty());
        assertEquals(2, board.you.discardPile().size());
    }

    @Test
    @DisplayName("Will makes the first coin of the next card-effect flip heads, once")
    void will() {
        TestBoard board = new TestBoard(ScriptedRandom.flipping(false));
        PokemonInPlay binacle = board.active(board.you, Fighting.BINACLE);
        PokemonInPlay target = board.active(board.them, Psychic.GRANBULL);
        ResolutionContext context = board.contextFor(binacle);

        play(Trainers.WILL, context);
        FlipN.forSpecialCondition().apply(context);
        attack(binacle).execute(board.contextFor(binacle));
        assertEquals(30, target.damage(), "the Special Condition's flip left it, and only the first of two coins is heads");

        attack(binacle).execute(board.contextFor(binacle));
        assertEquals(30, target.damage(), "spent");
    }

    @Test
    @DisplayName("Silver offers only a Supporter from the opponent's hand")
    void silver() {
        TestBoard board = new TestBoard(new Recording(0), new Recording(0));
        board.inHand(board.them, Trainers.SQUIRT_BOTTLE);
        CardInstance lyra = board.inHand(board.them, Trainers.LYRA);

        play(Trainers.SILVER, board.contextWithoutSource());

        assertFalse(board.them.hand().contains(lyra));
        assertTrue(board.them.deck().contains(lyra));
        assertEquals(1, board.them.hand().size());
    }

    @Test
    @DisplayName("Fisher recovers a Water Pokémon per heads, and nothing else")
    void fisher() {
        TestBoard board = new TestBoard(ScriptedRandom.flipping(true));
        board.inDiscard(board.you, Water.POLIWAG);
        board.inDiscard(board.you, Water.POLIWHIRL);
        board.inDiscard(board.you, Fire.MAGBY);

        play(Trainers.FISHER, board.contextWithoutSource());

        assertEquals(2, board.you.hand().size());
        assertEquals("Magby", board.you.discardPile().getFirst().definition().name());
    }

    @Test
    @DisplayName("Hiker arranges one card per Fighting Pokémon in play")
    void hiker() {
        TestBoard board = new TestBoard(new Recording(1), new Recording(1));
        board.active(board.you, Fighting.ONIX);
        board.bench(board.you, Fighting.GLIGAR);
        board.bench(board.you, Colorless.EEVEE);
        CardInstance first = board.inDeck(board.you, Fire.MAGBY);
        CardInstance second = board.inDeck(board.you, Lightning.PICHU);
        CardInstance third = board.inDeck(board.you, Lightning.ELEKID);

        play(Trainers.HIKER, board.contextWithoutSource());

        assertEquals(List.of(second, first, third), board.you.deck());
        assertEquals(List.of(second, first), board.you.seenTopCards());
    }

    @Test
    @DisplayName("Gigantic Press needs 2 Fighting Energy beyond its cost")
    void giganticPress() {
        PokemonInPlay donphan = board.active(board.you, Fighting.DONPHAN_EX);
        PokemonInPlay target = board.active(board.them, Psychic.GRANBULL);

        donphan.attachEnergy(Type.FIGHTING, 2);
        attack(donphan).execute(board.contextFor(donphan));
        assertEquals(50, target.damage());

        target.heal(50);
        donphan.attachEnergy(Type.FIGHTING, 1);
        attack(donphan).execute(board.contextFor(donphan));
        assertEquals(110, target.damage());
    }
}
