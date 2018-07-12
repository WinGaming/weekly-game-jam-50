package de.wingaming.remake.games.portal;

import de.wingaming.remake.games.CollideFallback;
import de.wingaming.remake.games.World;
import de.wingaming.remake.utils.Location;

public class PortalPlayer {
	
	private Location position;
	
	public PortalPlayer(Location position) {
		this.position = position;
	}
	
	public void move(World world, double dx, double dy, CollideFallback collideFallback) {
		this.position = world.collide(position, dx, dy, 20, 20, collideFallback);
	}
	
	public Location getPosition() {
		return position;
	}
	
	public void setPosition(Location position) {
		this.position = position;
	}
}
