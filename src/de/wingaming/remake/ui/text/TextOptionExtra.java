package de.wingaming.remake.ui.text;

import de.wingaming.remake.Main;
import de.wingaming.remake.ui.GameUICommingSoon;

public class TextOptionExtra implements TextOption {
	
	public String getText() {
		return "Extras";
	}
	
	public void perform() {
		Main.renderer.setMenue(GameUICommingSoon.MENUE_INSTANCE);
	}
}
