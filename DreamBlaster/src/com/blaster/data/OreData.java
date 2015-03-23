package com.blaster.data;

public enum OreData {
	IRON,
	MITHRIL,
	ADAMANTITE,
	RUNE,
	;
	
	private int amount;
	private String name;
	
	OreData() {
		setName(name() + " ore");
		setAmount(ordinal() * 2);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}