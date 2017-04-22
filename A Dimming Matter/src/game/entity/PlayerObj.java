package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.Game;
import game.gfx.Screen;
import java.util.*;

public class PlayerObj {
	
	int mapx, mapy, w, h, health, ammo, bomb, range, speed;
	Direction direction;
	
	public PlayerObj(int mapx, int mapy){
		this.mapx = mapx; //center, not edge
		this.mapy = mapy;
		this.w = 24;
		this.h = 24;
		this.health = 100;
		this.ammo = 12;
		this.bomb = 0;
		this.range = 10;
		this.direction = Direction.UP;
		this.speed = 10;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public int getAmmo(){
		return this.ammo;
	}
	
	public int getBombs(){
		return this.bomb;
	}
	
	public void addAmmo(int ammoAdded){
		this.ammo += ammoAdded;
	}
	
	public void addBomb(){
		this.bomb += 1;
	}
	
//	public void basicAttack(int mouseX, int mouseY){
//		
//	}
	
//	private ArrayList<GridObj> getInRangeGridObjs(){
//		ArrayList<GridObj> inRangeGridObjs = new ArrayList();
//		int rangeChecked = 0;
//		while (rangeChecked < this.range){
//			this.row 
//		}
//	}
	
	public int getX(){
		return this.mapx;
	}
	
	public int getY(){
		return this.mapy;
	}
	
	public Direction getDir(){
		return this.direction;
	}
	public void update(boolean up, boolean down, boolean left, boolean right){
		int prevX = mapx;
		int prevY = mapy;
		if(up) 		mapy-=speed;
		if(down)	mapy+=speed;
		if(left)	mapx-=speed;
		if(right)	mapx+=speed;
		if(mapx + Game.offsetX > Game.WIDTH - Game.WIDTH/4)
			Game.offsetX -=speed;
		if(mapy + Game.offsetY > Game.HEIGHT - Game.HEIGHT/4)
			Game.offsetY -=speed;
		if(mapx + Game.offsetX < Game.WIDTH/4)
			Game.offsetX +=speed;
		if(mapy + Game.offsetY < Game.HEIGHT/4)
			Game.offsetY +=speed;
		//R-side collision
		if(EntityGlobals.mapArray[(mapx+12)/30][(mapy+12)/30].getType().equals("/wall.png")){
			
		}
	}
	public void draw(Graphics g){ 
//		g.setColor(Color.WHITE);
//		g.fillRect(x, y, w, h);
	}
}
