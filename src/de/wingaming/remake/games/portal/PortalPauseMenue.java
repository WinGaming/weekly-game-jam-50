package de.wingaming.remake.games.portal;

import java.util.ArrayList;
import java.util.List;

import de.wingaming.remake.Main;
import de.wingaming.remake.image.ImageManager;
import de.wingaming.remake.input.KeyboardManager;
import de.wingaming.remake.ui.GameUI;
import de.wingaming.remake.ui.text.TextOption;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PortalPauseMenue implements GameUI {

	private int index = 0;
	private List<TextOption> options = new ArrayList<>();
	private Color background = new Color(0, 0, 0, 0.75);
	private Image arrow = ImageManager.getImage("menue/arrow");
	
	PortalPauseMenue() {
		options.add(new TextOptionResume());
		options.add(new TextOptionGameExit());
	}
	
	public void update() {
		Main.scene.setCursor(Cursor.NONE);
		
		if (KeyboardManager.isDown(KeyCode.DOWN)) {
			KeyboardManager.release(KeyCode.DOWN);
			
			index++;
			if (index >= options.size()) {
				index = 0;
			}
		}
		if (KeyboardManager.isDown(KeyCode.UP)) {
			KeyboardManager.release(KeyCode.UP);
			
			index--;
			if (index < 0) {
				index = options.size() - 1;
			}
		}
		
		if (KeyboardManager.isDown(KeyCode.ENTER)) {
			KeyboardManager.release(KeyCode.ENTER);
			options.get(index).perform();
		}
	}
	
	public void render() {
		GraphicsContext gc = Main.gc;
		
		gc.setFill(background);
		gc.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		
		gc.setFont(Font.font(30));
		gc.setFill(Color.WHITE);
		for (int i = 0; i < options.size(); i++) {
			gc.fillText(options.get(i).getText(), 30, Main.HEIGHT / 2 - (35 * options.size()) / 2 + 35 * i);
		}
		
		gc.drawImage(arrow, 30 + 10 + 2*new Text(0, 0, options.get(index).getText()).getLayoutBounds().getWidth(), Main.HEIGHT / 2 - (35 * options.size()) / 2 + 35 * index - 30/2 - 25/4, 100, 25);
		
		
//		Main.gc.fillText("Move your mouse to change the rotation", 400, Main.HEIGHT/4);
//		Main.gc.fillText("Roll through the portals to get teleported", 400, Main.HEIGHT/4 + 35);
//		Main.gc.fillText("Hold a mouse-button to aim and press space to shoot portals", 400, Main.HEIGHT/4 + 70);
	}
}
