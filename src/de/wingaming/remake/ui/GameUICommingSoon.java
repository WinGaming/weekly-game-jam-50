package de.wingaming.remake.ui;

import de.wingaming.remake.Main;
import de.wingaming.remake.image.ImageManager;
import de.wingaming.remake.input.KeyboardManager;
import de.wingaming.remake.ui.text.TextOption;
import de.wingaming.remake.ui.text.TextOptionBack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameUICommingSoon implements GameUI {
	
	public static GameUICommingSoon MENUE_INSTANCE = new GameUICommingSoon();
	
	private TextOption back = new TextOptionBack();
	private Image arrow = ImageManager.getImage("menue/arrow");
	
	public void update() {
		if (KeyboardManager.isDown(KeyCode.ENTER)) {
			Main.renderer.setMenue(UIMainMenue.MENUE_ISNTANCE);
			KeyboardManager.release(KeyCode.ENTER);
		}
	}
	
	public void render() {
		GraphicsContext gc = Main.gc;
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

		gc.setFill(Color.WHITE);
		gc.setFont(Font.font(30));
		gc.fillText(back.getText(), 30, Main.HEIGHT - 35);

		gc.setFont(Font.font(50));
		gc.fillText("Comming soon", Main.WIDTH/2 - 150, Main.HEIGHT / 2 - 25);
		
		gc.drawImage(arrow, 30 + 10 + 2*new Text(0, 0, back.getText()).getLayoutBounds().getWidth(), Main.HEIGHT - 35 - 25, 100, 25);
	}
}
