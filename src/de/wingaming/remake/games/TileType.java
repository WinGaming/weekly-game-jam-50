package de.wingaming.remake.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.wingaming.remake.image.ImageManager;
import de.wingaming.remake.utils.ImageUtils;
import javafx.scene.image.Image;

public class TileType {
	
	private static List<TileType> tiles = new ArrayList<>();
	
	private static Map<String, TileType> types = new HashMap<>();
	
	public static final TileType TILE_WOOD_1 = new TileType(new Image("file:res/tiles/wood_1.png")).setSolid(true);
	public static final TileType TILE_WOOD_2 = new TileType(new Image("file:res/tiles/wood_2.png")).setSolid(false);
	
	public static final TileType TILE_DIRT = new TileType(new Image("file:res/tiles/dirt.png")).setSolid(true);
	
	public static final TileType TILE_GRASS_DARK = new TileType(new Image("file:res/tiles/grass_dark.png")).setSolid(true);
	public static final TileType TILE_GRASS_DRY = new TileType(new Image("file:res/tiles/grass_dry.png")).setSolid(true);
	public static final TileType TILE_GRASS_LIGHT = new TileType(new Image("file:res/tiles/grass_light.png")).setSolid(true);
	
	public static final TileType TILE_GROUND_1 = new TileType(new Image("file:res/tiles/ground_1.png"));
	public static final TileType TILE_GROUND_2 = new TileType(new Image("file:res/tiles/ground_2.png")).setSolid(true).setPortalable(true);
	public static final TileType TILE_GROUND_3 = new TileType(new Image("file:res/tiles/ground_3.png")).setSolid(true);
	
	public static final TileType TILE_CHECKPOINT = new TileType(ImageManager.getImage("portal/checkpoint"));
	
	static {
		registerType("!", TILE_DIRT);
		
		registerType("\"", TILE_GRASS_DARK);
		registerType("§", TILE_GRASS_DRY);
		registerType("$", TILE_GRASS_LIGHT);
		
		registerType("%", TILE_GROUND_1);
		registerType("&", TILE_GROUND_2);
		registerType("$", TILE_GROUND_3);
	}
	
	private static int i;
	public static void nextType() {
		i++;
		
		if (i >= tiles.size()) {
			i = 0;
		}
	}
	
	public static TileType getCurrentType() {
		return tiles.get(i);
	}
	
	public static void registerType(String symbol, TileType type) {
		tiles.add(type);
		
		types.put(symbol, type);
	}
	
	public static TileType getType(String symbol) {
		return types.get(symbol);
	}
	
	public static String getSymbol(TileType type) {
		for (String symbol : types.keySet()) {
			if (types.get(symbol) == type)
				return symbol;
		}
		
		return "*";
	}
	
	private boolean portalable;
	private boolean solid;
	private Image texture;
	
	private TileType(Image texture) {
		this.texture = ImageUtils.resample(texture, 2);
	}
	
	public TileType setSolid(boolean solid) {
		this.solid = solid;
		return this;
	}
	
	public TileType setPortalable(boolean portalable) {
		this.portalable = portalable;
		return this;
	}
	
	public boolean isPortalable() {
		return portalable;
	}
	
	public Image getTexture() {
		return texture;
	}
	
	public boolean isSolid() {
		return solid;
	}
}