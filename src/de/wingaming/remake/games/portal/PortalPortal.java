package de.wingaming.remake.games.portal;

import de.wingaming.remake.utils.Direction;
import de.wingaming.remake.utils.Location;

public class PortalPortal {
	
	private Location position;
	private Direction facing;

	public PortalPortal(Location position, Direction facing) {
		this.position = position;
		this.facing = facing;
	}
	
	public Location getPosition() {
		return position;
	}
	
	public void setPosition(Location position) {
		this.position = position;
	}
	
	public Direction getFacing() {
		return facing;
	}
	
	public void setFacing(Direction facing) {
		this.facing = facing;
	}
}
