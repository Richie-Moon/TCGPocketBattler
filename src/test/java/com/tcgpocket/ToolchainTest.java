package com.tcgpocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifies the build is actually configured for what the model needs.
 *
 * <p>The design in {@code docs/UML} leans on sealed hierarchies of records and
 * exhaustive pattern-matching switches — that is how {@code INumber},
 * {@code ICondition}, {@code ITarget} and {@code IEffect} are meant to be
 * written. If the compiler release ever slips backwards, that shows up here
 * rather than halfway through implementing the interpreter.
 *
 * <p>Safe to delete once real tests exist.
 */
class ToolchainTest {

    /** A miniature stand-in for the real INumber hierarchy. */
    sealed interface Expr permits Literal, Sum {
    }

    record Literal(int value) implements Expr {
    }

    record Sum(Expr left, Expr right) implements Expr {
    }

    private static int evaluate(Expr expr) {
        // Exhaustive over the permits clause: no default branch needed, and
        // adding a variant without handling it becomes a compile error.
        return switch (expr) {
            case Literal l -> l.value();
            case Sum s -> evaluate(s.left()) + evaluate(s.right());
        };
    }

    @Test
    @DisplayName("sealed interfaces, records and pattern-matching switch compile and run")
    void interpreterStyleCompilesAndRuns() {
        Expr expr = new Sum(new Literal(30), new Sum(new Literal(20), new Literal(10)));

        assertEquals(60, evaluate(expr));
    }

    @Test
    @DisplayName("records give structural equality, which test assertions rely on")
    void recordsCompareStructurally() {
        assertEquals(new Literal(20), new Literal(20));
        assertEquals(new Sum(new Literal(1), new Literal(2)),
                     new Sum(new Literal(1), new Literal(2)));
    }

    @Test
    @DisplayName("running on Java 25 or newer")
    void javaVersionIsAtLeast25() {
        int major = Runtime.version().feature();

        assertTrue(major >= 25, "expected Java 25+, found " + major);
    }
}
