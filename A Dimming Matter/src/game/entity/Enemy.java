package game.entity;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
	public int flee;
	int x, y, w, h, health, speed;
	boolean alive, agro;
	
	public Enemy(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 8;
		this.h = 8;
		this.health = 100;
		this.speed = 1;
		this.alive = true;
		this.agro = true;
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
	
	public boolean update(int playerX, int playerY){
		boolean up=false, down=false, left=false, right=false;
		if (agro && flee==0){
			up = playerY < this.y;
			down = playerY > this.y;
			left = playerX < this.x;
			right = playerX > this.x;
		}
		else if (flee>0){
			int rand = (int)(Math.random()*2);
			int rand2 = (int)(Math.random()*2);
			down = rand == 0;
			up = rand == 1;
			right = rand2 == 1;
			left = rand2 == 0;
			flee--;
		}
		update(up,down,left,right);
		if (Math.abs(playerY - this.y) + Math.abs(playerX - this.x) < 16 && flee == 0){
			flee = 100;
			return true;
		}
		return false;
	}
	
	public void update(boolean up, boolean down, boolean left, boolean right){
		if(right)	x+=speed;
		//R-side collision
		if(right && (EntityGlobals.mapArray[(x+8)/30][(y+8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x+8)/30][(y-8)/30].getType().equals("/wall.png"))){
			x = ((x+8)/30) * 30 - 9;
		}
		if(left)	x-=speed;
		//L-side collision
		if(left && (EntityGlobals.mapArray[(x-8)/30][(y+8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x-8)/30][(y-8)/30].getType().equals("/wall.png"))){
			x = ((x-8)/30) * 30 + 38;
		}
		if(up) 		y-=speed;
		//T-side collision
		if(up && (EntityGlobals.mapArray[(x-8)/30][(y-8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x+8)/30][(y-8)/30].getType().equals("/wall.png"))){
			y = ((y-8)/30) * 30 + 38;
		}
		
		if(down)	y+=speed;
		//B-side collision
		if(down && (EntityGlobals.mapArray[(x-8)/30][(y+8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x+8)/30][(y+8)/30].getType().equals("/wall.png"))){
			y = ((y+8)/30) * 30 - 9;
		}
	}
	public void draw(Graphics g){ 
		g.setColor(Color.RED);
		g.fillRect(x, y, w, h);
	}

}
