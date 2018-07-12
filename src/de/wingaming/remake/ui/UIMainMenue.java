package de.wingaming.remake.ui;

import java.util.ArrayList;
import java.util.List;

import de.wingaming.remake.Main;
import de.wingaming.remake.image.ImageManager;
import de.wingaming.remake.input.KeyboardManager;
import de.wingaming.remake.ui.text.TextOption;
import de.wingaming.remake.ui.text.TextOptionExit;
import de.wingaming.remake.ui.text.TextOptionExtra;
import de.wingaming.remake.ui.text.TextOptionOptions;
import de.wingaming.remake.ui.text.TextOptionPlay;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UIMainMenue implements GameUI {
	
	public static UIMainMenue MENUE_ISNTANCE = new UIMainMenue();
	
	private int index = 0;
	private List<TextOption> options;

	private Image arrow = ImageManager.getImage("menue/arrow");
	
	public UIMainMenue() {
		options = new ArrayList<>();
		options.add(new TextOptionPlay());
		options.add(new TextOptionOptions());
		options.add(new TextOptionExtra());
		options.add(new TextOptionExit());
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
			options.get(index).perform();
			KeyboardManager.release(KeyCode.ENTER);
		}
	}
	
	public void render() {
		GraphicsContext gc = Main.gc;
		
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		
		gc.setFont(Font.font(30));
		gc.setFill(Color.WHITE);
		for (int i = 0; i < options.size(); i++) {
			gc.fillText(options.get(i).getText(), 30, Main.HEIGHT / 2 - (35 * options.size()) / 2 + 35 * i);
		}
		
		gc.drawImage(arrow, 30 + 10 + 2*new Text(0, 0, options.get(index).getText()).getLayoutBounds().getWidth(), Main.HEIGHT / 2 - (35 * options.size()) / 2 + 35 * index - 30/2 - 25/4, 100, 25);
		
		gc.setFont(Font.font(50));
		gc.fillText("Use arrow-keys and ENTER to navigate", 300, Main.HEIGHT/2);
		
//		gc.drawImage(ImageManager.getImage("entity/clippy"), Main.WIDTH - 64 - 10, Main.HEIGHT - 128 - 10, 64, 128);
	}
}