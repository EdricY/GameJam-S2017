package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import java.util.*;

public class HellPortal implements GridObj {
	
	int x, y, w, h, row, col, health;
	boolean agro;
	
	public HellPortal(int row, int col){
		this.x = row * 30;
		this.y = col * 30;
		this.w = 30;
		this.h = 30;
		this.row = row;
		this.col = col;
		this.agro = false;
		this.health = 100;
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
	
	public boolean getAgro(){
		return this.agro;
	}
	
	public void setAgro(){
		this.agro = true;
		spawnEnemies( EntityGlobals.getRoundNum() );
	}
	
	public void spawnEnemies( int iterations ){
		for (int j = 0; j < iterations; j++){
			for (int i = 0; i < 3; i++){
				int rowToCheck = this.row - 1;
				int colToCheck = this.col + (-1 + i);
				if (EntityGlobals.getMapArray()[rowToCheck][colToCheck].getType() == "/Tile.png" ){
					EntityGlobals.enemyList.add(new Enemy(rowToCheck*30, colToCheck*30));
				}
			}
			for (int i = 0; i < 3; i++){
				int rowToCheck = this.row + 1;
				int colToCheck = this.col + (-1 + i);
				if (EntityGlobals.getMapArray()[rowToCheck][colToCheck].getType() == "/Tile.png" ){
					EntityGlobals.enemyList.add(new Enemy(rowToCheck*30, colToCheck*30));
				}
			}
		}
	}
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
	}

	public String getType() {
		return "/hellportal.png";
	}

	public String getPath() {
		return "/hellportal.png";
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
