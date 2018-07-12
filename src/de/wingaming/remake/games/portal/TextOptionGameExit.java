package de.wingaming.remake.games.portal;

import de.wingaming.remake.Main;
import de.wingaming.remake.ui.UIMainMenue;
import de.wingaming.remake.ui.text.TextOption;

public class TextOptionGameExit implements TextOption {
	
	public String getText() {
		return "Exit";
	}
	
	public void perform() {
		Main.renderer.setMenue(UIMainMenue.MENUE_ISNTANCE);
	}
}