package de.wingaming.remake.utils;

public class Vector2d {
	
	private double x, y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector2d value) {
		this.x += value.getX();
		this.y += value.getY();
	}
	
	public void add(double value) {
		this.x += value;
		this.y += value;
	}
	
	public void multiply(double value) {
		this.x *= value;
		this.y *= value;
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
}
