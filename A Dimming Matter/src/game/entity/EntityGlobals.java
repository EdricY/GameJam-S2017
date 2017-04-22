package game.entity;

import java.util.*;

public class EntityGlobals {
	static ArrayList<Enemy> enemyList;
	static ArrayList<Wall> wallList;
	static GridObj[][] mapArray;
	static int roundNumber = 0;
	static int rowCount = 6 * 9;
	static int colCount = 6 * 16;
	
	
	public static void resetMap(){
		roundNumber += 1;
		enemyList = new ArrayList();
		wallList = new ArrayList();
		mapArray = new GridObj[colCount][rowCount];
		populateMap( roundNumber );
	}
	
	private static void populateMap( int currentRound ){
		for (int i = 0; i < colCount; i++){
			for (int j = 0; j < rowCount; j++){
				if (((i % 16 == 0) && (j % 9 == 0)) || (i == 0) || (j == 0) || ((i == colCount) || (j == rowCount)) ){
					mapArray[i][j] = new Wall(i, j);
				} else if ((i % 16 == 0) || (j % 9 == 0)){
					if ((int)(Math.random() * 15) < 14){
						mapArray[i][j] = new Wall(i, j);
					} else {
						mapArray[i][j] = new Tile(i, j);
					}
				} else if((int)(Math.random() * 100) == 0){
					mapArray[i][j] = new AmmoPack(i, j);
				} else {
					mapArray[i][j] = new Tile(i, j);
				}
			}
		}
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
	
	public static GridObj[][] getMapArray(){
		return mapArray;
	}
}
