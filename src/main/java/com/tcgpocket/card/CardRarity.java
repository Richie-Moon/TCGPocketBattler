package com.tcgpocket.card;

public enum CardRarity {
    /** 1 Diamond */
    COMMON("Common"),
    /** 2 Diamond */
    UNCOMMON("Uncommon"),
    /** 3 Diamond */
    RARE("Rare"),
    /** 4 Diamond */
    DOUBLE_RARE("Double Rare"),
    /** 1 Star */
    ILLUSTRATION_RARE("Illustration Rare"),
    /** 2 Star - Full art */
    ULTRA_RARE("Ultra Rare"),
    /** 2 Star - Rainbow border */
    SPECIAL_ILLUSTRATION_RARE("Special Illustration Rare"),
    /** 3 Star */
    IMMERSIVE("Immersive"),
    /** 1 Shiny Star */
    SHINY_RARE("Shiny Rare"),
    /** 2 Shiny Star */
    SHINY_ULTRA_RARE("Shiny Ultra Rare"),
    /** Crown */
    HYPER_RARE("Hyper Rare");

    private final String name;

    CardRarity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
