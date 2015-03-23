package com.blaster.node;

import com.blaster.DreamBlaster;

public class Banking implements Node {
	
	public Banking(DreamBlaster ctx) {
		this.ctx = ctx;
	}
	
	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public boolean execute() {
		if (ctx.getBank().isOpen()) {
			if (ctx.getInventory().isEmptyExcept("Coins", "Bucket", "Bucket of water")) {
				
			} else {
				return ctx.getBank().depositAllExcept("Coins", "Bucket", "Bucket of water");
			}
		} else {
			
		}
		return false;
	}
	
	

}
