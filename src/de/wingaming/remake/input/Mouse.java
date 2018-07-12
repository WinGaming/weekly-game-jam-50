package de.wingaming.remake.input;

import javafx.scene.input.MouseButton;

public class Mouse {
	
	private static boolean pressed;
	private static MouseButton button;
	
	public static void setPressed(boolean pressed) {
		Mouse.pressed = pressed;
	}
	
	public static boolean isPressed() {
		return pressed;
	}
	
	public static MouseButton getLastutton() {
		return button;
	}

	public static void setLastButton(MouseButton button) {
		Mouse.button = button;
	}
}