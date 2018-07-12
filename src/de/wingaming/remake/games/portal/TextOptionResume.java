package de.wingaming.remake.games.portal;

import de.wingaming.remake.ui.text.TextOption;

public class TextOptionResume implements TextOption {
	
	public String getText() {
		return "Resume";
	}
	
	public void perform() {
		Portal.GAME_INSTANCE.setPaused(false);
	}
}
