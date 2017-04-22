package game;
//butts

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import game.entity.Boss;
import game.entity.Enemy;
import game.entity.EntityGlobals;
import game.entity.Fog;
import game.entity.GridObj;
import game.entity.PlayerObj;
import game.entity.Tile;
import game.entity.Wall;
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
	public int shotTimer = 0;
	public int shotTimerMax = 20;
	public int lastShotX;
	public int lastShotY;
	public static int offsetX = 0;
	public static int offsetY = 0;
	public int peaceTimer = 1200;
	public static boolean nextWave = false;
	
	int upspeed=		0;
	int upmaxhealth=	2;
	int uppower=		3;
	int upammoconserve=	4;
	int upcrit=			5;
	int upfirerate=		1;
	
	PlayerObj player;
	Boss boss;
	
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
		case GAMEOVER:
			screen.render(0, 0, "/gameover.png");
			break;
		case PAUSE:
			screen.render(0, 0, "/pause.png");
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
			EntityGlobals.getEnemyList();
			for (Enemy e : EntityGlobals.getEnemyList()) 
				screen.render(e.getX()+offsetX-8, e.getY()+offsetY -8, "/enemy.png");
			screen.render(boss.getX()+offsetX - 16, boss.getY()+offsetY - 16, "/boss.png");
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
		
		/*fog loop
		/*/
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
		/**/
		screen.lookupSprite("/blank.png").draw(g, 0, 0);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(7));
		if(stage == Stage.PAUSE){
		g.setColor(Color.GRAY);
		for(int ypos = 100; ypos <= 400; ypos+=60)
			g.drawLine(250, ypos, 550, ypos);
		g.setColor(Color.GREEN);
		if (upspeed > 0)
			g.drawLine(250, 100, 250+60*upspeed, 100);
		if (upmaxhealth > 0)
		g.drawLine(250, 160, 250+60*upmaxhealth, 160);
		if (uppower > 0)
			g.drawLine(250, 220, 250+60*uppower, 220);
		if (upammoconserve > 0)
			g.drawLine(250, 280, 250+60*upammoconserve, 280);
		if (upcrit > 0)
			g.drawLine(250, 340, 250+60*upcrit, 340);
		if (upfirerate > 0)
			g.drawLine(250, 400, 250+60*upfirerate, 400);
		}
		
		g.drawImage(Game.image, 0, 0, getWidth(), getHeight(), null);
		if (stage == Stage.LEVEL){ // draw UI
			BufferedImage shot = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
			BufferedImage ref = screen.lookupSprite("/circle.png").image;
			for (int y = 0; y < shot.getWidth(); y++){
	            for (int x = 0; x < shot.getHeight(); x++){
	            	//Color c = new Color(ref.getRGB(x,y));
	            	int argb = ref.getRGB(x,y);
	            	float frac = ((float)shotTimer)/((float)shotTimerMax);
            		argb = (((int)(frac*255) << 24)| 0x00FFFFFF )& (argb);
	            	//c = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	            	
	            	shot.setRGB(x, y, argb);
	            }
			}
			g.drawImage(shot, lastShotX*30 + offsetX, lastShotY*30+offsetY, null);
			g.setColor(Color.YELLOW);
			g.drawString("Bombs: " +Integer.toString(player.getBombs()), 300, 10);
			g.drawString("Ammo: " +Integer.toString(player.getAmmo()), 200, 10);
			g.drawString("Health: " +Integer.toString(player.getHealth()), 100, 10);
			
			g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			ArrayList<Enemy> enemyChangeBuffer = new ArrayList<Enemy>();
			for (Enemy e : EntityGlobals.getEnemyList()){
				enemyChangeBuffer.add(e);
			}
			for (Enemy e : enemyChangeBuffer){
				if (e.getHealth() == e.getMaxHealth()) continue;
				int ypos = e.getY()-e.getH()-3+offsetY;
				int xpos = e.getX()-e.getW()+offsetX;
				g.setColor(Color.RED);
				g.drawLine(xpos, ypos, e.getX()+e.getW()+offsetX, ypos);
				g.setColor(Color.GREEN);
				float hfrac = ((float)e.getHealth())/((float)e.getMaxHealth());
				g.drawLine(xpos, ypos, xpos + 2*(int)(hfrac * e.getW()), ypos);
			}
			EntityGlobals.setEnemyList(enemyChangeBuffer);
		}
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
				setupGame();
				stage = Stage.LEVEL;
			}
			fog.update(mouseHoverX, mouseHoverY, 500);
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
		case GAMEOVER:
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
		case PAUSE:
			if (buttons.get(BN.PLAY).isClicked()){
			
			}
			fog.update(mouseHoverX, mouseHoverY, 500);
			break;
		case LEVEL:
			if (mp3player.isIdle()) mp3player.play();
			if(boom){
				boom = false;
				if(player.getBombs() > 0){//detonate bomb
					fog.startFlash(120);
					player.modifyBomb(-1);
					player.modifyHealth((int)(-1 * (.06 * player.getHealth())));
					int range = player.getHealth()/10;
					explode(EntityGlobals.getMapArray()[player.getX()/30][player.getY()/30], range);
				}
				//else fog.startHurtFlash(120);
			}
			player.update(up, down, left, right);
			fcount++;
			fcount %= 3;
			if (fcount == 2){
				for (Enemy e : EntityGlobals.getEnemyList()){
					if(e.getType().equals("Boss")){
						if (((Boss)e).update(player.getX(), player.getY())){
							player.modifyHealth(-10);
							fog.startHurtFlash(40);
						}
					}
					else if (e.update(player.getX(), player.getY())){
						player.modifyHealth(-5);
						fog.startHurtFlash(20);
					}
				}
			}
			if (peaceTimer > 0){
				fog.update(player.getX() + offsetX, player.getY() + offsetY, 600);
				peaceTimer--;
				if (peaceTimer==0){//wave begins
				}
			}
			else fog.update(player.getX() + offsetX, player.getY() + offsetY, player.getHealth()*3);
			if(peaceTimer==0 && nextWave){//wave ends
				System.out.println("next Wave");
				nextWave=false;
				this.setupGame();
				peaceTimer = 1200;
			}
			
			if (shotTimer > 0) shotTimer--;
			
			if(player.getHealth() <= 0){
				stage = Stage.GAMEOVER;
				mp3player.close();
				mp3player.changeMusic("/SOUND_menu_theme.mp3");
				mp3player.play();
			}
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
	public void togglePause(){
		if (this.stage == Stage.LEVEL){
			fog.startFlash(100);
			this.stage = Stage.PAUSE;
		}
		else if (this.stage == Stage.PAUSE)
			this.stage = Stage.LEVEL;
	}
	
	public void shoot(int mouseX, int mouseY, int power){
		if (shotTimer > 0) return;
		if (player.getAmmo() < 10) return;
		if ((mouseX- offsetX)/30 < 0 || (mouseX- offsetX)/30 >= 97)
			return;
		if ((mouseY- offsetY)/30 < 0 || (mouseY- offsetY)/30 >= 55)
			return;
		
		GridObj go = EntityGlobals.getMapArray()[(mouseX- offsetX)/30][(mouseY- offsetY)/30];
		if (go.getType().equals("/tile.png")){
			if (((Tile) go).contains(boss.getX(), boss.getY()))
				boss.dealDamage(power);
			else if (((Tile) go).contains(boss.getX()-boss.getW()/2, boss.getY()-boss.getH()/2))
				boss.dealDamage(power);
			else if (((Tile) go).contains(boss.getX()+boss.getW()/2-1, boss.getY()-boss.getH()/2))
				boss.dealDamage(power);
			else if (((Tile) go).contains(boss.getX()-boss.getW()/2, boss.getY()+boss.getH()/2))
				boss.dealDamage(power);
			else if (((Tile) go).contains(boss.getX()+boss.getW()/2-1, boss.getY()+boss.getH()/2))
				boss.dealDamage(power);
			else{
				((Tile) go).setLight(100);
				((Tile) go).dealDamage();
				((Tile) go).setLight(0);
			}
				
			player.addAmmo(-10);
			lastShotX=(mouseX- offsetX)/30;
			lastShotY=(mouseY- offsetY)/30;
			shotTimer = shotTimerMax;
		}
		return;
	}
	private void explode(GridObj go, int power){
		if (power == 0) return;
		if (go.getType().equals("/tile.png")){
			((Tile) go).setLight(2);
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
		
		buttons.add(new Button(screen, 600, 100,
				5, 5, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.UPSPEED);
		buttons.get(BN.UPSPEED).text = "+";
		buttons.get(BN.UPSPEED).state = Button.States.HIDDEN;

		buttons.add(new Button(screen, 600, 160,
				5, 5, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.UPHP);
		buttons.get(BN.UPHP).text = "+";
		buttons.get(BN.UPHP).state = Button.States.HIDDEN;

		buttons.add(new Button(screen, 600, 220,
				5, 5, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.UPPOW);
		buttons.get(BN.UPPOW).text = "+";
		buttons.get(BN.UPPOW).state = Button.States.HIDDEN;
		
		buttons.add(new Button(screen, 600, 280,
				5, 5, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.UPAMMOCONS);
		buttons.get(BN.UPAMMOCONS).text = "+";
		buttons.get(BN.UPAMMOCONS).state = Button.States.HIDDEN;
		
		buttons.add(new Button(screen, 600, 340,
				5, 5, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.UPCRIT);
		buttons.get(BN.UPCRIT).text = "+";
		buttons.get(BN.UPCRIT).state = Button.States.HIDDEN;
		
		buttons.add(new Button(screen, 600, 400,
				5, 5, "/button_disabled.png", "/button_enabled.png", "/button_pressed.png") , BN.UPFIRERATE);
		buttons.get(BN.UPFIRERATE).text = "+";
		buttons.get(BN.UPFIRERATE).state = Button.States.HIDDEN;
		setupGame();
	}
	public void setupGame(){
		EntityGlobals.resetMap();	
		fog = new Fog();
		int randX = 480 * (int)(Math.random() * 6);
		int randY = 270 * (int)(Math.random() * 6);
		offsetX = 8 - randX;
		offsetY = 8 - randY;
		player = new PlayerObj(248+randX , 143 + randY);
		if(getRoomC(player.getX()) < 3 && getRoomR(player.getY()) < 3){
			System.out.println("topleft");
			
		}
		int randX2 = (int)(Math.random() * 2);
		int randY2 = (int)(Math.random() * 2);
		if (randX2 == 0) randX = 100;
		else randX2 = 2850;
		if (randY2 == 0)randY = 100;
		else randY2 = 1560;
		
		if 		(randX2 == 100  && randY2 == 100  && randX < 1440 && randY < 810) randX2 = 2850;
		else if (randX2 == 100  && randY2 == 1560 && randX < 1440 && randY > 810) randY2 = 100;
		else if (randX2 == 2850 && randY2 == 100  && randX > 1440 && randY < 810) randY2 = 160;
		else if (randX2 == 2850 && randY2 == 1560 && randX > 1440 && randY > 810) randX2 = 100;
		boss = new Boss(randX2, randY2);
		EntityGlobals.addEnemy(boss);
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
		MENU, LEVEL, INSTRUCTIONS, CREDITS, GAMEOVER, PAUSE;
	}

	/**
	 * Available button names (Unique)(Self explanatory).
	 * @author AJ
	 *
	 */
	public enum BN {
		PLAY, QUIT, INSTRUCTIONS, CREDITS, UPSPEED, UPHP, UPPOW, UPAMMOCONS, UPCRIT, UPFIRERATE;
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