package com.blaster.util;

public final class PaintItem {
	
	private String item;
	private boolean drawCondition;
	private boolean condition;
	
	public PaintItem(String item, boolean drawCondition, boolean condition) {
		setItem(item);
		setDrawCondition(drawCondition);
		setCondition(condition);
	}

	public boolean isCondition() {
		return condition;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public boolean isDrawCondition() {
		return drawCondition;
	}

	public void setDrawCondition(boolean drawCondition) {
		this.drawCondition = drawCondition;
	}

}