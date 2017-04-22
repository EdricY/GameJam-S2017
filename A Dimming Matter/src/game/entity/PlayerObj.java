package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;

public class PlayerObj {
	
	int x, y, w, h, health, ammo, bomb, range;
	
	public PlayerObj(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 24;
		this.h = 24;
		this.health = 100;
		this.ammo = 12;
		this.bomb = 0;
		this.range = 10;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public int getAmmo(){
		return this.ammo;
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
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void draw(Graphics g){ 
		g.setColor(Color.WHITE);
		g.fillRect(x, y, w, h);
	}
}
