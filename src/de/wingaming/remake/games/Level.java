package de.wingaming.remake.games;

public class Level {
	
	private String worldFile;
	private String title;
	
	public Level(String world, String title) {
		this.worldFile = world;
		this.title = title;
	}
	
	public String getWorldFile() {
		return worldFile;
	}
	
	public String getTitle() {
		return title;
	}
}
