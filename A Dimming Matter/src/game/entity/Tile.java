package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import java.util.*;

public class Tile implements GridObj {
	
	int x, y, w, h, row, col, light, ammo;
	public Tile(int row, int col){
		this.x = row * 30;
		this.y = col * 30;
		this.w = 30;
		this.h = 30;
		this.row = row;
		this.col = col;
		this.light = 0;
	}
	
	public void setLight(int Light){
		this.light = Light;
	}
	
	public void dealDamage(){
		ArrayList<Enemy> listofEnemies = enemiesIn();
		for( int i = 0; i < listofEnemies.size(); i++ ){
			listofEnemies.get(i).dealDamage(this.light);
		}
	}
	
	public boolean contains(int x, int y){
		return (x >= this.x && x <= this.x + this.w
				&& y > this.y && y <= this.y + this.h);
	}
	
	public ArrayList<Enemy> enemiesIn(){
		ArrayList<Enemy> listofObjects = new ArrayList<Enemy>();
		ArrayList<Enemy> listofEnemies = EntityGlobals.getEnemyList();
		for(int i = 0; i < EntityGlobals.lenEnemyList(); i++){
			if (contains(listofEnemies.get(i).getX(), listofEnemies.get(i).getY())){
				listofObjects.add(listofEnemies.get(i));
			}
		}
		return listofObjects;
	}
	
	public void addAmmo(PlayerObj player){
		player.addAmmo( this.ammo );
		this.ammo = 0;
		if((int)(Math.random() * 12) == 0)
			player.modifyBomb(1);
	}
	
	public void setAmmo(int num){
		this.ammo = num;
	}
	
	public int getAmmo(){
		return this.ammo;
	}
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
	}

	public String getType() {
		return "/tile.png";
	}

	public String getPath() {
		if (ammo == 0)
			return "/tile.png";
		else
			return "/ammo.png";
	}

	@Override
	public int getR() {
		return row;
	}

	@Override
	public int getC() {
		return col;
	}
}
