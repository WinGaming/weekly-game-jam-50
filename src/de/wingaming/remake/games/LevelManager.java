package de.wingaming.remake.games;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelManager {
	
	private static List<Level> levels = new ArrayList<>();
	private static Map<String, Long> times = new HashMap<>();
	
	static {
		registerLevel(new Level("level_0", "Tutorial"));
		registerLevel(new Level("level_1", "Portals"));
		registerLevel(new Level("level_2", "Checkpoints"));
		registerLevel(new Level("level_3", "Aim"));
		registerLevel(new Level("level_4", "Jump"));
		registerLevel(new Level("level_5", "Back to start"));
//		registerLevel(new Level("level_6", "No idea"));
		
//		registerLevel(new Level("current", "Current"));
		
		try {
			File file = new File("times.yml");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] data = line.split(":");
				times.put(data[0], Long.parseLong(data[1]));
			}
			
			reader.close();
		} catch (FileNotFoundException ex) {
			//Ignore
		} catch (IOException | NumberFormatException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void registerLevel(Level level) {
		levels.add(level);
	}
	
	public static List<Level> getLevels() {
		return levels;
	}
	
	public static void setTime(String level, long time) {
		long oldTime = getTime(level);
		
		if (oldTime == -1 || time < oldTime) times.put(level, time);
	}
	
	public static long getTime(String level) {
		return times.containsKey(level) ? times.get(level) : -1;
	}
	
	public static void saveTimes() {
		try {
			File file = new File("times.yml");
			
			if (!file.exists()) file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for (String level : times.keySet()) {
				writer.write(level + ":" + times.get(level));
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}