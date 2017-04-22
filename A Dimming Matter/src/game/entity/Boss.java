package game.entity;

import game.Game;

public class Boss extends Enemy{
	public int flee;
	int x, y, w, h, health, speed;
	boolean alive, agro;

	public Boss(int x, int y){
		this.x = x;
		this.y = y;
		speed = 2;
		this.w = 30;
		this.h = 30;
	}
	
	private void killEnemy(){
		EntityGlobals.removeEnemy( this );
		Game.nextWave=true;
	}
}
