package game.entity;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
	public int flee;
	int x, y, w, h, health, speed, maxhealth;
	boolean alive, agro;
	
	public Enemy(){
	}
	public Enemy(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 8;
		this.h = 8;
		this.health = 100;
		this.maxhealth = 100;
		this.speed = 1;
		this.alive = true;
		this.agro = true;
		EntityGlobals.addEnemy(this);
	}
	public String getType(){
		return "Joe";
	}
	public void setSpeed(int num){
		speed = num;
	}
	public int getHealth(){
		return this.health;
	}
	public int getMaxHealth(){
		return this.maxhealth;
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
		if ((int)(Math.random() * 10) < 1 && EntityGlobals.getMapArray()[getX()/30][getY()/30].getType().equals("/tile.png")){
			((Tile)EntityGlobals.getMapArray()[getX()/30][getY()/30]).addHealthOrb();
		}
	}
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	public int getW(){
		return this.w;
	}
	public int getH(){
		return this.h;
	}
	
	public boolean update(int playerX, int playerY){
		boolean up=false, down=false, left=false, right=false;
		if (agro && flee==0){
			up = playerY < this.y;
			down = playerY > this.y;
			left = playerX < this.x;
			right = playerX > this.x;
			int rand = (int)(Math.random()*8);
			up ^= (rand == 1);
			down ^= (rand == 2);
			left ^= (rand == 3);
			right ^= (rand == 4);
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
