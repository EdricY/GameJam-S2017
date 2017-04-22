package game.entity;


import java.awt.Color;
import java.awt.Graphics;

public class AmmoPack implements GridObj{
	
	int x, y, w, h, ammoContained;
	
	public AmmoPack(int x, int y){
		this.x = x;
		this.y = y;
		this.w = 30;
		this.h = 30;
		this.ammoContained = 12;
	}
	
	public void addAmmo(PlayerObj player){
		player.addAmmo( this.ammoContained );
	}
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
	}

	public String getType() {
		return "AmmoPack";
	}

}
