
import com.rawad.ballsimulator.world.World;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
	
	// TODO: Implement UUID's with UUIDManager
//	private final String UUID;
	
	protected World world;
	
	protected Rectangle hitbox;
	
	protected String name;
	
	protected SimpleDoubleProperty x;
	protected SimpleDoubleProperty y;
	
	protected SimpleDoubleProperty width;
	protected SimpleDoubleProperty height;
	
	public Entity(World world) {
		
		this.world = world;
		
		hitbox = new Rectangle();
		
		hitbox.xProperty().bind(xProperty());
		hitbox.yProperty().bind(yProperty());
		hitbox.widthProperty().bind(widthProperty());
		hitbox.heightProperty().bind(heightProperty());
		
		this.world.addEntity(this);
		
		name = "";
		
	}
	
	public abstract void update();
	
	public abstract void render(GraphicsContext g);
	
	public void renderHitbox(GraphicsContext g) {
		
		g.setStroke(Color.BLACK);
		g.strokeRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		
	}
	
	protected void updateHitbox(double x, double y, int width, int height) {
		hitbox.setBounds((int) x, (int) y, width, height);
	}
	
	public void updateHitbox() {
		updateHitbox(x - (width/2), y - (height/2), width, height);
	}
	
	public boolean intersects(Entity e) {
		return hitbox.intersects(e.getHitbox());
	}
	
	public boolean intersects(int x, int y) {
		return hitbox.contains(x, y);
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	/**
	 * 
	 * @return the x-coordinate of the center point of this entity
	 */
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * 
	 * @return the y-coordinate of the center point of this entity
	 */
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * 
	 * @return Current world this entity is in.
	 */
	public World getWorld() {
		return world;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
