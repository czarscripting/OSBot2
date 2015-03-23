package com.blaster;

import java.util.ArrayList;
import java.util.Collections;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.input.mouse.EntityDestination;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import com.blaster.data.BlastConfig;
import com.blaster.data.OreData;
import com.blaster.node.Banking;
import com.blaster.node.Node;

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

	public void onStart() {
		Collections.addAll(nodes, new Banking(this));

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

	private int triedCamera;

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

}
