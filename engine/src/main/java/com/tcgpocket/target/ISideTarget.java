package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;

import java.util.Optional;

/**
 * Resolves "whose" against the board.
 *
 * <p>Unlike {@link ITarget} this never fails: a battle always has two sides.
 * That is why it returns a {@link Side} rather than an {@link Optional}.
 */
public sealed interface ISideTarget permits AttackerSide, OpponentSide, SelfSide {

    Side resolve(ResolutionContext context);
}
