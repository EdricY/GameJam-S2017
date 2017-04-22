package game;
//butts

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;

import game.entity.Enemy;
import game.entity.EntityGlobals;
import game.entity.Fog;
import game.entity.GridObj;
import game.entity.PlayerObj;
import game.entity.Tile;
import game.entity.Wall;
import game.entity.EntityGlobals;
import game.gfx.Button;
import game.gfx.Button.States;
import game.gfx.ChatBox;
import game.gfx.Font;
import game.gfx.FontJump;
import game.gfx.ProgressBar;
import game.gfx.Screen;
import game.gfx.TextBox;
import game.handlers.InputHandler;
import game.handlers.MouseHandler;
import game.handlers.WindowHandler;
import game.utils.ButtonList;
import game.utils.Debug;
import game.utils.MP3;
import game.utils.Out;
import game.utils.TextBoxList;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * The main class that instantiates all other variables.
 *
 * @author AJ
 */

@SuppressWarnings("restriction")
public class Game extends Canvas implements Runnable {

	/**
	 * I have no idea what this black magic is.
	 */
	private static final long serialVersionUID = -8332966340668015263L;

	/**
	 * The {@link String} of the game. Starts with a v. This is used when
	 * logging on to a server, so update it often to prevent out dated clients
	 * from connecting.
	 */
	public static final String VERSION = "v0";

	/**
	 * The {@link String} of the class. This is displayed in the title.
	 */
	public static final String NAME = "A Dimming Matter";

	/**
	 * The minimum {@link Out} of debug that is output. Should be
	 * {@link Out#INFO} or higher on release. NEVER SET TO {@link Out#TRACE} ON
	 * RELEASE!
	 */
	public static final Out debugLevel = Out.INFO;

	/**
	 * Integer representing the dimensions of the JFrame.
	 */
	public static final int WIDTH = 960, HEIGHT = (Game.WIDTH / 16) * 9, SCALE = 1;

	/**
	 * The {@link JFrame} window the game is rendered to.
	 */
	public final JFrame frame;

	/**
	 * A list of all of the {@link Button}s (Enabled or hidden) in the game.
	 */
	public final ButtonList buttons = new ButtonList();

	/**
	 * A list of all of the {@link TextBox}s (Enabled or hidden) in the game.
	 */
	public final TextBoxList textboxes = new TextBoxList();
	
	/**
	 * The {@link BufferedImage} that is rendered to the JFrame.
	 */
	private static final BufferedImage image = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);

	/**
	 * Error stream for the game. And game messages
	 */
	private final ChatBox err;

	/**
	 * Used for updating and pressing the mouse.
	 */
	private final MouseHandler mouse;

	/**
	 * Screen that is being rendered.
	 */
	private final Screen screen;

	/**
	 * An array of ints that is used to draw the picture on the JFrame.
	 */
	private final int[] pixels = ((DataBufferInt) Game.image.getRaster().getDataBuffer()).getData();

	/**
	 * State of the running game
	 */
	public Stage stage = Stage.MENU;
	/**
	 * FontJump for awesome menu effects!
	 */
	FontJump fj;
	
	MP3 mp3player = new MP3();
	MP3 soundeffects = new MP3();
	Fog fog;
	int fcount;
	int[][] brightenmap;
	
	public boolean backspace;
	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;
	public boolean boom;
	public static int offsetX = 0;
	public static int offsetY = 0;
	
	PlayerObj player;
	
	/**
	 * Creates the Game class
	 */
	public Game() {
		Debug.out(Out.INFO, getClass().getName(), "You are running " + Game.VERSION);

		setMinimumSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		setMaximumSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.setResizable(false);
		final ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/smallIcon.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
		frame.setIconImages(icons);
		frame.pack();
		frame.setLocationRelativeTo(null);
		screen = new Screen(Game.WIDTH, Game.HEIGHT);
		err = new ChatBox(0, 200, 50, 3);
		mouse = new MouseHandler(this);
	}

	/**
	 * Starts up the game thread.
	 */
	private void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime(); // long time in nanoseconds.
		final double nsPerTick = 1000000000D / 60D; // limits updates to 60 ups
		int frames = 0; // frames drawn
		long lastTimer = System.currentTimeMillis();
		double wait = 0; // unprocessed nanoseconds.
		int ticks = 0;

		init();

		while (true) {
			final long now = System.nanoTime();
			wait += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (wait >= 1D) {
				ticks++; // Update drawn ticks.
				tick();
				wait = wait - 1D;
			}
			frames++;
			render();
			try {
				Thread.sleep(6);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			if ((System.currentTimeMillis() - lastTimer) > 1000) {
				lastTimer += 1000;
				frame.setTitle(Game.NAME + " - " + ticks + " ticks, " + frames + " frames.");
				frames = 0;
				ticks = 0;
			}
		}
	}

	/**
	 * Code that is run to draw objects to the frame.
	 */
	private void render() {
		final BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2); // Triple buffering!
			return;
		}
		final Graphics g = bs.getDrawGraphics();
		
		switch (stage) {
		default:
		case MENU:
			screen.render(0, 0, "/background.png");
			break;
		case INSTRUCTIONS:
			screen.render(0, 0, "/instructions.png");
			break;
		case CREDITS:
			screen.render(0, 0, "/credits.png");
			break;
		case LEVEL:
			screen.render(0, 0, "/blank.png");
			GridObj[][] ma = EntityGlobals.getMapArray();
			for (int i = 0; i < ma.length; i++){
				for (int j = 0; j < ma[0].length; j++){
					screen.render(i * 30 + offsetX, j * 30 + offsetY, ma[i][j].getPath());
				}
			}
			screen.render(player.getX()+offsetX - 8, player.getY()+offsetY - 8, "/player.png");
			for (Enemy e : EntityGlobals.getEnemyList()) 
				screen.render(e.getX()+offsetX-8, e.getY()+offsetY -8, "/enemy.png");
			
		}
		
		if (fj != null) fj.render(screen);
		
		for (final Button b : buttons.getAll()) {
			b.render(screen);
		}
		for (final TextBox t : textboxes.getAll()) {
			t.render();
		}
		
		for (int pix = 0; pix < screen.pixels.length; pix++) {
			pixels[pix] = screen.pixels[pix];
		}
		
		int brighten = 0;
		int alpha = 0;
		int redden = 0;
		int flasht = fog.getFlashTimer();
		int hurtt = fog.getHurtTimer();
		
		for (int y = 0; y < getWidth(); y++) {
            for (int x = 0; x < getHeight(); x++) {
            	alpha = fog.getAlpha(x, y);
            	if (alpha == 0){
            		Game.image.setRGB(y, x, 0 & 0);
            	} else if (flasht==0 && hurtt==0){
            		Color c = new Color(Game.image.getRGB(y, x));
            		int argb = c.getRGB();
            		argb = (alpha << 24) | (argb & 0x00FFFFFF);
            		Game.image.setRGB(y, x, argb);
            	} else {
                	brighten = fog.getBrightness(x, y);
                	redden = fog.getRedder(x, y);
	            	Color c = new Color(Game.image.getRGB(y, x));
	            	int re = c.getRed() + brighten;
	            	int gr = c.getGreen() + brighten - redden;
	            	int bl = c.getBlue() + brighten - redden;
	            	re = (re > 255) ? 255 : re;
	            	re = (re < 0) ? 0 : re;
	            	gr = (gr > 255) ? 255 : gr;
	            	gr = (gr < 0) ? 0 : gr;
	            	bl = (bl > 255) ? 255 : bl;
	            	bl = (bl < 0) ? 0 : bl;
	            	c = new Color(
	            			re,
	            			gr,
	            			bl,
	            			alpha);
	            	Game.image.setRGB(y, x, c.getRGB());
	            }
			}
		}
		screen.lookupSprite("/blank.png").draw(g, 0, 0);
		g.drawImage(Game.image, 0, 0, getWidth(), getHeight(), null);
		if (stage == Stage.LEVEL){ // draw UI
			g.setColor(Color.YELLOW);
			g.drawString("bombs: " +Integer.toString(player.getBombs()), 300, 10);
			g.drawString("ammo: " +Integer.toString(player.getAmmo()), 200, 10);
		}
//		switch (stage){
//		case LEVEL:
//			GridObj[][] ma = EntityGlobals.getMapArray();
//			for (int i = 0; i < ma.length; i++)
//			{
//				for (int j = 0; j < ma[0].length; j++)
//				{
//					ma[i][j].draw(g);
//				}
//			}
//			break;
//		}
		bs.show();
	}

	/**
	 * Code that is updated once every nsPerTick nanoseconds.
	 *
	 * @see Game#run()
	 */
	private void tick() {
		int mouseX = mouse.getX();
		int mouseY = mouse.getY();
		int mouseHoverX = mouse.getHoverX();
		int mouseHoverY = mouse.getHoverY();
		switch (stage) {
		default:
		case MENU:
			if (mp3player.isIdle()) mp3player.play();
			if (buttons.get(BN.QUIT).isClicked()){
				playSound("/Blip.wav");
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			if (buttons.get(BN.INSTRUCTIONS).isClicked()){
				fog.startFlash(100);
				fcount = 65;
				hideAll();
				fj = new FontJump(buttons.get(BN.INSTRUCTIONS).getX(), buttons.get(BN.INSTRUCTIONS).getY(), "Smart Guy!", 100, 45.0, 30, false);
				playSound("/Blip.wav");
				stage = Stage.INSTRUCTIONS;
			}
			if (buttons.get(BN.CREDITS).isClicked()){
				fog.startFlash(100);
				hideAll();
				fj = new FontJump(buttons.get(BN.CREDITS).getX(), buttons.get(BN.CREDITS).getY(), "Thank You!", 100, 45.0, 30, false);
				playSound("/Blip.wav");
				stage = Stage.CREDITS;
			}
			if (buttons.get(BN.PLAY).isClicked()){
				fog.startFlash(100);
				hideAll();
				fj = new FontJump(buttons.get(BN.PLAY).getX(), buttons.get(BN.PLAY).getY(), "Let's Go!", 100, 45.0, 30, false);
				playSound("/Blip.wav");
				mp3player.close();
				mp3player.changeMusic("/SOUND_main_theme.mp3");
				mp3player.play();
				stage = Stage.LEVEL;
			}
			
			fog.update(mouseHoverX, mouseHoverY, 300);
			
			break;
		case INSTRUCTIONS:
			if (mp3player.isIdle()) mp3player.play();
			if (backspace){
				fog.startFlash(100);
				backspace = false;
				stage = Stage.MENU;
				buttons.get(BN.INSTRUCTIONS).state = States.ENABLED;
				buttons.get(BN.PLAY).state = States.ENABLED;
				buttons.get(BN.QUIT).state = States.ENABLED;
				buttons.get(BN.CREDITS).state = States.ENABLED;
			}
			fog.update(mouseHoverX, mouseHoverY, 300);
			break;
		case CREDITS:
			if (mp3player.isIdle()) mp3player.play();
			if (backspace){
				fog.startFlash(100);
				backspace = false;
				stage = Stage.MENU;
				buttons.get(BN.INSTRUCTIONS).state = States.ENABLED;
				buttons.get(BN.PLAY).state = States.ENABLED;
				buttons.get(BN.QUIT).state = States.ENABLED;
				buttons.get(BN.CREDITS).state = States.ENABLED;
			}
			fog.update(mouseHoverX, mouseHoverY, 300);
			break;
		case LEVEL:
			if (mp3player.isIdle()) mp3player.play();
			if(boom){
				boom = false;
				if(player.getBombs() > 0){//detonate bomb
					fog.startFlash(120);
					player.modifyBomb(-1);
					player.modifyHealth(-5);
					int range = player.getHealth()/10;
					explode(EntityGlobals.getMapArray()[player.getX()/30][player.getY()/30], range);
				}
				else fog.startHurtFlash(120);
			}
			player.update(up, down, left, right);
			for (Enemy e : EntityGlobals.getEnemyList()) e.update(player.getX(), player.getY());
			fog.update(player.getX() + offsetX, player.getY() + offsetY, player.getHealth()*3);
			break;
		}
		if(fj != null && !fj.isDone()) fj.tick();
		
		err.tick();
		for (final Button b : buttons.getAll()) {
			if ((b.state == Button.States.ENABLED) || (b.state == Button.States.PRESSED)
					|| (b.state == Button.States.OUT)) {
				final boolean within = (mouse.getX() >= b.getX())
						&& (mouse.getX() <= ((b.getW() * b.getTileWidth()) + b.getX())) && (mouse.getY() >= b.getY())
						&& (mouse.getY() <= ((b.getH() * b.getTileHeight()) + b.getY()));
				if (within && (mouse.getX() >= 0) && (mouse.getY() >= 0)) {
					b.state = Button.States.PRESSED;
				} else if (!within && (mouse.getX() >= 0) && (mouse.getY() >= 0)) {
					b.state = Button.States.OUT;
				} else {
					b.state = Button.States.ENABLED;
				}
			}
		}
		for (final TextBox t : textboxes.getAll()) {
			if (((mouse.getX() >= 0) && (mouse.getY() >= 0))
					&& ((t.state == TextBox.States.ENABLED) || (t.state == TextBox.States.SELECTED))) {
				final boolean within = (mouse.getX() >= t.getX()) && (mouse.getX() <= (t.getW() + t.getX()))
						&& (mouse.getY() >= t.getY()) && (mouse.getY() <= (t.getH() + t.getY()));
				if (within) {
					for (final TextBox sett : textboxes.getAll()) {
						if (sett.state == TextBox.States.SELECTED) {
							sett.state = TextBox.States.ENABLED;
						}
					}
					t.state = TextBox.States.SELECTED;
				} else {
					t.state = TextBox.States.ENABLED;
				}
			}
		}
	}

	private void explode(GridObj go, int power){
		if (power == 0) return;
		if (go.getType().equals("/tile.png")){
			((Tile) go).setLight(1);
			((Tile) go).dealDamage();
			((Tile) go).setLight(0);
			if(go.getC()+1 < 55)
				explode(EntityGlobals.getMapArray()[go.getR()][go.getC()+1], power-1);
			if(go.getC()-1 >= 0)
				explode(EntityGlobals.getMapArray()[go.getR()][go.getC()-1], power-1);
			if(go.getR()+1 < 193)
				explode(EntityGlobals.getMapArray()[go.getR()+1][go.getC()], power-1);
			if(go.getR()-1 >= 0)
				explode(EntityGlobals.getMapArray()[go.getR()-1][go.getC()], power-1);
		}
		else if (go.getType().equals("/wall.png")){
			((Wall) go).destroyWall();
		}
	}
	
	private void hideAll() {
		for (final Button b : buttons.getAll()) {
			b.state = Button.States.HIDDEN;
		}
		for (final TextBox t : textboxes.getAll()) {
			t.state = TextBox.States.HIDDEN;
		}
	}

	/**
	 * Completely restarts the game from the bottom up, showing an error message to the user.
	 * @param reason Reason given for the sudden restart.
	 */
	public void restart(String reason) {
		stage = Stage.MENU;
		hideAll();
		Debug.out(Out.WARNING, "game.Game", "Error: " + reason);
		err.addString(reason, 0xFFFF0000);
	}

	/**
	 * Gets the currently selected text box.
	 * @return The selected text box.
	 */
	public TextBox getSelectedTextBox() {
		for (final TextBox t : textboxes.getAll()) {
			if (t.state == TextBox.States.SELECTED) {
				return t;
			}
		}
		return null;
	}

	/**
	 * @param args
	 *            Arguments used to start the game with.
	 */
	public static void main(String[] args) {
		for (final String s : args) {
			System.out.println(s);
		}
		new Game().start();
	}

	/**
	 * Code that runs ONE TIME when the game is started up.
	 */
	private void init() {
		new InputHandler(this);
		new WindowHandler(this);
		frame.setVisible(true); // After init, show the frame.
		final int BUTTON_WIDTH = 10;
		final int BUTTON_HEIGHT = 4;
		final int BUTTON_PIX_WIDTH = 9;
		final int BUTTON_PIX_HEIGHT = 9;
		mp3player.changeMusic("/SOUND_menu_theme.mp3"); mp3player.play();
		
		buttons.add(new Button(screen, Game.WIDTH/2 - (18/2)*BUTTON_PIX_WIDTH, (Game.HEIGHT / 2)-75, 
				17, 4, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.PLAY);
		buttons.get(BN.PLAY).text = "Play!";
		buttons.get(BN.PLAY).state = Button.States.ENABLED;
		
		buttons.add(new Button(screen, (Game.WIDTH/2) - (18/2)*BUTTON_PIX_WIDTH, (Game.HEIGHT / 2)-75 + 4*45,
				17, 4, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.QUIT);
		buttons.get(BN.QUIT).text = "Quit";
		buttons.get(BN.QUIT).state = Button.States.ENABLED;
		
		buttons.add(new Button(screen, Game.WIDTH/2 - (18/2)*BUTTON_PIX_WIDTH, (Game.HEIGHT / 2) -75+4*15, 
				17, 4, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.INSTRUCTIONS);
		buttons.get(BN.INSTRUCTIONS).text = "Instructions";
		buttons.get(BN.INSTRUCTIONS).state = Button.States.ENABLED;
		
		buttons.add(new Button(screen, (Game.WIDTH/2) - (18/2)*BUTTON_PIX_WIDTH, (Game.HEIGHT / 2)-75 + 4*30,
				17, 4, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.CREDITS);
		buttons.get(BN.CREDITS).text = "Credits";
		buttons.get(BN.CREDITS).state = Button.States.ENABLED;
		EntityGlobals.resetMap();	
		
		fog = new Fog();
		int randX = 480 * (int)(Math.random() * 6);
		int randY = 270 * (int)(Math.random() * 6);
		offsetX = 8 - randX;
		offsetY = 8 - randY;
		player = new PlayerObj(248+randX , 143 + randY);
		}
	
	public int getRoomR(int y){
		return y/270;
	}
	public int getRoomC(int x){
		return x/480;
	}
	
	private void playSound(String filename) {
	try{
		InputStream inputStream = Game.class.getResourceAsStream(filename);
		AudioStream audioStream = new AudioStream(inputStream);
		AudioPlayer.player.start(audioStream);
	} catch (Exception e){
		e.printStackTrace();
		}
	}

	/**
	 * State of the game.
	 * @author AJ
	 *
	 */
	public enum Stage {
		
		/**
		 * The game is at the main menu.
		 */
		MENU, LEVEL, INSTRUCTIONS, CREDITS;
	}

	/**
	 * Available button names (Unique)(Self explanatory).
	 * @author AJ
	 *
	 */
	public enum BN {
		PLAY, QUIT, INSTRUCTIONS, CREDITS;
	}

	/**
	 * Available text box names (Unique)(Self explanatory).
	 * @author AJ
	 *
	 */
	public enum TN {
		TEXTBOX
	}
}