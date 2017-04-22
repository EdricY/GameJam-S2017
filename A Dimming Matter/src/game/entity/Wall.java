package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;

public class Wall implements GridObj{
	
	int x, y, w, h, row, col;
	
	public Wall(int row, int col){
		this.x = row * 30;
		this.y = col * 30;
		this.w = 30;
		this.h = 30;
		this.row = row;
		this.col = col;
	}
	
	public void destroyWall(){
		EntityGlobals.getMapArray()[row][col] = new Tile(row, col);
	}
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.GRAY);
		g.fillRect(x, y, w, h);
	}

	public String getType() {
		return "/wall.png";
	}

}
