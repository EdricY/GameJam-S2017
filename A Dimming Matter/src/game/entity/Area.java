package game.entity;

import java.awt.Color;
import java.awt.Graphics;

import game.gfx.Screen;

public class Area {
	
	int x, y, w, h;
	public Area(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public boolean contains(int x, int y){
		return (x >= this.x && x <= this.x + this.w
				&& y > this.y && y <= this.y + this.h);
	}
	public void draw(Graphics g)
	{ 
		g.setColor(Color.BLUE);
		g.fillRect(x, y, w, h);
	}

}
