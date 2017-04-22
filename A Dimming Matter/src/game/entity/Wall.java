package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;

public class Wall {
	
	int x, y, w, h;
	public Wall(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 30;
		this.h = 30;
	}
	
	public boolean stopsLight(){
		return this.stopsLight();
	}
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.GREEN);
		g.fillRect(x, y, w, h);
	}

}
