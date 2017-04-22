package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;

public class Enemy {
	
	int x, y, w, h, health;
	boolean alive;
	
	public Enemy(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 24;
		this.h = 24;
		this.health = 100;
		this.alive = true;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public void dealDamage(int damage){
		if (this.health - damage < 0){
			this.health = 0;
			this.alive = false;
		} else {
			this.health -= damage;
		}
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
