package com.blaster.node;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;

import com.blaster.DreamBlaster;

/**
 * Deposits ore onto the pressure gauge
 */
public class PutOre implements Node {
	
	public PutOre(DreamBlaster ctx) {
		this.ctx = ctx;
	}
	
	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return ctx.getCfg().getCoalAmount() < 254;
	}

	@Override
	public boolean execute() {
		if (ctx.getDialogues().isPendingContinuation()) {
			ctx.getDialogues().clickContinue();
			return true;
		}
		if (ctx.getDialogues().isPendingOption()) {
			ctx.getDialogues().selectOption(1);
			return true;
		}
		if (getBelt() == null) {
			return false;
		}
		if (closerToBank()) {
			Position walkPos = new Position(getBelt().getPosition().getX() - 5, getBelt().getPosition().getY(), getBelt().getPosition().getZ());
			if (ctx.getMap().walk(walkPos)) {
				return true;
			}
		} else {
			return ctx.interactObj("Conveyor belt", "Put-ore-on");
		}
		return false;
	}
	
	public boolean closerToBank() {
		RS2Object bankChest = ctx.getObjects().closest("Bank chest");
		if (bankChest == null) {
			return false;
		}
		RS2Object conveyorBelt = getBelt();
		if (conveyorBelt == null) {
			return false;
		}
		int distanceToChest = ctx.myPosition().distance(bankChest.getPosition());
		int distanceToBelt = ctx.myPosition().distance(conveyorBelt.getPosition());
		return distanceToChest < distanceToBelt;
	}
	
	public RS2Object getBelt() {
		RS2Object conveyorBelt = ctx.getObjects().closest("Conveyor belt");
		if (conveyorBelt == null) {
			return null;
		}
		return conveyorBelt;
	}

}