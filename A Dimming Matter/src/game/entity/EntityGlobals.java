package game.entity;

import java.util.*;

public class EntityGlobals {
	static ArrayList<Enemy> enemyList;
	static ArrayList<Wall> wallList;
	static GridObj[][] mapArray;
	
	public static void resetEntityGlobals(){
		enemyList = new ArrayList();
		wallList = new ArrayList();
	}

	public static void addEnemy( Enemy enemy ){
		enemyList.add(enemy);
	}
	
	public static int lenEnemyList(){
		return enemyList.size();
	}
	
	public static void removeEnemy( Enemy enemy ){
		enemyList.remove(enemyList.indexOf(enemy));
	}
	
	public static ArrayList<Enemy> getEnemyList(){
		return enemyList;
	}
	
	
	public static void addWall( Wall wall ){
		wallList.add(wall);
	}
	
	public static int lenWallList(){
		return wallList.size();
	}
	
	public static void removeWall( Wall wall ){
		wallList.remove(wallList.indexOf(wall));
	}
	
	public static ArrayList<Wall> getWallList(){
		return wallList;
	}
}
