package com.blaster.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import org.osbot.rs07.api.ui.Skill;

import com.blaster.DreamBlaster;

public class Paint implements KeyListener, MouseListener, MouseMotionListener {

	private DreamBlaster script;

	public String bigFontPath = "http://www.sitefilehosting.com/pakko_fl/TheBridgeS1/fonts/AgencyFB/TTF/Agency%20FB%20Bold.ttf";
	public String objectiveFontPath = "http://www.huttonconstruction.com/wp-content/themes/hcc/css/fonts/eurostile-webfont.ttf";
	public Font bigFont;
	public Font objectiveFont;
	public Font epicFont;
	public Graphics graphics;

	private int combatExp;
	public Skill mySkill = Skill.ATTACK;

	// Buttons
	public CzarButton[] buttons;

	// Features
	public static final int FONT_BOLD = 1;
	public static final int FONT_ITALIC = 2;
	public static final int FONT_SHADOW = 4;
	public static final int FONT_CENTER = 8;
	public static final int FONT_ALIGN_RIGHT = 16;
	// Sizes
	public static final int FONT_EXTRABIG = 32;
	public static final int FONT_BIG = 64;
	public static final int FONT_OBJECTIVE = 128;
	public static final int FONT_SLAYER = 256;
	public static final int FONT_EPIC = 512;

	// Custom skills
	public int[] startLevels = new int[25];
	public int[] startExps = new int[25];

	// Paint start coords
	public int originalX = 15;
	public int originalY = 65;

	// Color theme (replaces yellow)
	public String colorTheme = "^3";

	private Tab tab = Tab.GENERAL;

	private String firstString, secondString;

	public enum Tab {
		GENERAL, FISHING, PROFIT, WELCOME
	}

	public PaintItem getDefault() {
		return null;
	}

	public int getLevelForXP(int exp) {
		int xp = 0;
		for (int i = 0; i < xpTable.length; i++) {
			if (exp >= xpTable[i]) {
				xp = i;
				i++;
			}
		}
		return xp;
	}

	public int draw(PaintItem... p) {
		if (graphics == null) {
			return 0;
		}
		int sec = (int) (getTime() / 1000);
		if (sec < 2) {
			return 0;
		}
		int x = originalX;
		int y = originalY;
		switch (tab) {
		case WELCOME:
			drawSpace(0, 0, 765, 565);
			break;
		case GENERAL:
			graphics.setColor(Color.BLACK);
			graphics.drawLine(originalX, originalY + 6, originalX + 215,
					originalY + 6);
			graphics.drawLine(originalX, originalY + 41, originalX + 215,
					originalY + 41);
			graphics.setColor(Color.WHITE);
			graphics.drawLine(originalX, originalY + 5, originalX + 215,
					originalY + 5);
			graphics.drawLine(originalX, originalY + 40, originalX + 215,
					originalY + 40);
			setRenderingHints();
			String czar = firstString;
			String aio = secondString;
			String time = "" + script.getVersion();
			drawString(czar, x, y, FONT_EXTRABIG | FONT_BOLD | FONT_SHADOW);
			x += getStringWidth(czar);
			drawString(aio, x, y, FONT_EXTRABIG | FONT_BOLD | FONT_SHADOW);
			x += getStringWidth(aio);
			drawString(time, x, y, FONT_BIG | FONT_SHADOW);
			x += getStringWidth(time);
			y += getStringHeight(czar);
			x = originalX + 24;
			y = originalY + 33;

			int level2 = script.getSkills().getStatic(mySkill);

			String level = "Level: " + colorTheme + "" + level2 + "";
			time = "" + formatTimePaint(getTime());

			drawString(level, x, y, FONT_BIG | FONT_SHADOW);
			x = originalX + 224 / 2;
			drawString(time, x, y, FONT_BIG | FONT_SHADOW);
			y += getStringHeight(time) + 15;

			x = originalX;
			y = originalY + 64;
			y += drawCustom(x, y, p);
			break;
		}
		return y;
	}

	public void drawString(String s, int x, int y, int arg) {
		if (graphics == null) {
			return;
		}
		boolean isBold = ((arg & FONT_BOLD) == FONT_BOLD);
		boolean isCenter = ((arg & FONT_CENTER) == FONT_CENTER);
		boolean isAlignRight = ((arg & FONT_ALIGN_RIGHT) == FONT_ALIGN_RIGHT);
		boolean isShadow = ((arg & FONT_SHADOW) == FONT_SHADOW);
		boolean fontExtraBig = ((arg & FONT_EXTRABIG) == FONT_EXTRABIG);
		boolean fontBig = ((arg & FONT_BIG) == FONT_BIG);
		boolean fontObjective = ((arg & FONT_OBJECTIVE) == FONT_OBJECTIVE);
		boolean fontSlayer = ((arg & FONT_SLAYER) == FONT_SLAYER);
		boolean fontEpic = ((arg & FONT_EPIC) == FONT_EPIC);
		int pushX = 0, pushY = 0;
		if (fontExtraBig) {
			graphics.setFont(bigFont);
			graphics.setFont(graphics.getFont().deriveFont(44.0f));
		}
		if (fontBig) {
			graphics.setFont(bigFont);
			graphics.setFont(graphics.getFont().deriveFont(24.0f));
		}
		if (fontObjective) {
			graphics.setFont(objectiveFont);
			graphics.setFont(graphics.getFont().deriveFont(16.0f));
		}
		if (fontSlayer) {
			graphics.setFont(objectiveFont);
			graphics.setFont(graphics.getFont().deriveFont(24.0f));
		}
		if (fontEpic) {
			graphics.setFont(epicFont);
			graphics.setFont(graphics.getFont().deriveFont(52.0f));
		}
		if (isBold) {
			graphics.setFont(graphics.getFont().deriveFont(Font.BOLD));
		}
		if (isAlignRight) {
			FontMetrics fm = graphics.getFontMetrics();
			x -= (fm.stringWidth(s));
		}
		if (isCenter) {
			x = (x + 215 - getStringWidth(s)) / 2;
		}
		if (s.contains("^")) {
			String[] colors = s.split("\\^");
			for (String next : colors) {
				if (next != null && next.length() > 0) {
					Color colorCode = getColorForId(next.substring(0, 1));
					if (colorCode != null) {
						next = next.substring(1);
					}
					if (isShadow) {
						int out = (fontObjective && !isBold) ? 1 : 2;
						graphics.setColor(Color.BLACK);
						drawString(next, pushX + x + out, pushY + y + out);
					}
					graphics.setColor(colorCode != null ? colorCode
							: Color.WHITE);
					drawString(next, pushX + x, pushY + y);
					pushX += getStringWidth(next);
				}
			}
		} else {
			if (isShadow) {
				int out = (fontObjective && !isBold) ? 1 : 2;
				graphics.setColor(Color.BLACK);
				drawString(s, x + out, y + out);
			}
			graphics.setColor(Color.WHITE);
			drawString(s, x, y);
		}
	}

	public void drawString(String s, int x, int y) {
		for (int i = 0; i < s.split("\n").length; i++) {
			if (i > 0) {
				y += getStringHeight(s);
			}
			graphics.drawString(s.split("\n")[i], x, y);
		}
	}
	
	private Skills skillManager;

	// TODO
	public Paint(DreamBlaster script, String name1, String name2, String color1,
			Skill s) {
		this.skillManager = new Skills(script);
		this.script = script;
		firstString = name1;
		secondString = name2;
		this.mySkill = s;
		this.colorTheme = color1;
		try {
			URL bigFontURL = new URL(bigFontPath);
			bigFont = Font.createFont(Font.TRUETYPE_FONT,
					bigFontURL.openStream()).deriveFont(Font.PLAIN, 44);
			URL objectiveFontURL = new URL(objectiveFontPath);
			objectiveFont = Font.createFont(Font.TRUETYPE_FONT,
					objectiveFontURL.openStream()).deriveFont(Font.PLAIN, 20);
			epicFont = new Font("Vikings", Font.PLAIN, 52);
		} catch (MalformedURLException e) {
		} catch (FontFormatException e) {
		} catch (IOException e) {
		}
		for (int i = 0; i < 22; i++) {
			startLevels[i] = script.getSkills().getStatic(Skill.forId(i));
			startExps[i] = script.getSkills().getExperience(Skill.forId(i));
		}
	}

	public int perHour(int amount) {
		return (int) ((3600000.0 / (double) getTime()) * amount);
	}

	public int paint(Graphics2D g, PaintItem... p) {
		if (this.graphics == null) {
			this.graphics = g;
			return 0;
		}
		return draw(p);
	}

	public void setRenderingHints() {
		if (graphics == null) {
			return;
		}
		((Graphics2D) graphics).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) graphics).setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f));
	}

	public int getStringWidth(String text) {
		text = text.replaceAll("\\^;", "");
		for (int i = 0; i < 10; i++) {
			text = text.replaceAll("\\^" + i, "");
		}
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
		return metrics.stringWidth(text);
	}

	public int getStringHeight(String text) {
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
		return metrics.getHeight();
	}

	public Color getColorForId(String id) {
		switch (id) {
		case "1":
			return Color.RED;
		case "2":
			return Color.GREEN;
		case "3":
			return Color.YELLOW;
		case "4":
			return Color.BLUE;
		case "5":
			return Color.CYAN;
		case "6":
			return Color.PINK;
		case "7":
			return Color.WHITE;
		case "8":
			return DARK_GREEN;
		case "9":
			return Color.GRAY;
		case "0":
			return Color.BLACK;
		case ";":
			return YALE_BLUE;
		case ":":
			return LIGHT_YELLOW;
		}
		return null;
	}

	public Color YALE_BLUE = new Color(0x0f4d92);
	public Color LIGHT_YELLOW = new Color(0xFFFFE0);
	public Color DARK_GREEN = new Color(0x006400);
	public Color MAROON = new Color(0x800000);

	public long getTime() {
		return script.getTimeElapsed().time();
	}

	public String formatTimePaint(final long time) {
		final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	public String formatTime(long time) {
		StringBuilder t = new StringBuilder();
		long total_secs = time / 1000L;
		long total_mins = total_secs / 60L;
		long total_hrs = total_mins / 60L;
		int secs = (int) total_secs % 60;
		int mins = (int) total_mins % 60;
		int hrs = (int) total_hrs % 24;
		t.append(total_hrs > 0 ? "" + colorTheme + "" : "^7");
		if (hrs < 10) {
			t.append("0");
		}
		t.append(hrs).append("^7:");
		t.append(total_mins > 0 ? "" + colorTheme + "" : "^7");
		if (mins < 10) {
			t.append("0");
		}
		t.append(mins).append("^7:");
		t.append(total_secs > 0 ? "" + colorTheme + "" : "^7");
		if (secs < 10) {
			t.append("0");
		}
		t.append(secs);
		return t.toString();
	}

	public int getLevel(Skill lvl) {
		return script.getSkills().getStatic(lvl);
	}

	public int getExp(Skill lvl) {
		return script.getSkills().getExperience(lvl);
	}
	
	public double round(double num, int multipleOf) {
		return Math.round((num + multipleOf / 2) / multipleOf) * multipleOf;
	}

	public int drawCustom(int x, int y, PaintItem... p) {
		if (graphics == null) {
			return 0;
		}
		x = x + 8;
		int exp = script.getExperienceTracker().getGainedXP(mySkill);
		int level = script.getSkills().getStatic(mySkill);
		int xpHour = 0;

		if (exp > 0) {
			xpHour = (int) ((3600000.0 / (double) getTime()) * exp);
		}
		
		long TTL = 0;
		int ultimateLevel = 99;
		if (level < 90) {
			ultimateLevel = (int) round(level, 10);
		}
		if (exp > 0 && level != 99) {
			if (level > 0) {
				TTL = skillManager.getTimeTillLevel(mySkill.getId(), exp, ultimateLevel, getTime());
			}
		}

		String xpHourString = "";
		if (exp > 0 && xpHour > 0) {
			xpHourString = "^7(" + colorTheme + "" + df.format(xpHour)
					+ "^7/hr)";
		} else {
			xpHourString = "^7(calculating...)";
		}
		drawSpace(x - 8, y - 23, 216, 25);

		String experience = "^7(calculating...)";
		if (exp > 0) {
			experience = "^7Exp " + colorTheme + "" + df.format(exp) + "^7 "
					+ xpHourString;
		}
		drawString(experience, x, y - 4, FONT_OBJECTIVE | FONT_SHADOW
				| FONT_CENTER);
		y += 25;

		x -= 6;

		drawString("Current", x, y, FONT_OBJECTIVE | FONT_SHADOW);
		y += 15;
		if (level != 99) {
			if (exp > 0) {
				drawString(
						"  level-up "
								+ colorTheme
								+ ""
								+ df.format(getExpLeft(mySkill))
								+ "^7xp ("
								+ formatTime(getTTL(mySkill,
										(getLevel(mySkill) + 1), exp, getTime()))
								+ "^7)", x, y, FONT_OBJECTIVE | FONT_SHADOW);
			} else {
				drawString("- calculating...", x + 5, y, FONT_OBJECTIVE
						| FONT_CENTER | FONT_SHADOW);
			}
		}
		y += 15;
		y += 15;
		drawString("Goals", x, y, FONT_OBJECTIVE | FONT_SHADOW);
		y += 15;
		drawString(
				"  exp to " + ultimateLevel + " " + colorTheme + ""
						+ df.format(getExpLeft(exp, ultimateLevel))
						+ "^7xp", x, y, FONT_OBJECTIVE | FONT_SHADOW);
		y += 15;
		drawString("  time to " + ultimateLevel + " " + formatTime(TTL) + "",
				x, y, FONT_OBJECTIVE | FONT_SHADOW);
		y += 15;
		y += 15;

		for (PaintItem pi : p) {
			if (!pi.isDrawCondition()) {
				continue;
			}
			drawString(pi.getItem(), x, y, FONT_OBJECTIVE | FONT_SHADOW);
			y += 15;
		}
		return y;
	}

	public void drawSpace(int x, int y, int w, int h) {
		if (graphics == null) {
			return;
		}
		((Graphics2D) graphics).setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.75f));
		graphics.setColor(Color.BLACK);
		graphics.fillRect(x, y, w, h);
		((Graphics2D) graphics).setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1f));
	}

	public long getTTL(Skill index, int level, int xpGained, long elapsedTime) {
		return (long) ((elapsedTime * (xpTable[level] - getExp(index))) / xpGained);
	}

	public int getExpLeft(final Skill index) {
		final int lvl = script.getSkills().getStatic(index);
		if (lvl == 99) {
			return 0;
		}
		final int xpTotal = xpTable[lvl + 1] - xpTable[lvl];
		if (xpTotal == 0) {
			return 0;
		}
		final int xpDone = script.getSkills().getExperience(index)
				- xpTable[lvl];
		return xpTotal - xpDone;
	}

	public int getExpLeft(final Skill index, int level) {
		final int lvl = script.getSkills().getStatic(index);
		if (lvl == 99) {
			return 0;
		}
		final int xpTotal = xpTable[level] - xpTable[lvl];
		if (xpTotal == 0) {
			return 0;
		}
		final int xpDone = script.getSkills().getExperience(index)
				- xpTable[lvl];
		return xpTotal - xpDone;
	}

	public int getExpLeft(final int exp, int level) {
		int lvl = this.getLevelForXP(exp);
		if (lvl == 99) {
			return 0;
		}
		final int xpTotal = xpTable[level] - xpTable[lvl];
		if (xpTotal == 0) {
			return 0;
		}
		final int xpDone = exp - xpTable[lvl];
		return xpTotal - xpDone;
	}

	public int getCombatExp() {
		return combatExp;
	}

	public void setCombatExp(int combatExp) {
		this.combatExp = combatExp;
	}

	public static int[] xpTable = { 0, 0, 83, 174, 276, 388, 512, 650, 801,
			969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973,
			4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
			13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
			33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
			83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
			184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
			407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
			899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
			1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
			3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
			7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431 };

	public DecimalFormat df = new DecimalFormat();

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// if (s.actionTimer > 0) {
		// return;
		// }
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// if (s.actionTimer > 0) {
		// return;
		// }
	}

	public void drawSwitch() {
		if (graphics == null) {
			return;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		if (e.getModifiers() != InputEvent.BUTTON1_MASK) {
			/*
			 * Right click support will be added in future For more details on
			 * click
			 */
			return;
		}
		for (CzarButton b : buttons) {
			if (b == null) {
				continue;
			}
			if (mouseX >= b.x && mouseX <= b.x + b.w && mouseY >= b.y
					&& mouseY <= b.y + b.h) {
				script.log("Tab [" + b.buttonName + "] has no PaintTab");
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// if (s.actionTimer > 0) {
		// return;
		// }
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// if (s.actionTimer > 0) {
		// return;
		// }
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// if (s.actionTimer > 0) {
		// return;
		// }
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public class CzarButton {
		public String buttonName;
		public int x, y, w, h;

		public CzarButton(String n, int x, int y, int w, int h) {
			this.buttonName = n;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		public void draw(Graphics2D g) {
			if (g == null) {
				return;
			}
			drawSpace(x, y, w, h);
			drawString(colorTheme + buttonName, x, y + 20, FONT_OBJECTIVE
					| FONT_CENTER | FONT_SHADOW);
		}
	}
}