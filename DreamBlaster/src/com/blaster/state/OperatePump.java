package com.blaster.state;

import com.blaster.DreamBlaster;
import com.blaster.data.TransitionId;

/**
 * Deposits ore onto the pressure gauge
 */
public class OperatePump extends ScriptState {

	public OperatePump(DreamBlaster ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public TransitionId execute() {
		if (ctx.myPlayer().isAnimating()) {
			return TransitionId.PUMP;
		}
		ctx.interactObj("Pump", "Operate");
		return TransitionId.NULL;
	}

}