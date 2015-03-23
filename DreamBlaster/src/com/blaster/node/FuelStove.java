package com.blaster.node;

import com.blaster.DreamBlaster;

/**
 * Deposits ore onto the pressure gauge
 */
public class FuelStove implements Node {
	
	public FuelStove(DreamBlaster ctx) {
		this.ctx = ctx;
	}
	
	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public boolean execute() {
		if (ctx.getInventory().contains("Spadeful of coke")) {
			return ctx.interactObj("Stove", "Refuel");
		} else {
			return ctx.interactObj("Coke", "Collect");
		}
	}

}