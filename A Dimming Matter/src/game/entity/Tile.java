package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;
import java.util.*;

public class Tile implements GridObj {
	
	int x, y, w, h, row, col, light;
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
		ArrayList<Enemy> listofObjects = new ArrayList();
		ArrayList<Enemy> listofEnemies = EntityGlobals.getEnemyList();
		for(int i = 0; i < EntityGlobals.lenEnemyList(); i++){
			if (contains(listofEnemies.get(i).getX(), listofEnemies.get(i).getY())){
				listofObjects.add(listofEnemies.get(i));
			}
		}
		return listofObjects;
	}
	
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
	}

	public String getType() {
		return "/tile.png";
	}

}
