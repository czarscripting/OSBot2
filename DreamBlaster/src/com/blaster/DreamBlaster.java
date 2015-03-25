package com.blaster;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.input.mouse.EntityDestination;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import com.blaster.data.BlastConfig;
import com.blaster.data.OreData;
import com.blaster.node.*;
import com.blaster.util.Paint;
import com.blaster.util.PaintItem;
import com.blaster.util.ScriptTimer;

@ScriptManifest(author = "Czar, Eliot", info = "Blast furnace professional and sophisticated programming", name = "DreamBlaster", version = 1.0, logo = "http://i.imgur.com/zKm3G3U.png")
public class DreamBlaster extends Script {

	/**
	 * Ore the player chooses (handled in GUI)
	 */
	private OreData chosenOre = OreData.IRON;
	/**
	 * Node list represents the actions in the script
	 */
	private ArrayList<Node> nodes = new ArrayList<>();
	/**
	 * For debugging purposes
	 */
	private Node currentNode;
	/**
	 * Used for grabbing config data
	 */
	private BlastConfig cfg = new BlastConfig(this);
	/**
	 * User goal
	 */
	private boolean leechMode;
	/**
	 * Attempts at interacting with objects that are far away (>7 tiles)
	 */
	private int triedCamera;
	/**
	 * Paint total time elapsed since start
	 */
	private ScriptTimer timeElapsed;
	/**
	 * Use pipes (30 crafting)
	 */
	private boolean pipes;
	/**
	 * Use pump (30 strength)
	 */
	private boolean pump;
	/**
	 * Use pedals (30 agility)
	 */
	private boolean pedal;
	/**
	 * Use coal bag
	 */
	private boolean bag;
	/**
	 * For users with less than 60 smithing
	 */
	private boolean paying;
	/**
	 * Script paint
	 */
	private Paint paint;
	/**
	 * Script GUI
	 */
	private BlasterGUI gui = new BlasterGUI();
	/**
	 * Bars made
	 */
	private int barsMade;

	public void onStart() {
		gui.setVisible(true);
		getExperienceTracker().startAll();
		paint = new Paint(this, "Dream", "^3Blaster", "^3", Skill.SMITHING);
		Collections.addAll(nodes, new Banking(this));

		if (isLeechMode()) {
			Collections.addAll(nodes, new Banking(this), new PutOre(this),
					new CollectBars(this));
		}

		log("// DreamBlaster initialized");
	}

	@Override
	public int onLoop() throws InterruptedException {
		synchronized (this) {
			for (Node node : nodes) {
				if (!myPlayer().isUnderAttack()) {
					if (node.activate()) {
						node.execute();
						setCurrentNode(node);
					}
				}
			}
		}
		return 200;
	}

	public boolean interactObj(String name, String option) {
		RS2Object o = getObjects().closest(name);
		if (o == null) {
			return false;
		}
		if (myPlayer().isMoving()) {
			if (getMap().getDestination().distance(o.getPosition()) == 0) {
				// interacted with the object successfully, so wait
				return true;
			}
			return false;
		}
		if (o.isVisible()) {
			EntityDestination ent = new EntityDestination(getBot(), o);
			if (ent.isVisible()) {
				if (ent.isHover()) {
					if (o.interact(option)) {
						return true;
					}
				} else {
					getMouse().move(ent);
					return false;
				}
			}
			// TODO maybe walk to nearest adjacent tile?
			// will have to resort to that^ if method doesn't work
			return getCamera().toEntity(o);
		} else {
			int angleTo = angleToTile(o.getPosition());
			if (distance(o) > 7) {
				if (triedCamera == 0) {
					if (o.getPosition().getTileHeight(getBot()) == myPosition()
							.getTileHeight(getBot())) {
						triedCamera++;
						return getCamera().toBottom();
					} else {
						getMap().walk(o.getPosition());
					}
				} else {
					triedCamera = 0;
				}
			} else {
				return getCamera().toEntity(o);
			}
		}
		return false;
	}

	/**
	 * Returns the angle to a given tile in degrees anti-clockwise from the
	 * positive x axis (where the x-axis is from west to east).
	 *
	 * @param t
	 *            The target tile
	 * @return The angle in degrees
	 */
	public int angleToTile(final Position t) {
		final Position me = myPosition();
		final int angle = (int) Math.toDegrees(Math.atan2(t.getY() - me.getY(),
				t.getX() - me.getX()));
		return angle >= 0 ? angle : 360 + angle;
	}

	@Override
	public void onPaint(Graphics2D g) {
		if (!gui.isReady) {
			return;
		}
		PaintItem items = new PaintItem("Items", true, true);
		PaintItem fish = new PaintItem("  Bars ^3 " + getBarsMade() + " ^7(^3"
				+ paint.df.format(paint.perHour(getBarsMade())) + "^7/hr)", true, true);
		PaintItem prof = new PaintItem("  Profit ^3 " + getBarsMade() + " ^7(^3"
				+ paint.df.format(paint.perHour(getBarsMade())) + "^7/hr)",
				true, true);
//		PaintItem state = new PaintItem("State ^3" + getState(),
//				getState() != null, true);

		PaintItem paints[] = { items, fish, prof, };

		paint.paint(g, paints);
	}

	public int distance(Position p) {
		return myPosition().distance(p);
	}

	public int distance(RS2Object o) {
		return myPosition().distance(o.getPosition());
	}

	public OreData getChosenOre() {
		return chosenOre;
	}

	public void setChosenOre(OreData chosenOre) {
		this.chosenOre = chosenOre;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public BlastConfig getCfg() {
		return cfg;
	}

	public void setCfg(BlastConfig cfg) {
		this.cfg = cfg;
	}

	public boolean isLeechMode() {
		return leechMode;
	}

	public void setLeechMode(boolean leechMode) {
		this.leechMode = leechMode;
	}

	public ScriptTimer getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(ScriptTimer timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public boolean isPipes() {
		return pipes;
	}

	public void setPipes(boolean pipes) {
		this.pipes = pipes;
	}

	public boolean isPump() {
		return pump;
	}

	public void setPump(boolean pump) {
		this.pump = pump;
	}

	public boolean isPedal() {
		return pedal;
	}

	public void setPedal(boolean pedal) {
		this.pedal = pedal;
	}

	public boolean isBag() {
		return bag;
	}

	public void setBag(boolean bag) {
		this.bag = bag;
	}

	public boolean isPaying() {
		return paying;
	}

	public void setPaying(boolean paying) {
		this.paying = paying;
	}

	public int getBarsMade() {
		return barsMade;
	}

	public void addBarsMade() {
		this.barsMade++;
	}

}
