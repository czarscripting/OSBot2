package com.blaster.util;

import java.util.Date;

public class ScriptTimer {
	public long start;
	private Date lastDate;

	public ScriptTimer start() {
		return new ScriptTimer();
	}

	public ScriptTimer() {
		reset();
	}

	public ScriptTimer reset() {
		start = System.currentTimeMillis();
		return this;
	}

	public long time() {
		long end = System.currentTimeMillis();
		return end - start;
	}

	public int getSeconds() {
		return (int) (time() / 1000L);
	}

	public int getMinutes() {
		return (int) (getSeconds() / 60L);
	}

	public int getHours() {
		final int sec = (int) (time() / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (int) h;
	}
}