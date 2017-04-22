package game.entity;


import java.awt.Color;
import java.awt.Graphics;

public class AmmoPack{
	
	int x, y, w, h, row, col, ammoContained;
	boolean bombContained;
	
	public AmmoPack(int row, int col){
		this.x = row * 30;
		this.y = col * 30;
		this.w = 30;
		this.h = 30;
		this.row = row;
		this.col = col;
		this.ammoContained = 12;
		this.bombContained = (Math.random() * 16 == 0);
	}
	
	public void addAmmo(PlayerObj player){
		player.addAmmo( this.ammoContained );
	}
	
	public void draw(Graphics g)
	{ 
		g.setColor(Color.BLUE);
		g.fillRect(x, y, w, h);
	}

	public String getType() {
		return "/ammo.png";
	}

	
	public String getPath() {
		return "/ammo.png";
	}
}