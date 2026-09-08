package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTypeEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PreventAttack;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

public final class Fire {
    /**
     * A1-033 - Charmander
     */
    public static final PokemonCard CHARMANDER = PokemonCard.basic(
            "A1-033", "Charmander", "It has a preference for hot things. When it rains, steam is said to spout from the tip of its tail.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ember", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A1-034 - Charmeleon
     */
    public static final PokemonCard CHARMELEON = PokemonCard.evolution(
            "A1-034", "Charmeleon", "It has a barbaric nature. In the battle, it whips its fiery tail around and slashes away with sharp claws.",
            1, "Charmeleon", 90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fire Claws", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A1-035 - Charizard
     */
    public static final PokemonCard CHARIZARD = PokemonCard.evolution(
            "A1-035", "Charizard", "It spits fire that is hot enough to melt boulders. It may cause forest fires by blowing flames.",
            2, "Charmeleon", 150, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fire Spin", "Discard 2 Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(2), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A1-036 - Charizard ex
     */
    public static final PokemonCard CHARIZARD_EX = PokemonCard.evolution(
            "A1-036", "Charizard ex", "",
            2, "Charmeleon", 180, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Slash", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(60), new OpponentActive())))
                    ), new Action(
                            "Crimson Storm", "Discard 2 Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(200), new OpponentActive()),
                            new DiscardTypeEnergy(Type.FIRE, new Literal(2), new Self()))))
            ), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.WATER).withTags(CardTag.EX);

    /**
     * A1-037 - Vulpix
     */
    public static final PokemonCard VULPIX = PokemonCard.basic(
            "A1-037", "Vulpix", "While young, it has six gorgeous tails. When it grows, several new tails are sprouted.",
            50, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tail Whip", "Flip a coin. If heads, the Defending Pokémon can't attack during your opponent's next turn.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(
                            new LastCoinTossHeads(),
                            new PreventAttack(new OpponentActive(), new Literal(1))))))
            ), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A1-038 - Ninetales
     */
    public static final PokemonCard NINETALES = PokemonCard.evolution(
            "A1-038", "Ninetales", "It is said to live for 1,000 years, and each of its tails is loaded with supernatural powers.",
            1, "Vulpix", 90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flamethrower", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    private Fire() {
    }
}
