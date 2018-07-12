package de.wingaming.remake;

import de.wingaming.remake.ui.GameUI;
import de.wingaming.remake.ui.UIMainMenue;
import javafx.animation.AnimationTimer;

public class Renderer extends AnimationTimer {
	
	private GameUI menue;
	
	public Renderer() {
		setMenue(UIMainMenue.MENUE_ISNTANCE);
	}
	
	public void handle(long arg0) {
//		Main.gc.clearRect(0, 0, Main.WIDTH, Main.HEIGHT); //TODO: Remove?
		
		if (menue != null) {
			menue.update();
			menue.render();
		}
	}
	
	public void setMenue(GameUI menue) {
		this.menue = menue;
	}
}
