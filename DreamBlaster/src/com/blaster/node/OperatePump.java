package com.blaster.node;

import com.blaster.DreamBlaster;

/**
 * Deposits ore onto the pressure gauge
 */
public class OperatePump implements Node {

	public OperatePump(DreamBlaster ctx) {
		this.ctx = ctx;
	}

	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public boolean execute() {
		if (ctx.myPlayer().isAnimating()) {
			return true;
		}
		return ctx.interactObj("Pump", "Operate");
	}

}