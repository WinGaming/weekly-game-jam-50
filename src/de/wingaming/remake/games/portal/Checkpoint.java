package de.wingaming.remake.games.portal;

import java.awt.Point;

public class Checkpoint {
	
	private boolean used;
	private Point position;
	
	public Checkpoint(Point position) {
		this.position = position;
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public Point getPosition() {
		return position;
	}
}
