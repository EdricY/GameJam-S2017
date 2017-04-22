package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;
import java.util.*;

public class Tile {
	
	int x, y, w, h, light;
	public Tile(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 30;
		this.h = 30;
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
		ArrayList<Enemy> listofEnemies = entityGlobals.getEnemyList();
		for(int i = 0; i < entityGlobals.lenEnemyList(); i++){
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

}
