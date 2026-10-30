package com.tcgpocket.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("OptionView")
class OptionViewTest {

    @Test
    @DisplayName("a yes-or-no answer is logged with its question, and anything else without")
    void logText() {
        assertEquals("Draw 2 cards? Yes", new OptionView("Yes", "answer", null, null).logText("Draw 2 cards?"));
        assertEquals("Retreat", new OptionView("Retreat", "retreat", null, null).logText("Your turn"));
    }
}
