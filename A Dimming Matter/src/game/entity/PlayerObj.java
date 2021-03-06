package game.entity;

import java.awt.Graphics;

import game.Game;

public class PlayerObj {
	int mapx, mapy, w, h, health, maxhealth, ammo, bomb, range, speed, money;
	Direction direction;
	
	public PlayerObj(int mapx, int mapy){
		this.mapx = mapx; //center, not edge
		this.mapy = mapy;
		this.w = 16;
		this.h = 16;
		this.maxhealth = 100 + 10 * Game.upmaxhealth;
		this.health = maxhealth;
		this.ammo = 12;
		this.bomb = 3;
		this.range = 10;
		this.direction = Direction.UP;
		this.speed = 1 + Game.upspeed;
		this.money = 3;
	}
	
	public int getHealth(){
		return this.health;
	}
	public int getMaxHealth(){
		return this.maxhealth;
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
	
	public void modifyBomb(int num){
		this.bomb += num;
		if (this.bomb < 0) this.bomb = 0;
	}
	public void modifyHealth(int num){
		this.health += num;
		if (this.health < 0) this.health = 0;
	}
	public void modifyMaxHealth(int num){
		this.maxhealth += num;
		this.health += num;
	}
	public void addMoney( int moneyToAdd ){
		this.money += moneyToAdd;
	}
	
	public int getMoney(){
		return this.money;
	}
	
	public void spendMoney( int moneySpent ){
		this.money -= moneySpent;
	}
	
//	public void basicAttack(int mouseX, int mouseY){
//		
//	}
	
//	private ArrayList<GridObj> getInRangeGridObjs(){
//		ArrayList<GridObj> inRangeGridObjs = new ArrayList();
//		int rangeChecked = 0;
//		while (rangeChecked < this.range){
//			
//		}
//	}
//	
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
		if (health > maxhealth) health--;
		if (this.ammo < 10 && (int)(Math.random() * 50) == 0) this.addAmmo(1);
		this.speed = 1 + Game.upspeed;
		this.maxhealth = 100 + 10 * Game.upmaxhealth;
		if(right)	mapx+=speed;
		//R-side collision
		if(right && (EntityGlobals.mapArray[(mapx+8)/30][(mapy+8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(mapx+8)/30][(mapy-8)/30].getType().equals("/wall.png"))){
			mapx = ((mapx+8)/30) * 30 - 9;
		}
		if(left)	mapx-=speed;
		//L-side collision
		if(left && (EntityGlobals.mapArray[(mapx-8)/30][(mapy+8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(mapx-8)/30][(mapy-8)/30].getType().equals("/wall.png"))){
			mapx = ((mapx-8)/30) * 30 + 38;
		}
		if(up) 		mapy-=speed;
		//T-side collision
		if(up && (EntityGlobals.mapArray[(mapx-8)/30][(mapy-8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(mapx+8)/30][(mapy-8)/30].getType().equals("/wall.png"))){
			mapy = ((mapy-8)/30) * 30 + 38;
		}
		
		if(down)	mapy+=speed;
		//B-side collision
		if(down && (EntityGlobals.mapArray[(mapx-8)/30][(mapy+8)/30].getType().equals("/wall.png") ||
				EntityGlobals.mapArray[(mapx+8)/30][(mapy+8)/30].getType().equals("/wall.png"))){
			mapy = ((mapy+8)/30) * 30 - 9;
		}
		
		if(mapx + Game.offsetX > Game.WIDTH - Game.WIDTH/4)
			Game.offsetX -=speed;
		if(mapy + Game.offsetY > Game.HEIGHT - Game.HEIGHT/4)
			Game.offsetY -=speed;
		if(mapx + Game.offsetX < Game.WIDTH/4)
			Game.offsetX +=speed;
		if(mapy + Game.offsetY < Game.HEIGHT/4)
			Game.offsetY +=speed;
		if(playerTile() != null && playerTile().getType()=="/tile.png"){
			if (((Tile)playerTile()).getAmmo() > 0){
				((Tile)playerTile()).addAmmo(this);
				Game.ammoSound();
			}
			if (((Tile)playerTile()).isHealthOrb()){
				((Tile)playerTile()).removeHealthOrb();
				this.health += .1*this.maxhealth;
				Game.ammoSound();
			}
		}
	}
	public void draw(Graphics g){ 
//		g.setColor(Color.WHITE);
//		g.fillRect(x, y, w, h);
	}
	public GridObj playerTile(){
		return EntityGlobals.getMapArray()[getX()/30][getY()/30];
	}
}
