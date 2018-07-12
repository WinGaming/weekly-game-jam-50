package de.wingaming.remake.games;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.wingaming.remake.games.portal.Checkpoint;
import de.wingaming.remake.utils.Direction;
import de.wingaming.remake.utils.Location;

public class Loader {
	
	public static World loadWorld(String fileName) {
		try {
//			InputStream is = Main.class.getResourceAsStream("saves/"+fileName+".world");
			File file = new File("saves/"+fileName+".world");
			if (!file.exists()) {
				World w = new World();
				
				return w;
			}

			World world = new World();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			List<Checkpoint> checkpoints = new ArrayList<>();
			
			String line = null;
			while((line = reader.readLine()) != null) {
				if (line.contains(">>")) {
					String[] data = line.split(">>");
					int dx = 0;
					int y = Integer.parseInt(data[0]);
					String blockString = data[1];
					
					String[] blocksdata = blockString.split(";");
					
					for (String blockdata : blocksdata) {
						int count;
						TileType type;
						
						if (!blockdata.contains(",")) {
							count = 1;
							type = TileType.getType(blockdata);
						} else {
							String[] blockdataData = blockdata.split(",");
							count = Integer.parseInt(blockdataData[0]);
							type = TileType.getType(blockdataData[1]);
						}
						
						for (int i = 0; i < count; i++) {
							world.setTile(i+dx, y, type);
						}

						dx += count;
					}
				} else if (line.startsWith("checks:")) {
					String[] data = line.split(":");
					String[] positionData = data[1].split(";");
					
					for (String position : positionData) {
						int x = Integer.parseInt(position.split(",")[0]);
						int y = Integer.parseInt(position.split(",")[1]);
						
						checkpoints.add(new Checkpoint(new Point(x, y)));
					}
				} else if (line.startsWith("blue:")) {
					String[] data = line.split(":");
					
					int x = Integer.parseInt(data[1].split(",")[0]);
					int y = Integer.parseInt(data[1].split(",")[1]);
					Direction direction = Direction.valueOf(data[1].split(",")[2]);
					
					world.setBluePosition(new Location(x, y));
					world.setBlueDirection(direction);
				} else if (line.startsWith("orange:")) {
					String[] data = line.split(":");
					
					int x = Integer.parseInt(data[1].split(",")[0]);
					int y = Integer.parseInt(data[1].split(",")[1]);
					Direction direction = Direction.valueOf(data[1].split(",")[2]);
					
					world.setOrangePosition(new Location(x, y));
					world.setOrangeDirection(direction);
				} else if (line.startsWith("player:")) {
					String[] data = line.split(":");
					
					int x = Integer.parseInt(data[1].split(",")[0]);
					int y = Integer.parseInt(data[1].split(",")[1]);
					
					world.setPlayerPosition(new Location(x, y));
				} else if (line.startsWith("text:")) {
					String[] data = line.split(":");
					
					world.setTextID(Integer.parseInt(data[1]));
				} else if (line.startsWith("goal:")) {
					String[] data = line.split(":");
					
					int x = Integer.parseInt(data[1].split(",")[0]);
					int y = Integer.parseInt(data[1].split(",")[1]);
					
					world.setGoalPosition(new Location(x, y));
				}
			}
			
			world.setCheckpoints(checkpoints);

			reader.close();
			
			return world;
		} catch (IOException ex) {
			ex.printStackTrace();
			World w = new World();
			
			return w;
		}
	}
	
	public static void saveWorld(String name, World world, Location player, List<Checkpoint> checkpoints, int text, Location goal) {
		File file = new File("saves/"+name+".world");
		
		try {
			if (!file.exists()) file.createNewFile();
			
			TileType lastType = null;
			int typeCount = 0;
			
			StringBuilder builder = new StringBuilder();
			for (int y = 0; y < world.getTiles().length; y++) {
				StringBuilder lineBuilder = new StringBuilder();
				lineBuilder.append(y+">>");
				
				for (int x = 0; x < world.getTiles()[y].length; x++) {
					TileType type = world.getTileTypeAt(x, y);
					
					if (type != lastType) {
						if (typeCount == 1)
							lineBuilder.append(TileType.getSymbol(lastType)+";");
						else
							lineBuilder.append(typeCount+","+TileType.getSymbol(lastType)+";");
						
						typeCount = 1;
						lastType = type;
					} else {
						typeCount++;
					}
				}
				
				typeCount = 0;
				
				lineBuilder.append(typeCount+","+TileType.getSymbol(lastType));
				
				if (!lineBuilder.toString().endsWith(">>0,&")) {
					builder.append(lineBuilder.toString() + "\n");
				}
			}
			
			builder.append("\n");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(builder.toString());
			
			//Location player, List<Checkpoint> checkpoints, int text, Location goal
			
			writer.write("player:" + ((int) player.getX()) + "," + ((int) player.getY()));
			writer.newLine();
			
			StringBuilder checkpointString = new StringBuilder();
			
			checkpointString.append("checks:");
			for (Checkpoint checkpoint : checkpoints) {
				checkpointString.append(checkpoint.getPosition().getX() + "," + checkpoint.getPosition().getY() + ";");
			}
			writer.write(checkpointString.substring(0, checkpointString.length()-1).replace(".0", ""));
			writer.newLine();
			
			writer.write("text:" + text);
			writer.newLine();
			
			writer.write("goal:" + ((int) goal.getX()) + "," + ((int) goal.getY()));
			
			writer.close();
			
			System.out.println("Saved!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}