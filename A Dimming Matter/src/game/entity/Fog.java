package game.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;
import game.gfx.Sprite;

public class Fog {
	int[][] fogAlpha;
	
	public Fog(){
		fogAlpha = new int[Game.HEIGHT][Game.WIDTH];
	}
	
	public void update(int centerX, int centerY, int radius){
		for (int y = 0; y < fogAlpha.length; y++) {
            for (int x = 0; x < fogAlpha[0].length; x++) {
            	int distsq = distsq(x, y, centerX, centerY);
            	int base = (int) Math.sqrt(distsq);
            	base = (int)(255*(1-((float)(base))/((float)(radius))));
            	base = Math.min(255, base);
            	base = Math.max(0, base);
            	
            	fogAlpha[y][x] = base;
            }
        }
	}
	
	public int getAlpha(int y, int x){
		return fogAlpha[y][x];
	}
	
	public int distsq(int x1, int y1, int x2, int y2){
		return ( (x1-x2)*(x1-x2) ) + ( (y1-y2)*(y1-y2) );
	}
	
}
