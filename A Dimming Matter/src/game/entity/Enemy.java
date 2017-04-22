package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;

public class Enemy {
	
	int x, y, w, h, health;
	boolean alive, agro;
	
	public Enemy(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 8;
		this.h = 8;
		this.health = 100;
		this.alive = true;
		this.agro = false;
		EntityGlobals.addEnemy(this);
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public void dealDamage(int damage){
		if (this.health - damage < 0){
			this.health = 0;
			this.alive = false;
			killEnemy();
		} else {
			this.health -= damage;
		}
	}
	
	private void killEnemy(){
		EntityGlobals.removeEnemy( this );
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void draw(Graphics g){ 
		g.setColor(Color.RED);
		g.fillRect(x, y, w, h);
	}

}
