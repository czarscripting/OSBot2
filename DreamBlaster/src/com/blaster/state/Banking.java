package com.blaster.state;

import com.blaster.DreamBlaster;
import com.blaster.data.TransitionId;

public class Banking extends ScriptState {
	
	public Banking(DreamBlaster ctx) {
		super(ctx);
	}
	
	DreamBlaster ctx;

	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public TransitionId execute() {
		if (ctx.getBank().isOpen()) {
			if (ctx.getInventory().isEmptyExcept("Coins", "Bucket", "Bucket of water")) {
				
			} else {
				ctx.getBank().depositAllExcept("Coins", "Bucket", "Bucket of water");
			}
		} else {
			
		}
		return TransitionId.NULL;
	}
	
	

}
