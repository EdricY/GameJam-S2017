package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;

public class PlayerObj {
	
	int x, y, w, h, health, ammo;
	
	public PlayerObj(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 24;
		this.h = 24;
		this.health = 100;
		this.ammo = 100;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public int getAmmo(){
		return this.ammo;
	}
	
	public void addAmmo(int ammoAdded){
		this.ammo = ammoAdded;
	}
	
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
