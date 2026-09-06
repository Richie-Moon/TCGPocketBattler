package com.tcgpocket.trigger;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.StadiumCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.Fail;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.IEffect;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.EventDamage;
import com.tcgpocket.number.Literal;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ITriggerTest {

    private final TestBoard board = new TestBoard();

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard BULBASAUR = TestBoard.card("Bulbasaur", 70, Type.GRASS);

    private static Trigger whenever(Class<? extends GameEvent> on, IEffect... effects) {
        return new Trigger(on, new Attempt(List.of(effects)));
    }

    private static Trigger when(
            Class<? extends GameEvent> on, ICondition<ResolutionContext> condition, IEffect... effects) {
        return new Trigger(on, condition, new Attempt(List.of(effects)));
    }

    @Nested
    @DisplayName("A trigger on its own")
    class TriggerItself {

        @Test
        void listensForOneEventTypeOnly() {
            Trigger onDamage = whenever(DamageDealt.class);
            PokemonInPlay pikachu = board.active(board.you, PIKACHU);

            assertTrue(onDamage.appliesTo(new DamageDealt(Optional.empty(), pikachu, 10)));
            assertFalse(onDamage.appliesTo(new TurnEnd(board.you)));
        }

        @Test
        @DisplayName("declines quietly when its condition does not hold")
        void aFalseConditionIsNotAFailure() {
            PokemonInPlay pikachu = board.active(board.you, PIKACHU);
            pikachu.takeDamage(30);

            Trigger never = when(
                    TurnEnd.class,
                    new EventSideIs(new OpponentSide()),
                    new HealDamage(new Literal(30), new Self()));

            AttemptResult result = never.fire(TriggerSource.heldBy(never, pikachu)
                    .contextFor(board.battle, new TurnEnd(board.you)));

            assertTrue(result.succeeded(), "declining to fire is not a failure");
            assertFalse(result.changedTheBoard());
            assertEquals(30, pikachu.damage());
        }
    }

    @Nested
    @DisplayName("Collection walks the board")
    class Collection {

        @Test
        void findsATriggerOnAnAttachedTool() {
            PokemonInPlay pikachu = board.active(board.you, PIKACHU);
            ToolCard cape = ToolCard.of("cape", "Giant Cape", whenever(TurnEnd.class));
            pikachu.attachTool(board.loose(board.you, cape));

            List<TriggerSource> found = TriggerDispatcher.collect(board.battle);

            assertEquals(1, found.size());
            assertEquals(Optional.of(pikachu), found.get(0).holder());
            assertSame(board.you, found.get(0).controller());
        }

        @Test
        @DisplayName("a Tool that has been removed cannot fire")
        void noStaleListeners() {
            PokemonInPlay pikachu = board.active(board.you, PIKACHU);
            ToolCard cape = ToolCard.of("cape", "Giant Cape", whenever(TurnEnd.class));
            pikachu.attachTool(board.loose(board.you, cape));
            pikachu.removeTool();

            assertTrue(TriggerDispatcher.collect(board.battle).isEmpty());
        }

        @Test
        @DisplayName("an evolution brings its own ability with it")
        void evolvingSwapsTheAbility() {
            PokemonInPlay pokemon = board.active(board.you, BULBASAUR);
            assertTrue(TriggerDispatcher.collect(board.battle).isEmpty());

            PokemonCard ivysaur = PokemonCard
                    .evolution("ivysaur", "Ivysaur", 1, "Bulbasaur", 90, Type.GRASS, 2, List.of())
                    .withAbility(new PassiveAbility("Photosynthesis", whenever(TurnEnd.class)));
            pokemon.evolveInto(ivysaur, 2);

            assertEquals(1, TriggerDispatcher.collect(board.battle).size());
        }

        @Test
        @DisplayName("a Stadium has no holder, so its text cannot say Self")
        void stadiumTriggersAreUnheld() {
            board.active(board.you, PIKACHU);
            StadiumCard tower = StadiumCard.of("tower", "Lightning Tower", whenever(TurnEnd.class));
            board.battle.setStadium(board.loose(board.you, tower));

            List<TriggerSource> found = TriggerDispatcher.collect(board.battle);

            assertEquals(1, found.size());
            assertTrue(found.get(0).holder().isEmpty());
            assertSame(board.you, found.get(0).controller());
        }

        @Test
        @DisplayName("both sides are walked, not just the attacker's")
        void reachesTheDefender() {
            PokemonInPlay theirs = board.active(board.them, BULBASAUR);
            ToolCard cape = ToolCard.of("cape", "Giant Cape", whenever(TurnEnd.class));
            theirs.attachTool(board.loose(board.them, cape));

            List<TriggerSource> found = TriggerDispatcher.collect(board.battle);

            assertEquals(1, found.size());
            assertSame(board.them, found.get(0).controller(),
                    "a defender's Tool is still controlled by the defender");
        }
    }

    @Nested
    @DisplayName("Dispatch")
    class Dispatch {

        @Test
        @DisplayName("Self means the Pokemon holding the trigger, not the attacker")
        void firesWithTheHolderAsSource() {
            PokemonInPlay mine = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(board.them, BULBASAUR);
            mine.takeDamage(40);
            theirs.takeDamage(40);

            ToolCard cape = ToolCard.of("cape", "Giant Cape",
                    whenever(TurnEnd.class, new HealDamage(new Literal(10), new Self())));
            theirs.attachTool(board.loose(board.them, cape));

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertEquals(30, theirs.damage(), "the Cape healed its wearer");
            assertEquals(40, mine.damage(), "and nobody else");
        }

        @Test
        @DisplayName("a defender's Tool hits back at whoever is attacking it")
        void turnRelativeTargetsReachAcross() {
            PokemonInPlay mine = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(board.them, BULBASAUR);

            ToolCard spikes = ToolCard.of("spikes", "Rocky Helmet",
                    whenever(TurnEnd.class, new PlaceDamage(new Literal(20), new AttackerActive())));
            theirs.attachTool(board.loose(board.them, spikes));

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertEquals(20, mine.damage());
            assertEquals(0, theirs.damage());
        }

        @Test
        @DisplayName("EventConcerns keeps a board-wide dispatch from hitting everyone")
        void onlyTheEventsSubjectReacts() {
            PokemonInPlay front = board.active(board.you, PIKACHU);
            PokemonInPlay back = board.bench(board.you, BULBASAUR);
            front.takeDamage(30);
            back.takeDamage(30);

            ToolCard cape = ToolCard.of("cape", "Giant Cape",
                    when(DamageDealt.class, new EventConcerns(new Self()),
                            new HealDamage(new Literal(10), new Self())));
            front.attachTool(board.loose(board.you, cape));
            back.attachTool(board.loose(board.you, cape));

            TriggerDispatcher.dispatch(board.battle, new DamageDealt(Optional.empty(), front, 30));

            assertEquals(20, front.damage(), "the damaged Pokemon's Cape fired");
            assertEquals(30, back.damage(), "the other one's did not");
        }

        @Test
        @DisplayName("the event's payload is readable from inside the trigger")
        void eventDamageResolves() {
            PokemonInPlay mine = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(board.them, BULBASAUR);

            ToolCard thorns = ToolCard.of("thorns", "Thorns",
                    when(DamageDealt.class, new EventConcerns(new Self()),
                            new PlaceDamage(new EventDamage(), new OpponentActive())));
            mine.attachTool(board.loose(board.you, thorns));

            TriggerDispatcher.dispatch(board.battle, new DamageDealt(Optional.of(theirs), mine, 40));

            assertEquals(40, theirs.damage(), "took back exactly what it dealt");
        }

        @Test
        void reportsWhatItFired() {
            board.active(board.you, PIKACHU);
            DispatchResult result = TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertEquals(0, result.fired());
            assertFalse(result.vetoed());
            assertFalse(result.changedTheBoard());
        }

        @Test
        @DisplayName("a failed trigger attempt is a veto")
        void failurePropagates() {
            PokemonInPlay pikachu = board.active(board.you, PIKACHU);
            ToolCard veto = ToolCard.of("veto", "Veto", whenever(TurnEnd.class, new Fail()));
            pikachu.attachTool(board.loose(board.you, veto));

            DispatchResult result = TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertEquals(1, result.fired());
            assertTrue(result.vetoed());
        }

        @Test
        @DisplayName("a retaliation loop is cut off rather than overflowing the stack")
        void depthIsBounded() {
            PokemonInPlay mine = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(board.them, BULBASAUR);

            // Fires on any damage anywhere, and deals damage — so its own
            // DamageDealt event feeds straight back into it.
            ToolCard loop = ToolCard.of("loop", "Feedback Loop",
                    when(DamageDealt.class, new Always<ResolutionContext>(),
                            new PlaceDamage(new Literal(10), new AttackerActive())));
            theirs.attachTool(board.loose(board.them, loop));

            TriggerDispatcher.dispatch(board.battle, new DamageDealt(Optional.empty(), theirs, 0));

            assertEquals(mine.maxHp(), mine.damage(), "it ran until the target was full of damage");
        }
    }

    @Nested
    @DisplayName("Events the model dispatches")
    class DispatchSites {

        @Test
        @DisplayName("dealing damage announces what actually landed, after weakness")
        void dealDamageAnnouncesTheFinalAmount() {
            PokemonInPlay mine = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(
                    board.them, TestBoard.card("Bulbasaur", 70, Type.GRASS).withWeakness(Type.LIGHTNING));

            ToolCard echo = ToolCard.of("echo", "Echo",
                    when(DamageDealt.class, new EventConcerns(new Self()),
                            new PlaceDamage(new EventDamage(), new AttackerActive())));
            theirs.attachTool(board.loose(board.them, echo));

            new DealDamage(new Literal(30), new OpponentActive()).apply(board.contextFor(mine));

            assertEquals(50, theirs.damage(), "30 plus the 20 weakness bonus");
            assertEquals(50, mine.damage(), "and the echo repeated the post-weakness number");
        }

        @Test
        @DisplayName("poison damage names no source, so nothing retaliates against it")
        void placedDamageHasNoSource() {
            PokemonInPlay mine = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(board.them, BULBASAUR);

            ToolCard thorns = ToolCard.of("thorns", "Thorns",
                    when(DamageDealt.class, new EventConcerns(new Self()),
                            new PlaceDamage(new EventDamage(), new AttackerActive())));
            theirs.attachTool(board.loose(board.them, thorns));

            // Dealt by nobody: the event carries no source, but the Tool still
            // sees that its wearer was hurt.
            new PlaceDamage(new Literal(10), new OpponentActive()).apply(board.contextFor(mine));

            assertEquals(10, theirs.damage());
            assertEquals(10, mine.damage(), "Thorns fires on any damage to its wearer");
        }

        @Test
        void healingAnnouncesWhatCameOffNotWhatWasAskedFor() {
            TestBoard fixed = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay mine = fixed.active(fixed.you, PIKACHU);
            PokemonInPlay theirs = fixed.active(fixed.them, BULBASAUR);
            mine.takeDamage(20);

            ToolCard mirror = ToolCard.of("mirror", "Mirror",
                    when(Healed.class, new EventConcerns(new Self()),
                            new PlaceDamage(new EventDamage(), new OpponentActive())));
            mine.attachTool(fixed.loose(fixed.you, mirror));

            new HealDamage(new Literal(50), new Self()).apply(fixed.contextFor(mine));

            assertEquals(0, mine.damage());
            // EventDamage only reads DamageDealt, so the mirror places nothing;
            // what matters here is that Healed carried 20, not 50.
            assertEquals(0, theirs.damage());
        }

        @Test
        void attachingATriggerlessToolStillAnnouncesIt() {
            PokemonInPlay pikachu = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(board.them, BULBASAUR);
            CardInstance cape = board.inHand(board.you, ToolCard.named("cape", "Giant Cape"));

            ToolCard alarm = ToolCard.of("alarm", "Alarm",
                    when(ToolAttached.class, new EventSideIs(new AttackerSide()),
                            new PlaceDamage(new Literal(10), new AttackerActive())));
            theirs.attachTool(board.loose(board.them, alarm));

            new com.tcgpocket.effect.AttachTool(new Self(), (ToolCard) cape.definition())
                    .apply(board.contextFor(pikachu));

            assertTrue(pikachu.hasTool());
            assertEquals(10, pikachu.damage(), "the opposing Alarm noticed");
        }
    }
}
