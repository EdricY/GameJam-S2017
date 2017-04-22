package game.entity;

import java.awt.Graphics;
import java.util.ArrayList;

public interface GridObj {
	public String getType();
	public String getPath();
	public void draw(Graphics g);
	public int getR();
	public int getC();
}
