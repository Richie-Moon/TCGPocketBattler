package com.tcgpocket.server;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.IAbility;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.PlayableItemCard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.StadiumCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.pool.CardPool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The card catalogue the deck editor browses: every card in {@link CardPool}, in set order.
 *
 * <p>Only what is printed on a card goes out, so nothing here is hidden state, and it needs no
 * sign-in or database.
 */
@RestController
class Cards {

    /**
     * @param kind   "Pokémon", "Item", "Supporter", "Tool" or "Stadium"
     * @param types  a Pokémon's printed types; empty for a Trainer
     * @param search the attack and ability names, for the editor's search box
     */
    record Entry(String id, String name, String kind, Set<Type> types, String search) {
    }

    private static final List<Entry> ALL = CardPool.all().stream().map(Cards::entry).toList();

    @GetMapping("/api/cards")
    List<Entry> all() {
        return ALL;
    }

    static Entry entry(ICard card) {
        return switch (card) {
            case PokemonCard pokemon -> new Entry(card.id(), card.name(), "Pokémon", pokemon.types(),
                    Stream.concat(pokemon.actions().stream().flatMap(Cards::attackName),
                                    pokemon.ability().map(IAbility::name).stream())
                            .collect(Collectors.joining(" ")));
            case PlayableItemCard _, ItemCard _ -> trainer(card, "Item");
            case SupporterCard _ -> trainer(card, "Supporter");
            case ToolCard _ -> trainer(card, "Tool");
            case StadiumCard _ -> trainer(card, "Stadium");
        };
    }

    private static Entry trainer(ICard card, String kind) {
        return new Entry(card.id(), card.name(), kind, Set.of(), "");
    }

    /** An attack is an {@link Action}, sometimes behind a precondition; the other actions carry no name. */
    private static Stream<String> attackName(IAction action) {
        return switch (action) {
            case Action attack -> Stream.of(attack.name());
            case WithPrecondition guarded -> attackName(guarded.action());
            default -> Stream.empty();
        };
    }
}
