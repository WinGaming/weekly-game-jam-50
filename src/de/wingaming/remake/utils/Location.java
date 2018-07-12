package de.wingaming.remake.utils;

import de.wingaming.remake.games.World;

public class Location {
	
	private double x, y;
	
	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Location increace(double dx, double dy) {
		this.x += dx;
		this.y += dy;
		
		return this;
	}
	
	//TODO:
	public Location add(double dx, double dy) {
		return increace(dx, dy);
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Location clone() {
		return new Location(x, y);
	}

	public int getTileX() {
		return (int) (x / World.TILE_SIZE);
	}
	
	public int getTileY() {
		return (int) (y / World.TILE_SIZE);
	}
	
	public boolean isSame(Location loc) {
		if (loc == null) return false;
		return this.x == loc.getX() && this.y == loc.getY();
	}
}
