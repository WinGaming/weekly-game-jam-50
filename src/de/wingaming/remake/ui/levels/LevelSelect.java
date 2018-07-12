package de.wingaming.remake.ui.levels;

import de.wingaming.remake.Main;
import de.wingaming.remake.games.Level;
import de.wingaming.remake.games.LevelManager;
import de.wingaming.remake.games.Loader;
import de.wingaming.remake.games.portal.Portal;
import de.wingaming.remake.image.ImageManager;
import de.wingaming.remake.input.KeyboardManager;
import de.wingaming.remake.ui.GameUI;
import de.wingaming.remake.ui.UIMainMenue;
import de.wingaming.remake.ui.text.TextOptionBack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LevelSelect implements GameUI {
	
	private int selectIndex = 0;
	private TextOptionBack back = new TextOptionBack();
	private Image arrow = ImageManager.getImage("menue/arrow");
	public static LevelSelect UI_INSTANCE = new LevelSelect();
	
	private int levelsPerLine = 5;
	
	public void update() {
		if (KeyboardManager.isDown(KeyCode.RIGHT)) {
			KeyboardManager.release(KeyCode.RIGHT);
			selectIndex++;
			
			if (selectIndex > LevelManager.getLevels().size()) {
				selectIndex = 0;
			}
		} else if (KeyboardManager.isDown(KeyCode.LEFT)) {
			KeyboardManager.release(KeyCode.LEFT);
			selectIndex--;
			
			if (selectIndex < 0) {
				selectIndex = LevelManager.getLevels().size();
			}
		} else if (KeyboardManager.isDown(KeyCode.ENTER)) {
			if (selectIndex == LevelManager.getLevels().size()) {
				Main.renderer.setMenue(UIMainMenue.MENUE_ISNTANCE);
				selectIndex = 0;
			} else {
				Portal.GAME_INSTANCE.loadWorld(Loader.loadWorld(LevelManager.getLevels().get(selectIndex).getWorldFile()), LevelManager.getLevels().get(selectIndex));
				Main.renderer.setMenue(Portal.GAME_INSTANCE);
			}
			
			KeyboardManager.release(KeyCode.ENTER);
		} else if (KeyboardManager.isDown(KeyCode.DOWN)) {
			KeyboardManager.release(KeyCode.DOWN);
			selectIndex += levelsPerLine;
			
			if (selectIndex == LevelManager.getLevels().size() + levelsPerLine) {
				selectIndex = 0;
			} else if (selectIndex > LevelManager.getLevels().size()) {
				selectIndex = LevelManager.getLevels().size();
			}
		} else if (KeyboardManager.isDown(KeyCode.UP)) {
			KeyboardManager.release(KeyCode.UP);
			selectIndex -= levelsPerLine;
			
			if (selectIndex == LevelManager.getLevels().size() - levelsPerLine) {
				selectIndex = LevelManager.getLevels().size() - 1;
			} else if (selectIndex < 0) {
				selectIndex = LevelManager.getLevels().size();
			}
		}
	}

	public void render() {
		GraphicsContext gc = Main.gc;
		
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		
		double width = (Main.WIDTH - 60 - (levelsPerLine-1)*10) / levelsPerLine;
		for (int j = 0; j < LevelManager.getLevels().size(); j++) {
			int i = j;
			Level level = LevelManager.getLevels().get(i);
			
			int dy = 0;
			while (i >= levelsPerLine) {
				dy++;
				i -= levelsPerLine;
			}
			
			Color prim = Color.BLACK;
			Color seco = Color.WHITE;
			
			if (j == selectIndex) {
				prim = Color.WHITE;
				seco = Color.BLACK;
			}
			
			double x = 30 + width*i + 10*i;
			double y = 30 + dy*110;
			
			gc.setFill(prim);
			gc.fillRect(x, y, width, 100);
			
			gc.setFill(seco);
			gc.setFont(Font.font(35));
			gc.fillRect(x + 5, y + 100/2, width - 10, 5);
			gc.fillText(level.getTitle(), x + 5, y + 100/2 - 10);
			
			gc.setFont(Font.font(35));
			
			String timeS = LevelManager.getTime(level.getWorldFile()) == -1 ? "--:--" : createTimeString(LevelManager.getTime(level.getWorldFile()));
			gc.fillText(timeS, x + 5, y + 100/2 + 5 + 35);
		}
		
		gc.setFill(Color.WHITE);
		gc.setFont(Font.font(35));
		gc.fillText(back.getText(), 30, Main.HEIGHT - 30);
		
		if (selectIndex == LevelManager.getLevels().size()) gc.drawImage(arrow, 130, Main.HEIGHT - 30 - 25, 100, 25);
	}
	
	private String createTimeString(long time) {
		int min = (int) (time / (1000*60));
		int sec = (int) ((time - min*1000*60) / 1000);
		int mil = (int) (time - sec*1000 - min*1000*60);
		
		String minS = min < 10 ? "0" + min : min+"";
		String secS = sec < 10 ? "0" + sec : sec+"";
		String milS = mil < 10 ? "0" + mil : mil+"";
		
		return minS + ":" + secS + ":" + milS;
	}
}
