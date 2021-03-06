package game.entity;

import game.Game;

public class Boss extends Enemy{
	public int flee;
	int x, y, w, h, health, maxhealth, speed, hellPortalCounter;
	boolean alive, agro;

	public Boss(int x, int y){
		this.x = x;
		this.y = y;
		this.health = 500;
		this.maxhealth = 500;
		speed = 5;
		this.w = 32;
		this.h = 32;
		this.hellPortalCounter = 0;
	}
	public String getType(){
		return "Boss";
	}
	private void killEnemy(){
		Game.nextWave=true;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getW(){
		return this.w;
	}
	public int getH(){
		return this.h;
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
	
	public boolean update(int playerX, int playerY){
		boolean up=false, down=false, left=false, right=false;
		int taxidist = Math.abs(playerY - this.y) + Math.abs(playerX - this.x);
		if (taxidist < 1000) agro = true;
		else agro = false;
		
		if ( (agro == true) && ( this.hellPortalCounter < EntityGlobals.getRoundNum() )){
			for (int i = 0; i < 3; i++){
				int rowToCheck = (this.x / 30) - 1;
				int colToCheck = (this.y / 30) + (-1 + i);
				if(rowToCheck >= 0 && colToCheck >= 0)
				if (EntityGlobals.getMapArray()[rowToCheck][colToCheck].getType() == "/Tile.png" ){
					EntityGlobals.getMapArray()[rowToCheck][colToCheck] = new HellPortal(rowToCheck, colToCheck);
				}
			}
			for (int i = 0; i < 3; i++){
				int rowToCheck = (this.x / 30) + 1;
				int colToCheck = (this.y / 30) + (-1 + i);
				if(rowToCheck >= 0 && colToCheck >=0)
				if (EntityGlobals.getMapArray()[rowToCheck][colToCheck].getType() == "/Tile.png" ){
					EntityGlobals.getMapArray()[rowToCheck][colToCheck] = new HellPortal(rowToCheck, colToCheck);				}
			}
		}
		
		if (agro && flee==0){
			up = playerY < this.y;
			down = playerY > this.y;
			left = playerX < this.x;
			right = playerX > this.x;
			int rand = (int)(Math.random()*10);
			up ^= (rand == 1);
			down ^= (rand == 2);
			left ^= (rand == 3);
			right ^= (rand == 4);
			if (rand == 7){
				Enemy e = new Enemy(this.x, this.y);
				e.setSpeed(7);
				EntityGlobals.enemyList.add(e);
			}
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
		if (taxidist < 32 && flee == 0){
			flee = 25;
			return true;
		}
		return false;
	}
	
	public void update(boolean up, boolean down, boolean left, boolean right){
		if(right)	x+=speed;
		//R-side collision
		if(right && (EntityGlobals.mapArray[(x+16)/30][(y+16)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x+16)/30][(y-16)/30].getType().equals("/wall.png"))){
			x = ((x+8)/30) * 30 - 9;
		}
		if(left)	x-=speed;
		//L-side collision
		if(left && (EntityGlobals.mapArray[(x-16)/30][(y+16)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x-16)/30][(y-16)/30].getType().equals("/wall.png"))){
			x = ((x-8)/30) * 30 + 38;
		}
		if(up) 		y-=speed;
		//T-side collision
		if(up && (EntityGlobals.mapArray[(x-16)/30][(y-16)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x+16)/30][(y-16)/30].getType().equals("/wall.png"))){
			y = ((y-8)/30) * 30 + 38;
		}
		
		if(down)	y+=speed;
		//B-side collision
		if(down && (EntityGlobals.mapArray[(x-16)/30][(y+16)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(x+16)/30][(y+16)/30].getType().equals("/wall.png"))){
			y = ((y+8)/30) * 30 - 9;
		}
	}
}
