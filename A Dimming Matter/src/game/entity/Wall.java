package game.entity;

import java.awt.Color;
import java.awt.Graphics;

public class Wall implements GridObj{
	
	int x, y, w, h, row, col;
	boolean indestructable;
	String path;
	
	public Wall(int row, int col, boolean indestructable){
		this.x = row * 30;
		this.y = col * 30;
		this.w = 30;
		this.h = 30;
		this.row = row;
		this.col = col;
		this.indestructable = indestructable;
		int randomInd = (int)(Math.random() * 3);
		if (randomInd == 0){
			path =  "/wall0.png";
		} else if (randomInd == 1) {
			path = "/wall1.png";
		} else {
			path = "/wall2.png";
		}
		EntityGlobals.addWall(this);
	}
	
	public void destroyWall(){
		if (!indestructable)
			EntityGlobals.getMapArray()[row][col] = new Tile(row, col);
	}
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.GRAY);
		g.fillRect(x, y, w, h);
	}

	public String getType() {
		return "/wall.png";
		// we dumb png for no reason
	}
	
	public String getPath(){
		return path;
	}

	@Override
	public int getR() {
		// TODO Auto-generated method stub
		return row;
	}

	@Override
	public int getC() {
		// TODO Auto-generated method stub
		return col;
	}
}
