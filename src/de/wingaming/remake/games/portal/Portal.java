package de.wingaming.remake.games.portal;

import java.awt.MouseInfo;
import java.awt.Point;

import de.wingaming.remake.Main;
import de.wingaming.remake.games.CollideFallback;
import de.wingaming.remake.games.Level;
import de.wingaming.remake.games.LevelManager;
import de.wingaming.remake.games.Loader;
import de.wingaming.remake.games.TileType;
import de.wingaming.remake.games.World;
import de.wingaming.remake.image.ImageManager;
import de.wingaming.remake.input.KeyboardManager;
import de.wingaming.remake.ui.GameUI;
import de.wingaming.remake.utils.Direction;
import de.wingaming.remake.utils.Location;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Portal implements GameUI, CollideFallback {
	
	public static final Portal GAME_INSTANCE = new Portal();
	
	private PortalPlayer player;
	private PortalPortal blue, orange;
	private Image blueTexture = ImageManager.getImage("portal/blue");
	private Image orangeTexture = ImageManager.getImage("portal/orange");
	private Image greenTexture = ImageManager.getImage("portal/green");
	private Image goalTexture = ImageManager.getImage("portal/goal");
	private boolean shotboolean = false;
	private boolean canShoot = false;
	private Location prevHitLocation, hitLocation;
	
	private double rotation;
	private double gravity = 4.5;
	private RotationMode rotationMode;
	
	private PortalPortal inPortal = null;
	
	private boolean paused = false;
	private PortalPauseMenue pauseMenue;
	private World world = Loader.loadWorld("p");
	private Level level;
	
	//time
	private long lastSystemTime;
	private long timeToAdd;
	
	//checkpointns
	private Checkpoint checkpoint;
	private Checkpoint lastCheckpoint;
	
	public Portal() {
		this.player = new PortalPlayer(new Location(700, 200));
		this.rotationMode = RotationMode.LEFT_RIGHT;
		this.pauseMenue = new PortalPauseMenue();
		
		this.blue = new PortalPortal(new Location(1, 14), Direction.RIGHT);
		this.orange = new PortalPortal(new Location(38, 14), Direction.LEFT);
	}
	
	public void loadWorld(World world, Level level) {
		this.world = world;
		this.level = level;
		this.world.setFallback(TileType.TILE_GROUND_2);
		this.player.setPosition(world.getPlayerPosition());
		this.orange.setPosition(world.getOrangePosition());
		this.blue.setPosition(world.getBluePosition());
		this.blue.setFacing(world.getBlueDirection());
		this.orange.setFacing(world.getOrangeDirection());
		this.canShoot = false;
		this.lastCheckpoint = null;
		this.checkpoint = null;
		
		setPaused(false);
		
		timeToAdd = 0;
		lastSystemTime = System.currentTimeMillis();
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void update() {
		if (KeyboardManager.isDown(KeyCode.R)) {
			KeyboardManager.release(KeyCode.R);
			
			loadWorld(Loader.loadWorld(level.getWorldFile()), level);
		}
		
		//TODO:
		if (KeyboardManager.isDown(KeyCode.G)) {
			KeyboardManager.release(KeyCode.G);
			
			TileType.nextType();
		}
		
		if (KeyboardManager.isDown(KeyCode.Z)) {
			KeyboardManager.release(KeyCode.Z);
			
			Loader.saveWorld("current", world, world.getPlayerPosition(), world.getCheckpoints(), world.getTextID(), world.getGoalPosition());
			System.out.println("Saved!");
		}
		
		if (KeyboardManager.isDown(KeyCode.ESCAPE)) {
			KeyboardManager.release(KeyCode.ESCAPE);
			
			paused = !paused;
			
			if (paused) {
				timeToAdd += System.currentTimeMillis() - lastSystemTime;
				System.out.println("pause");
			} else {
				lastSystemTime = System.currentTimeMillis();
				System.out.println("unpause");
			}
		}
		
		if (!paused && KeyboardManager.isDown(KeyCode.ENTER)) {
			KeyboardManager.release(KeyCode.ENTER);
			
			if (canShoot) {
				canShoot = false;
				lastCheckpoint = checkpoint;
				checkpoint = null;
			}
		}
		
		if (checkpoint == null) {
			Checkpoint newCheckpoint = world.getCheckpointAt(player.getPosition().getTileX(), player.getPosition().getTileY());
			
			if (newCheckpoint != null && !newCheckpoint.equals(lastCheckpoint)) {
				this.checkpoint = newCheckpoint;
				player.getPosition().setX(checkpoint.getPosition().getX() * World.TILE_SIZE + World.TILE_SIZE/2-10);
				player.getPosition().setY(checkpoint.getPosition().getY() * World.TILE_SIZE + 4);
				
				this.rotation = 0;
				this.canShoot = true;
			} else if (newCheckpoint == null) {
				lastCheckpoint = null;
			}
		}
		
		if ((blue.getPosition() != null && orange.getPosition() != null) && !(player.getPosition().getTileX() == blue.getPosition().getX() && player.getPosition().getTileY() == blue.getPosition().getY()) &&
				!(player.getPosition().getTileX() == orange.getPosition().getX() && player.getPosition().getTileY() == orange.getPosition().getY()))
			inPortal = null;
		
		if ((blue.getPosition() != null && orange.getPosition() != null) && !orange.equals(inPortal) && player.getPosition().getTileX() == blue.getPosition().getX() && player.getPosition().getTileY() == blue.getPosition().getY()) {
			Location newLocation = new Location(orange.getPosition().getX() * World.TILE_SIZE, orange.getPosition().getY() * World.TILE_SIZE);
			
			if (orange.getFacing() == Direction.LEFT) {
				newLocation.add(0, World.TILE_SIZE);
			}
			
			inPortal = blue;
			player.setPosition(newLocation);
		} else if (blue.getPosition() != null && orange.getPosition() != null && !blue.equals(inPortal) && player.getPosition().getTileX() == orange.getPosition().getX() && player.getPosition().getTileY() == orange.getPosition().getY()) {
			Location newLocation = new Location(blue.getPosition().getX() * World.TILE_SIZE, blue.getPosition().getY() * World.TILE_SIZE);
			
			if (blue.getFacing() == Direction.LEFT) {
				newLocation.add(0, World.TILE_SIZE);
			}
			
			inPortal = orange;
			player.setPosition(newLocation);
		}

		portalPlacement:
		if (canShoot && KeyboardManager.isDown(KeyCode.SPACE)) {
			KeyboardManager.release(KeyCode.SPACE);
			
			PortalPortal portal = shotboolean ? blue : orange;

			PortalPortal other = portal.equals(orange) ? blue : orange;
			if ((prevHitLocation != null && world.getCheckpointAt((int) prevHitLocation.getX(), (int) prevHitLocation.getY()) != null) || hitLocation == null || prevHitLocation == null || prevHitLocation.isSame(other.getPosition())) break portalPlacement;
			
			Direction direction;
			if (prevHitLocation.getY() == hitLocation.getY()) {
				if (prevHitLocation.getX() < hitLocation.getX())
					direction = Direction.LEFT;
				else
					direction = Direction.RIGHT;
			} else if (prevHitLocation.getY() < hitLocation.getY())
				direction = Direction.UP;
			else
				direction = Direction.DOWN;
			
			Location placeon = prevHitLocation.clone();
			
			if (direction == Direction.DOWN)
				placeon.add(0, -1);
			else if (direction == Direction.UP)
				placeon.add(0, 1);
			else if (direction == Direction.LEFT)
				placeon.add(1, 0);
			else if (direction == Direction.RIGHT)
				placeon.add(-1, 0);
			
			TileType placeOnTile = world.getTileTypeAt((int) placeon.getX(), (int) placeon.getY());
			if (!placeOnTile.isPortalable()) break portalPlacement;
			
			shotboolean = !shotboolean;
			portal.setFacing(direction);
			portal.setPosition(prevHitLocation);
		}
		
		if (paused) {
			this.pauseMenue.update();
		}
		
		if (!paused && checkpoint == null) {
			if (world.getGoalPosition() != null) {
				if (player.getPosition().getTileX() == world.getGoalPosition().getX() && player.getPosition().getTileY() == world.getGoalPosition().getY()) {
					Main.renderer.setMenue(new UILevelEnd(level, (System.currentTimeMillis() - lastSystemTime) + timeToAdd));
					LevelManager.setTime(level.getWorldFile(), (System.currentTimeMillis() - lastSystemTime) + timeToAdd);
					LevelManager.saveTimes();
					return;
				}
			}
			
			//Update player:
//			player.addVelocity(rotation * 0.3, gravity);
//			player.update(world);
			player.move(world, 0, gravity, this);
			player.move(world, rotation * 0.3, 0, this);
			
			//Update rotation:
			if (rotationMode == RotationMode.LEFT_RIGHT) {
				Point mouse = MouseInfo.getPointerInfo().getLocation();
				Location relMouse = new Location(-Main.window.getX() + mouse.getX(), -Main.window.getY() + mouse.getY());
				
				if (	relMouse.getX() < 0 ||
						relMouse.getY() < 0 ||
						relMouse.getX() > Main.WIDTH ||
						relMouse.getY() > Main.HEIGHT)
					return;
				
				double dx;
				
				if (relMouse.getX() <= Main.WIDTH) {
					dx = relMouse.getX() - Main.WIDTH / 2;
				} else {
					dx = Main.WIDTH - relMouse.getX();
				}
				
				double rdx = dx/(Main.WIDTH/2);
				
				rotation = rdx * 20;
			}
		}
	}
	
	public void render() {
		Main.gc.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
		
		Main.gc.save();
		Main.gc.translate(Main.WIDTH / 2, Main.HEIGHT / 2);
		Main.gc.rotate(rotation);
		Main.gc.translate(- (Main.WIDTH / 2),- (Main.HEIGHT / 2));
		
		world.render();
		
		//Draw texts:
		{
			if (world.getTextID() == 0) {
				Main.gc.setFont(Font.font(40));
				Main.gc.setFill(Color.WHITE);
				Main.gc.fillText("Move your mouse to the sides to rotate the board", Main.WIDTH/2 - 450, Main.HEIGHT/2 - 150);
				
				Main.gc.setFill(Color.BLACK);
				Main.gc.fillText("Roll into the goal to finish level", Main.WIDTH/2 - 340, Main.HEIGHT/2 + 200/*260*/);
			} else if (world.getTextID() == 1) {
				Main.gc.setFont(Font.font(30));
				Main.gc.setFill(Color.BLACK);
				Main.gc.fillText("You can roll through portals from both sides", Main.WIDTH/2 - 300, Main.HEIGHT/2 + 81);
			} else if (world.getTextID() == 2) {
				Main.gc.setFont(Font.font(30));
				Main.gc.setFill(Color.BLACK);
				Main.gc.fillText("Use checkpoints to shoot portals", World.TILE_SIZE + 5, Main.HEIGHT/2 + 145);
				
				if (checkpoint != null) {
					Main.gc.fillText("Press SPACE to shoot portals", Main.WIDTH/2 + 35, Main.HEIGHT/2 - 77);
					Main.gc.fillText("Press ENTER to leave checkpoint", Main.WIDTH/2 + 35, Main.HEIGHT/2 + 145);
				}
				
				Main.gc.setFill(Color.WHITE);
				Main.gc.fillText("Press 'R' to reset the level", Main.WIDTH/2 - 170, 245);
			} else if (world.getTextID() == 5) {
				Main.gc.setFont(Font.font(30));
				Main.gc.setFill(Color.WHITE);
				Main.gc.fillText("You can't place portals on white blocks", Main.WIDTH/2 - 460, Main.HEIGHT/2 + 120);
			}
		}
		
		Main.gc.setFill(Color.WHITE);
		Main.gc.fillRoundRect(player.getPosition().getX(), player.getPosition().getY(), 20, 20, 90, 90);
		
		//Portals
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		Location relMouse = new Location(-Main.window.getX() + mouse.getX(),-Main.window.getY() + mouse.getY());
		
		boolean under = player.getPosition().getY() > Main.HEIGHT/2;
		
		Location newPlayerPos;
		//Recalculate relativeMousePosition
		{
			double preRotation = 0;//22.65;//33.5;
			double radius = new Point((int) player.getPosition().getX(), (int) player.getPosition().getY()).distance(Main.WIDTH/2, Main.HEIGHT/2);
			
			{
				Location center = new Location(Main.WIDTH/2, Main.HEIGHT/2);
				
				double a, b;
				
				if (under) {
					a = center.getX() - player.getPosition().getX();
					b = center.getY() - player.getPosition().getY();
				} else {
					a = player.getPosition().getX() - center.getX();
					b = player.getPosition().getY() - center.getY();
				}
				
				double c = Math.sqrt(a*a + b*b);
				double alpha = Math.toDegrees(Math.asin(a/c));
				
				preRotation = (under ? 180 : 0)+90-alpha;
			}
			
			Location circle = new Location(Main.WIDTH/2, Main.HEIGHT/2);
			double angle = rotation - preRotation;// + (under ? 180 : 0);
			
//			Main.gc.setFill(Color.CADETBLUE);
//			Main.gc.fillRoundRect(circle.getX() - radius, circle.getY()-radius, radius*2, radius*2, 3600, 3600);
			
			double a, b; //x, y
			
			a = radius * Math.cos(Math.toRadians(angle));
			b = radius * Math.sin(Math.toRadians(angle));
			
			newPlayerPos = new Location(circle.getX() + a + 10, circle.getY() + b + 10);
		}
		
		//End Recalculation
		
		//Draw portals
		if (blue.getPosition() != null) {
			double degr = 0;
			
			switch (blue.getFacing()) {
			case DOWN:
				degr = -90;
				break;
			case UP:
				degr = 90;
				break;
			case RIGHT:
				degr = 180;
				break;
			default:
				degr = 0;
				break;
			}
			
			Main.gc.translate(blue.getPosition().getX() * World.TILE_SIZE + World.TILE_SIZE/2, blue.getPosition().getY() * World.TILE_SIZE + World.TILE_SIZE/2);
			Main.gc.rotate(degr);
			Main.gc.drawImage(blueTexture, -(World.TILE_SIZE/2), -(World.TILE_SIZE/2), World.TILE_SIZE, World.TILE_SIZE);
			Main.gc.rotate(-degr);
			Main.gc.translate(-(blue.getPosition().getX() * World.TILE_SIZE + World.TILE_SIZE/2), -(blue.getPosition().getY() * World.TILE_SIZE + World.TILE_SIZE/2));
		}
		
		if (orange.getPosition() != null) {
			double degr = 0;
			
			switch (orange.getFacing()) {
			case DOWN:
				degr = -90;
				break;
			case UP:
				degr = 90;
				break;
			case RIGHT:
				degr = 180;
				break;
			default:
				degr = 0;
				break;
			}
			
			Main.gc.translate(orange.getPosition().getX() * World.TILE_SIZE + World.TILE_SIZE/2, orange.getPosition().getY() * World.TILE_SIZE + World.TILE_SIZE/2);
			Main.gc.rotate(degr);
			Main.gc.drawImage(orangeTexture, -(World.TILE_SIZE/2), -(World.TILE_SIZE/2), World.TILE_SIZE, World.TILE_SIZE);
			Main.gc.rotate(-degr);
			Main.gc.translate(-(orange.getPosition().getX() * World.TILE_SIZE + World.TILE_SIZE/2), -(orange.getPosition().getY() * World.TILE_SIZE + World.TILE_SIZE/2));
		}
		
		if (world.getGoalPosition() != null) {
			Main.gc.drawImage(goalTexture, world.getGoalPosition().getX() * World.TILE_SIZE, world.getGoalPosition().getY() * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
		}
		
		Main.gc.restore();

		Main.gc.drawImage(ImageManager.getImage("ui/cross"), mouse.getX() - 10 - Main.window.getX(), mouse.getY() - 10 - Main.window.getY(), 20, 20);
		Main.gc.drawImage(ImageManager.getImage("portal/middle"), Main.WIDTH/2 - 10, Main.HEIGHT/2 - 10, 20, 20);

		//Debug
//		Main.gc.setFill(Color.YELLOWGREEN);
//		Main.gc.fillRoundRect(newPlayerPos.getX() - 10, newPlayerPos.getY() - 10, 20, 20, 90, 90);

		//Portals:
		double a = relMouse.getY() - newPlayerPos.getY();//player.getPosition().getY();
		double c = new Point((int) relMouse.getX(), (int) relMouse.getY()).distance(newPlayerPos.getX(), newPlayerPos.getY());//.distance(player.getPosition().getX(), player.getPosition().getY());
		
		boolean r = relMouse.getX() < newPlayerPos.getX();//player.getPosition().getX();
		double alpha = (r ? 180 : 0) + (r ? 1 : -1) * Math.toDegrees(Math.asin(a/c));//Math.toDegrees(Math.sinh(Math.sin(Math.toRadians(i))));
		
		Main.gc.save();
		Main.gc.translate(newPlayerPos.getX(), newPlayerPos.getY());
		Main.gc.rotate(-alpha);
		
//		Main.gc.setFill(new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), .5));
//		Main.gc.fillRoundRect(0, -5, 200/*50*/, 10, 10, 10);

		Main.gc.rotate(-(alpha));
		Main.gc.translate(-(newPlayerPos.getX()), -(newPlayerPos.getY()));
		Main.gc.restore();
		
		//Draw portal target
		if (canShoot){
			Main.gc.setFill(new Color(.44, .55, .66, .9));
			
			Location prevHit = null;
			
			collideLoop:
			for (int i = 1; i < 200; i++) {
				double ac = i * 5;
				double aa = Math.sin(Math.toRadians(alpha)) * ac;
				double beta = 180 - 90 - (alpha);
				double ab = (ac / Math.sin(Math.toRadians(90))) * Math.sin(Math.toRadians(beta));
				
				double x = (newPlayerPos.getX() + ab);
				double y = (newPlayerPos.getY() - aa);
				
				int ix, iy;
				
				{
					int n = (int) (x / World.TILE_SIZE);
					double nd = x - World.TILE_SIZE*n;
					
					if (nd > World.TILE_SIZE) n++;
					
					ix = n;
				}
				
				{
					int n = (int) (y/ World.TILE_SIZE);
					double nd = y - World.TILE_SIZE*n;
					
					if (nd > World.TILE_SIZE) n++;
					
					iy = n;
				}
				
				TileType tile = world.getTileTypeAt(ix, iy);
				
				if (tile != null && tile.isSolid()) {
					Main.gc.save();
					Main.gc.translate(Main.WIDTH / 2, Main.HEIGHT / 2);
					Main.gc.rotate(rotation);
					Main.gc.translate(- (Main.WIDTH / 2),- (Main.HEIGHT / 2));
					
					hitLocation = new Location(ix, iy);
					prevHitLocation = prevHit == null ? new Location(player.getPosition().getTileX(), player.getPosition().getTileY()) : prevHit;
					
//					Main.gc.fillRect(ix * World.TILE_SIZE, iy * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
					Main.gc.restore();
					
					break collideLoop;
				}
				
				Main.gc.setFill(Color.RED);
				Main.gc.fillRect(x - 2, y - 2, 4, 4);
				
				prevHit = new Location(ix, iy);
				hitLocation = null;
			}
			
//			if (prevHit != null) {
//				Main.gc.save();
//				Main.gc.translate(Main.WIDTH / 2, Main.HEIGHT / 2);
//				Main.gc.rotate(rotation);
//				Main.gc.translate(- (Main.WIDTH / 2),- (Main.HEIGHT / 2));
//				
//				Main.gc.fillRect(prevHit.getX() * World.TILE_SIZE, prevHit.getY() * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
//				Main.gc.restore();
//			}
			
			if (prevHitLocation != null && hitLocation != null){
				Direction direction;
				if (prevHitLocation.getY() == hitLocation.getY()) {
					if (prevHitLocation.getX() < hitLocation.getX())
						direction = Direction.LEFT;
					else
						direction = Direction.RIGHT;
				} else if (prevHitLocation.getY() < hitLocation.getY())
					direction = Direction.UP;
				else
					direction = Direction.DOWN;
				
				double degr = 0;
				switch (direction) {
					case DOWN:
						degr = -90;
						break;
					case UP:
						degr = 90;
						break;
					case RIGHT:
						degr = 180;
						break;
					default:
						degr = 0;
						break;
				}
				
				Main.gc.save();
				Main.gc.translate((prevHitLocation.getX() * World.TILE_SIZE + World.TILE_SIZE/2), (prevHitLocation.getY() * World.TILE_SIZE + World.TILE_SIZE/2));
				Main.gc.rotate(degr);
				Main.gc.translate(-(prevHitLocation.getX() * World.TILE_SIZE + World.TILE_SIZE/2), -(prevHitLocation.getY() * World.TILE_SIZE + World.TILE_SIZE/2));
				
				Main.gc.drawImage(greenTexture, prevHit.getX() * World.TILE_SIZE, prevHit.getY() * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
				
				Main.gc.restore();
			}
		}

//		Main.gc.drawImage(TileType.getCurrentType().getTexture(), 10, 10);
		
		if (paused) {
			this.pauseMenue.render();
		}
	}
	
	public World getWorld() {
		return world;
	}
	
	public void collideX() {}
	public void collideY(boolean top) {}
}
