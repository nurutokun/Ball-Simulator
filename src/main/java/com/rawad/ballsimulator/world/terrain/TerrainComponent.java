package com.rawad.ballsimulator.world.terrain;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TerrainComponent {
	
	/** Temporary */
	private Color highlightColor;
	
	private Rectangle hitbox;
	
	private SimpleDoubleProperty x;
	private SimpleDoubleProperty y;
	
	private SimpleDoubleProperty width;
	private SimpleDoubleProperty height;
	
	private boolean selected;
	
	public TerrainComponent(double x, double y, double width, double height) {
		
		highlightColor = Color.GREEN;
		
		this.x = new SimpleDoubleProperty(x);
		this.y = new SimpleDoubleProperty(y);
		
		this.width = new SimpleDoubleProperty(width);
		this.height = new SimpleDoubleProperty(height);
		
		hitbox = new Rectangle(x, y, width, height);
		
		hitbox.xProperty().bind(xProperty());
		hitbox.yProperty().bind(yProperty());
		hitbox.widthProperty().bind(widthProperty());
		hitbox.heightProperty().bind(heightProperty());
		
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public SimpleDoubleProperty xProperty() {
		return x;
	}
	
	public double getX() {
		return xProperty().get();
	}
	
	public void setX(double x) {
		xProperty().set(x);
	}
	
	public SimpleDoubleProperty yProperty() {
		return y;
	}
	
	public double getY() {
		return yProperty().get();
	}
	
	public void setY(double y) {
		yProperty().set(y);
	}
	
	public SimpleDoubleProperty widthProperty() {
		return width;
	}
	
	public double getWidth() {
		return widthProperty().get();
	}
	
	public void setWidth(double width) {
		widthProperty().set(width);
	}
	
	public SimpleDoubleProperty heightProperty() {
		return height;
	}
	
	public double getHeight() {
		return heightProperty().get();
	}
	
	public void setHeight(double height) {
		heightProperty().set(height);
	}
	
	public void setHighlightColor(Color color) {
		this.highlightColor = color;
	}
	
	public Color getHighlightColor() {
		return highlightColor;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
}
