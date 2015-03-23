package com.blaster.data;

import com.blaster.DreamBlaster;

public class BlastConfig {
	private DreamBlaster ctx;
	
	public BlastConfig(DreamBlaster ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * Gets the amount of coal deposited
	 * @return the config value
	 */
	public int getCoalAmount() {
		// TODO find out the correct config
		return ctx.getConfigs().get(69);
	}
	
	/**
	 * Checks if the coal bag should be refilled
	 * @return coal bag has less than 5 coal
	 */
	public boolean isCoalBagEmpty() {
		// TODO get correct config
		if (!ctx.getInventory().contains("Coal bag")) {
			return false;
		}
		return ctx.getConfigs().get(699) < 5;
	}
	
	/**
	 * Checks if the stored ore has reached maximum (254 ore)
	 * @return true if full
	 */
	public boolean isFull() {
		// TODO get config
		return ctx.getConfigs().get(69999) < 5;
	}

}
