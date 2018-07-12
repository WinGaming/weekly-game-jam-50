package de.wingaming.remake.games.portal;

import de.wingaming.remake.Main;
import de.wingaming.remake.games.Level;
import de.wingaming.remake.input.KeyboardManager;
import de.wingaming.remake.ui.GameUI;
import de.wingaming.remake.ui.levels.LevelSelect;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UILevelEnd implements GameUI {
	
	private Level level;
	private long time;
	
	public UILevelEnd(Level level, long time) {
		this.level = level;
		this.time = time;
	}
	
	public void update() {
		if (KeyboardManager.isDown(KeyCode.ENTER)) {
			KeyboardManager.release(KeyCode.ENTER);
			
			Main.renderer.setMenue(LevelSelect.UI_INSTANCE);
		}
	}
	
	public void render() {
		GraphicsContext gc = Main.gc;
		
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		
		gc.setFill(Color.WHITE);
		gc.setFont(Font.font(70));
		gc.fillText(level.getTitle(), Main.WIDTH/2.5, 200);
		gc.fillRect(30, Main.HEIGHT/2, Main.WIDTH - 60, 10);
		
		int min = (int) (time / (1000*60));
		int sec = (int) ((time - min*1000*60) / 1000);
		int mil = (int) (time - sec*1000 - min*1000*60);
		
		String minS = min < 10 ? "0" + min : min+"";
		String secS = sec < 10 ? "0" + sec : sec+"";
		String milS = mil < 10 ? "0" + mil : mil+"";
		gc.fillText(minS + ":" + secS + ":" + milS, Main.WIDTH/2.6, Main.HEIGHT/2 + 200);
		
		gc.setFont(Font.font(20));
		gc.fillText("ENTER", Main.WIDTH - 100, Main.HEIGHT - 40);
	}
}
