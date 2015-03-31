package com.blaster.data;

import com.blaster.DreamBlaster;

public class BlastConfig {
	private DreamBlaster ctx;
	/**
	 * First four ore types (for the bars ready config)
	 */
	public static final int CONFIG_ORES_1 = 545;
	/**
	 * Last four ore types (for the bars ready config)
	 */
	public static final int CONFIG_ORES_2 = 546;
	/**
	 * Amount of coal in the furnace
	 */
	public static final int CONFIG_COAL_AMOUNT = 547;

	/**
	 */
	public BlastConfig(DreamBlaster ctx) {
		this.ctx = ctx;
	}

	/**
	 * Gets the amount of coal deposited
	 * 
	 * @return the config value
	 */
	public int getCoalAmount() {
		return ctx.getConfigs().get(547);
	}

	/**
	 * Gets the amount of bars of a bar type
	 * 
	 * @return the amount of bars
	 */
	public int getBars(OreData ore) {
		return ctx.getConfigs().get(547);
	}

	/**
	 * Checks if the coal bag should be refilled
	 * 
	 * @return coal bag has less than 5 coal
	 */
	public boolean isCoalBagEmpty() {
		if (!ctx.getInventory().contains("Coal bag")) {
			return false;
		}
		return ctx.getConfigs().get(699) < 5;
	}

	/**
	 * Checks if the stored ore has reached maximum (254 ore)
	 * 
	 * @return true if full
	 */
	public boolean isFull() {
		return ctx.getConfigs().get(69999) < 5;
	}
	
	/**
	 * Gets the bar amount for the user's chosen ore
	 * @return the amount of bars ready to take
	 */
	public int getMyBarAmount() {
		return getBarAmount(ctx.getChosenOre());
	}

	/**
	 * Gets the amount of bars ready to take for a certain ore
	 * @param ore the ore to get
	 * @return the amount of bars
	 */
	public int getBarAmount(OreData ore) {
		switch (ore) {
		case BRONZE:
			return (CONFIG_ORES_1) & 0xFF;
		case IRON:
			return (CONFIG_ORES_1 >> 8) & 0xFF;
		case STEEL:
			return (CONFIG_ORES_1 >> 16) & 0xFF;
		case MITHRIL:
			return (CONFIG_ORES_1 >> 24) & 0xFF;
		case ADAMANTITE:
			return (CONFIG_ORES_2) & 0xFF;
		case RUNE:
			return (CONFIG_ORES_2 >> 8) & 0xFF;
		case GOLD:
			return (CONFIG_ORES_2 >> 16) & 0xFF;
		case SILVER:
			return (CONFIG_ORES_2 >> 24) & 0xFF;
		}
		return -1;
	}

}