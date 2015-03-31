package com.blaster.state;

import org.osbot.rs07.api.model.RS2Object;

import com.blaster.DreamBlaster;
import com.blaster.data.TransitionId;

public class CollectBars extends ScriptState {

	public CollectBars(DreamBlaster ctx) {
		super(ctx);
	}

	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public TransitionId execute() {
		if (canTakeBars()) {
			ctx.interactObj("Bar dispenser", "Take");
			return TransitionId.NULL;
		}
		if (ctx.getInventory().contains("Bucket of water")) {
			// it hasn't been cooled
			if (ctx.getInventory().isItemSelected()) {
				ctx.interactObj("Bar dispenser", "Use");
				return TransitionId.NULL;
			} else {
				ctx.getInventory().interact("Use", "Bucket of water");
				return TransitionId.NULL;
			}
		} else {
			if (ctx.getInventory().isItemSelected()) {
				// safety purposes
				ctx.getInventory().deselectItem();
				return TransitionId.NULL;
			}
		}
		return TransitionId.NULL;
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
