package game.entity;

import game.Game;

public class Fog {
	int[][] fogAlpha;
	int[][] fogBrighten;
	int[][] fogRedden;
	int flashtimer = 0;
	int flashmax = 0;
	int hurttimer = 0;
	int hurtmax = 0;
	
	
	public Fog(){
		fogAlpha = new int[Game.HEIGHT][Game.WIDTH];
		fogBrighten = new int[Game.HEIGHT][Game.WIDTH];
		fogRedden = new int[Game.HEIGHT][Game.WIDTH];
	}
	
	public void update(int centerX, int centerY, int radius){
		int sqradius = radius*radius;
		fogAlpha = new int[Game.HEIGHT][Game.WIDTH];
		fogBrighten = new int[Game.HEIGHT][Game.WIDTH];
		fogRedden = new int[Game.HEIGHT][Game.WIDTH];
		
		for (int y = 0; y < fogAlpha.length; y++) {
            for (int x = 0; x < fogAlpha[0].length; x++) {
            	int distsq = distsq(x, y, centerX, centerY);
            	if (distsq <= sqradius){ //optimization attempt
	            	//int base = (int) Math.sqrt(distsq);
	            	int base = (int)(255*(1-((float)(distsq))/((float)(sqradius))));
	            	base = Math.min(255, base);
	            	base = Math.max(0, base);
	            	
	            	fogAlpha[y][x] = base;
	            	if (flashtimer != 0){
	            		float frac = ((float)flashtimer)/((float)flashmax);
	            		int delta = (int)(192*frac*frac);
	            		fogBrighten[y][x] = delta;
	            	}
	            	if (hurttimer != 0){
	            		float frac2 = ((float)hurttimer)/((float)hurtmax);
	            		int delta2 = (int)(192*frac2*frac2);
	            		fogRedden[y][x] = delta2;
	            	}
            	}
            }
        }
		if (flashtimer != 0) flashtimer--;
		if (hurttimer != 0) hurttimer--;
		//System.out.println("flashing " + flashtimer);
	}
	
	public int getAlpha(int y, int x){
		return fogAlpha[y][x];
	}
	public int getBrightness(int y, int x){
		return fogBrighten[y][x];
	}
	public int getRedder(int y, int x){
		return fogRedden[y][x];
	}
	public int distsq(int x1, int y1, int x2, int y2){
		return ( (x1-x2)*(x1-x2) ) + ( (y1-y2)*(y1-y2) );
	}
	public void startFlash(int frames){
		flashtimer = frames;
		flashmax = frames;
	}
	public int getFlashTimer(){
		return flashtimer;
	}
	public void startHurtFlash(int frames){
		hurttimer = frames;
		hurtmax = frames;
	}
	public int getHurtTimer(){
		return hurttimer;
	}
}
