package de.wingaming.remake.ui.text;

public class TextOptionExit implements TextOption {
	
	public String getText() {
		return "Exit";
	}
	
	public void perform() {
		System.out.println("Exit game");
		System.exit(0);
	}
}
