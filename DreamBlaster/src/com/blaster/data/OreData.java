package com.blaster.data;

public enum OreData {
	BRONZE("Bronze bar", 0),
	IRON("Iron bar", 0),
	STEEL("Steel bar", 1),
	MITHRIL("Mithril bar", 2),
	ADAMANTITE("Adamant bar", 4),
	RUNE("Rune bar", 8),
	GOLD("Gold bar", 0),
	SILVER("Silver bar", 0),
	;
	
	private int amount;
	private String name;
	
	OreData(String barName, int coal) {
		setName(name() + " ore");
		setAmount(coal);
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