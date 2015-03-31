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
import com.blaster.data.StateId;
import com.blaster.data.TransitionId;
import com.blaster.state.*;
import com.blaster.util.Paint;
import com.blaster.util.PaintItem;
import com.blaster.util.ScriptTimer;

@ScriptManifest(author = "Czar, Eliot", info = "Blast furnace professional and sophisticated programming", name = "DreamBlaster 2", version = 1.0, logo = "http://i.imgur.com/zKm3G3U.png")
public class DreamBlaster extends Script {

	/**
	 * Ore the player chooses (handled in GUI)
	 */
	private OreData chosenOre = OreData.IRON;
	/**
	 * Node list represents the actions in the script
	 */
	private ArrayList<ScriptState> nodes = new ArrayList<>();
	/**
	 * For debugging purposes
	 */
	private ScriptState currentNode;
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
	private ScriptTimer timeElapsed = new ScriptTimer().start();
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
	private BlasterGUI gui = new BlasterGUI(this);
	/**
	 * Bars made
	 */
	private int barsMade;

	public void onStart() {
		paint = new Paint(this, "Dream", "^3Blaster", "^3", Skill.SMITHING);
		paint.mySkill = Skill.SMITHING;
		gui = new BlasterGUI(this);
		cfg = new BlastConfig(this);
		getExperienceTracker().startAll();
		gui.setVisible(true);
		Collections.addAll(nodes, new Banking(this));

		ScriptState bankState = new Banking(this);

		// Keep it dynamic just to ensure no bugs.
		if (getSkills().getDynamic(Skill.SMITHING) < 30) {
			bankState.addTransition(TransitionId.PAY, StateId.PUT);
		} else {
			bankState.addTransition(TransitionId.BANK, StateId.PUT);
		}

		ScriptState actionState = new PutOre(this);

		if (isLeechMode()) {
			actionState.addTransition(TransitionId.PUT, StateId.BANK);
		} else {
			// TODO add rest of states (sequenced) here depending on if user chose it/can do
			if (isPump()) {
				actionState.addTransition(TransitionId.PUT, StateId.PUMP);
			}
		}

		Collections.addAll(nodes, bankState);

		log("// DreamBlaster initialized");
	}

	@Override
	public int onLoop() throws InterruptedException {
		synchronized (this) {
			for (ScriptState node : nodes) {
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

	public String getState() {
		if (currentNode != null) {
			return currentNode.getClass().getSimpleName();
		}
		return "";
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

	public ScriptState getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(ScriptState currentNode) {
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

	@Override
	public void onPaint(Graphics2D g) {
		if (paint == null) {
			return;
		}
		if (!gui.isReady) {
			// return;
		}
		// PaintItem items = new PaintItem("Items", true, true);
		// PaintItem fish = new PaintItem("  Bars ^3 " + getBarsMade() +
		// " ^7(^3"
		// + paint.df.format(paint.perHour(getBarsMade())) + "^7/hr)",
		// true, true);
		// PaintItem prof = new PaintItem("  Profit ^3 " + getBarsMade()
		// + " ^7(^3" + paint.df.format(paint.perHour(getBarsMade()))
		// + "^7/hr)", true, true);

		PaintItem[] paints = {
				// items, fish, prof,

				new PaintItem("[DEV] ^3" + getState(), true, true),

				new PaintItem("Coal config ^3" + cfg.getCoalAmount(), true,
						true),

		};

		paint.paint(g, paints);
	}
}
