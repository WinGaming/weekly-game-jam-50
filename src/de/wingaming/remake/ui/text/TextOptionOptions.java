package de.wingaming.remake.ui.text;

import de.wingaming.remake.Main;
import de.wingaming.remake.ui.GameUICommingSoon;

public class TextOptionOptions implements TextOption {
	
	public String getText() {
		return "Options";
	}
	
	public void perform() {
		Main.renderer.setMenue(GameUICommingSoon.MENUE_INSTANCE);
	}
}
