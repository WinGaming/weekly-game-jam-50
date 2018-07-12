package de.wingaming.remake.ui.text;

import de.wingaming.remake.Main;
import de.wingaming.remake.ui.levels.LevelSelect;

public class TextOptionPlay implements TextOption {
	
	public String getText() {
		return "Play";
	}
	
	public void perform() {
		Main.renderer.setMenue(LevelSelect.UI_INSTANCE);
	}
}
