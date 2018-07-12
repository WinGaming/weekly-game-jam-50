package de.wingaming.remake.input;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;

public class KeyboardManager {
	
	private static List<KeyCode> keys = new ArrayList<>();
	
	public static void press(KeyCode key) {
		if (!keys.contains(key)) keys.add(key);
	}
	
	public static void release(KeyCode key) {
		if (keys.contains(key)) keys.remove(key);
	}
	
	public static boolean isDown(KeyCode key) {
		return keys.contains(key);
	}
}