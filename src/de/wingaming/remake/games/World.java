package de.wingaming.remake.games;

import java.util.ArrayList;
import java.util.List;

import de.wingaming.remake.Main;
import de.wingaming.remake.games.portal.Checkpoint;
import de.wingaming.remake.utils.Direction;
import de.wingaming.remake.utils.Location;
import javafx.scene.canvas.GraphicsContext;

public class World {
	
	public static final double DEBUG_FACTOR = 0.00001;
	public static double TILE_SIZE = 32;
	private TileType[][] tiles = new TileType[512][512];
	
	private List<Checkpoint> checkpoints = new ArrayList<>();
	
	private Location playerPosition = new Location(0, 0);
	private Location bluePosition = null;
	private Location orangePosition = null;
	private Location goalPosition = null;
	private Direction blueDirection, orangeDirection;
	
	private TileType fallback = null;
	private int textID = -1;

	public void setPlayerPosition(Location playerPosition) {
		this.playerPosition = playerPosition;
	}
	
	public void setBluePosition(Location bluePosition) {
		this.bluePosition = bluePosition;
	}
	
	public void setOrangePosition(Location orangePosition) {
		this.orangePosition = orangePosition;
	}
	
	public void setGoalPosition(Location goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	public void setTextID(int textID) {
		this.textID = textID;
	}
	
	public Location getPlayerPosition() {
		return playerPosition;
	}
	
	public Location getBluePosition() {
		return bluePosition;
	}
	
	public Location getOrangePosition() {
		return orangePosition;
	}
	
	public Location getGoalPosition() {
		return goalPosition;
	}
	
	public void setBlueDirection(Direction blueDirection) {
		this.blueDirection = blueDirection;
	}
	
	public void setOrangeDirection(Direction orangeDirection) {
		this.orangeDirection = orangeDirection;
	}
	
	public Direction getBlueDirection() {
		return blueDirection;
	}
	
	public Direction getOrangeDirection() {
		return orangeDirection;
	}
	
	public int getTextID() {
		return textID;
	}
	
	public void setFallback(TileType fallback) {
		this.fallback = fallback;
	}
	
	public List<Checkpoint> getCheckpoints() {
		return checkpoints;
	}
	
	public void setCheckpoints(List<Checkpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}
	
	public void setTile(int x, int y, TileType type) {
		tiles[y][x] = type;
	}
	
	public void render() {
		GraphicsContext gc = Main.gc;
		
		double xInSight = Main.WIDTH/TILE_SIZE + 16;
		double yInSight = Main.HEIGHT/TILE_SIZE + 16;
		
		for (int y = -8; y < yInSight; y++) {
			for (int x = -8; x < xInSight; x++) {
				if (getTileTypeAt(x, y) == null) continue;
				
				gc.drawImage(getTileTypeAt(x, y).getTexture(), TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
			}
		}
		
		for (Checkpoint checkpoint : checkpoints) {
			gc.drawImage(TileType.TILE_CHECKPOINT.getTexture(), TILE_SIZE * checkpoint.getPosition().getX(), TILE_SIZE * checkpoint.getPosition().getY(), TILE_SIZE, TILE_SIZE);
		}
	}
	
	//TODO:
	public Location collide(Location startLocation, double dx, double dy, double width, double height, CollideFallback feedback) {
		Location targetLocation = startLocation.clone().add(dx, dy);
		Location result = targetLocation.clone();
		
		Location topLeft = result.clone();
		Location bottomRight = result.clone().add(width, height);
		
		if (dy != 0) {
			Location toLocation = startLocation.clone().add(0, dy);
			Location newLocation = startLocation.clone();
			
			int xCount = bottomRight.getTileX() - topLeft.getTileX() + 1;
			List<Location> testLocations = new ArrayList<>();
			
			if (dy > 0) {
				for (int i = 0; i < xCount; i++) {
					testLocations.add(new Location(toLocation.getX()+i*TILE_SIZE, toLocation.getY()+height));
				}
				
				if (collide(testLocations)) {
					newLocation.setY(toLocation.clone().add(0, height).getTileY()*TILE_SIZE-height-DEBUG_FACTOR);
					feedback.collideY(true);
				} else {
					newLocation.add(0, dy);
				}
			} else if (dy < 0) {
				for (int i = 0; i < xCount; i++) {
					testLocations.add(new Location(toLocation.getX()+i*TILE_SIZE, toLocation.getY()));
				}
				
				if (collide(testLocations)) {
					newLocation.setY(toLocation.getTileY()*TILE_SIZE+TILE_SIZE+DEBUG_FACTOR);
					feedback.collideY(false);
				} else {
					newLocation.add(0, dy);
				}
			}
			
			return collide(newLocation, dx, 0, width, height, feedback);
		} else if (dx != 0) {
			Location toLocation = startLocation.clone().add(dx, 0);
			Location newLocation = startLocation.clone();
			
			int yCount = bottomRight.getTileY() - topLeft.getTileY() + 1;
			List<Location> testLocations = new ArrayList<>();
			
			if (dx > 0) {
				for (int i = 0; i < yCount; i++) {
					testLocations.add(new Location(toLocation.getX()+width, toLocation.getY()+i*TILE_SIZE));
				}
				
				if (collide(testLocations)) {
					newLocation.setX(toLocation.clone().add(width, 0).getTileX()*TILE_SIZE-width-DEBUG_FACTOR);
					feedback.collideX();
				} else {
					newLocation.add(dx, 0);
				}
			} else if (dx < 0) {
				for (int i = 0; i < yCount; i++) {
					testLocations.add(new Location(toLocation.getX(), toLocation.getY()+i*TILE_SIZE));
				}
				
				if (collide(testLocations)) {
					newLocation.setX(toLocation.getTileX()*TILE_SIZE+TILE_SIZE+DEBUG_FACTOR);
					feedback.collideX();
				} else {
					newLocation.add(dx, 0);
				}
			}
			
			return collide(newLocation, 0, dy, width, height, feedback);
		} else {
			return targetLocation;
		}
	}
	
	private boolean collide(List<Location> locations) {
		for (Location location : locations) {
			int x = location.getTileX();
			int y = location.getTileY();
			
			if (getTileTypeAt(x, y) != null) {
				if (getTileTypeAt(x, y).isSolid()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public TileType[][] getTiles() {
		return tiles;
	}

	public TileType getTileTypeAt(int x, int y) {
		if (x < 0 || y < 0 || y > tiles.length-1 || x > tiles[y].length-1) return fallback;
		
		return tiles[y][x] == null ? fallback : tiles[y][x];
	}

	public Checkpoint getCheckpointAt(int tileX, int tileY) {
		for (Checkpoint checkpoint : checkpoints) {
			if (checkpoint.getPosition().getX() == tileX && checkpoint.getPosition().getY() == tileY) return checkpoint;
		}
		
		return null;
	}
}
