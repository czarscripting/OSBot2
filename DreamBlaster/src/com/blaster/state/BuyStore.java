package com.blaster.state;

import com.blaster.DreamBlaster;
import com.blaster.data.TransitionId;

/**
 * Deposits ore onto the pressure gauge
 */
public class BuyStore extends ScriptState {
	
	public BuyStore(DreamBlaster ctx) {
		super(ctx);
	}
	
	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public TransitionId execute() {
		if (ctx.getInventory().contains("Spadeful of coke")) {
			ctx.interactObj("Stove", "Refuel");
		} else {
			ctx.interactObj("Coke", "Collect");
		}
		return TransitionId.NULL;
	}

}