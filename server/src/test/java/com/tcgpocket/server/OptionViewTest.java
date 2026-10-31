package com.tcgpocket.server;

import com.tcgpocket.card.IPlayableCard;
import com.tcgpocket.effect.DistributeMixedEnergy.EnergyPlacement;
import com.tcgpocket.energy.Type;
import com.tcgpocket.player.Decision;
import com.tcgpocket.player.RandomPlayer;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.resolve.SeededRandom;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("OptionView")
class OptionViewTest {

    @Test
    @DisplayName("a yes-or-no answer is logged with its question, and anything else without")
    void logText() {
        assertEquals("Draw 2 cards? Yes", new OptionView("Yes", "answer", null, null).logText("Draw 2 cards?"));
        assertEquals("Retreat", new OptionView("Retreat", "retreat", null, null).logText("Your turn"));
    }

    @Test
    @DisplayName("a mixed Energy placement says which Energy goes where, and an empty list reads as None")
    void placements() {
        Side you = new Side("you", new RandomPlayer("you", new SeededRandom(1)));
        Side them = new Side("them", new RandomPlayer("them", new SeededRandom(2)));
        ResolutionContext context = ResolutionContext.of(new Battle(you, them, new SeededRandom(3)), you);
        PokemonInPlay oddish = new PokemonInPlay(1, (IPlayableCard) CardPool.get("A4-001"), you, Zone.BENCH, 0);
        List<List<EnergyPlacement>> options = List.of(
                List.of(new EnergyPlacement(Type.FIRE, oddish), new EnergyPlacement(Type.WATER, oddish)));
        Decision<List<EnergyPlacement>> decision = new Decision<>("Place", options, you, context);

        assertEquals("Fire on Oddish (your bench, 50 HP), Water on Oddish (your bench, 50 HP)",
                OptionView.of(options.get(0), decision).label());
        assertEquals("None", OptionView.of(List.of(), decision).label());
    }
}
