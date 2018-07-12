package de.wingaming.remake.image;

import java.util.HashMap;
import java.util.Map;

import de.wingaming.remake.utils.ImageUtils;
import javafx.scene.image.Image;

public class ImageManager {
	
	private static Map<String, Image> images = new HashMap<>();
	
	static {
		setImage("menue/arrow", new Image("file:res/arrow.png"));
		setImage("entity/clippy", ImageUtils.resample(new Image("file:res/clippy.png"), 20));
		setImage("ui/cross", ImageUtils.resample(new Image("file:res/cross.png"), 3));
		setImage("portal/middle", ImageUtils.resample(new Image("file:res/middle.png"), 3));
		setImage("portal/checkpoint", ImageUtils.resample(new Image("file:res/checkpoint.png"), 3));
		setImage("portal/blue", ImageUtils.resample(new Image("file:res/portal_blue.png"), 3));
		setImage("portal/orange", ImageUtils.resample(new Image("file:res/portal_orange.png"), 3));
		setImage("portal/green", ImageUtils.resample(new Image("file:res/portal_green.png"), 3));
		setImage("portal/goal", ImageUtils.resample(new Image("file:res/goal.png"), 3));
	}
	
	public static void setImage(String path, Image image) {
		images.put(path, image);
	}
	
	public static Image getImage(String path) {
		return images.get(path);
	}
}