package com.blaster.node;

import org.osbot.rs07.api.model.RS2Object;

import com.blaster.DreamBlaster;

public class CollectBars implements Node {

	public CollectBars(DreamBlaster ctx) {
		this.ctx = ctx;
	}

	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public boolean execute() {
		if (canTakeBars()) {
			return ctx.interactObj("Bar dispenser", "Take");
		}
		if (ctx.getInventory().contains("Bucket of water")) {
			// it hasn't been cooled
			if (ctx.getInventory().isItemSelected()) {
				return ctx.interactObj("Bar dispenser", "Use");
			} else {
				return ctx.getInventory().interact("Use", "Bucket of water");
			}
		} else {
			if (ctx.getInventory().isItemSelected()) {
				// safety purposes
				return ctx.getInventory().deselectItem();
			}
		}
		return false;
	}
	
	public boolean canTakeBars() {
		RS2Object barDispenser = ctx.getObjects().closest("Bar dispenser");
		if (barDispenser == null) {
			return false;
		}
		if (barDispenser.hasAction("Take")) {
			return true;
		}
		return false;
	}

}
