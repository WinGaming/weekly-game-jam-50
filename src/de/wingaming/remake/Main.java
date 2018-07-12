package de.wingaming.remake;

import de.wingaming.remake.games.TileType;
import de.wingaming.remake.games.World;
import de.wingaming.remake.games.portal.Portal;
import de.wingaming.remake.input.KeyboardManager;
import de.wingaming.remake.input.Mouse;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	
	public static final double WIDTH = 1280, HEIGHT = 720;
	
	public static Scene scene;
	public static Stage window;
	public static Renderer renderer;
	public static GraphicsContext gc;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage window) throws Exception {
		window.setTitle("//TODO: Set title");
		window.setWidth(WIDTH);
		window.setHeight(HEIGHT);
		window.setResizable(false);
		window.initStyle(StageStyle.UNDECORATED);
		
		Main.window = window;
		
		Group group = new Group();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		gc = canvas.getGraphicsContext2D();
		scene = new Scene(group);
		
		scene.setOnKeyPressed(e -> KeyboardManager.press(e.getCode()));
		scene.setOnKeyReleased(e -> KeyboardManager.release(e.getCode()));
		
		scene.setOnMousePressed(e -> { Mouse.setPressed(true); Mouse.setLastButton(e.getButton()); });
		scene.setOnMouseReleased(e -> Mouse.setPressed(false));
		
		group.getChildren().add(canvas);
		
		window.setScene(scene);
		window.show();
		
		renderer = new Renderer();
		renderer.start();
		
//		scene.setOnMouseClicked(e ->  {
//			int tileX = (int) (e.getX() / World.TILE_SIZE);
//			int tileY = (int) (e.getY() / World.TILE_SIZE);
//			
//			if (Portal.GAME_INSTANCE.getWorld() != null)
//				Portal.GAME_INSTANCE.getWorld().setTile(tileX, tileY, e.getButton() == MouseButton.PRIMARY ? TileType.getCurrentType() : null);
//			});
//			
//			scene.setOnMouseDragged(e -> {
//				int tileX = (int) (e.getX() / World.TILE_SIZE);
//				int tileY = (int) (e.getY() / World.TILE_SIZE);
//				
//				
//				if (Portal.GAME_INSTANCE.getWorld() != null)
//					Portal.GAME_INSTANCE.getWorld().setTile(tileX, tileY, e.getButton() == MouseButton.PRIMARY ? TileType.getCurrentType() : null);
//			});
		}
}