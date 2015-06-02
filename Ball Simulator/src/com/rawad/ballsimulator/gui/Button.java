package com.rawad.ballsimulator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Button extends GuiComponent {
	
	public Button(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height);
		
		this.x = x - (width/2);
		this.y = y - (height/2);
		
	}
	
	@Override
	public void update(int x, int y) {
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(isHovered()) {
			g.setColor(new Color(~background.getRGB()));
		} else {
			g.setColor(background);
		}
		
		g.fillRect(x, y, width, height);
		
		if(isHovered()) {
			g.setColor(foreground);
			g.drawRect(x, y, width, height);
		}
		
		Font f = g.getFont();
		FontMetrics fm = g.getFontMetrics(f);
		
		int stringWidth = fm.stringWidth(id);
		int stringHeight = fm.getHeight();
		
		int stringX = x + (width/2) - (stringWidth/2);
		int stringY = y + (height/2) + (stringHeight/4);// Since Strings are drawn from baseline (bottom y)
		
		g.setColor(foreground);
		g.drawString(id, stringX, stringY);
		
	}
	
}
