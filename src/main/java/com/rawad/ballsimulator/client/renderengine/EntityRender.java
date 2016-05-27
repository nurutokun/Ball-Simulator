package com.rawad.ballsimulator.client.renderengine;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.renderengine.components.CollisionComponentRender;
import com.rawad.ballsimulator.client.renderengine.components.ComponentRender;
import com.rawad.ballsimulator.client.renderengine.components.HealthComponentRender;
import com.rawad.ballsimulator.client.renderengine.components.SelectionComponentRender;
import com.rawad.ballsimulator.entity.RenderingComponent;
import com.rawad.ballsimulator.entity.TransformComponent;
import com.rawad.gamehelpers.client.renderengine.Render;
import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EntityRender extends Render {
	
	private ArrayList<ComponentRender<? extends Component>> componentRenders = new 
			ArrayList<ComponentRender<? extends Component>>();
	
	public EntityRender() {
		super();
		
		componentRenders.add(new SelectionComponentRender());
		componentRenders.add(new HealthComponentRender());
		componentRenders.add(new CollisionComponentRender());
		
	}
	
	public void render(GraphicsContext g, Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		Image texture = renderingComp.getTexture();
		
		final double width = texture.getWidth() * transformComp.getScaleX();// Don't scale graphics context b/c it will
		final double height = texture.getHeight() * transformComp.getScaleY();// distort x,y values.
		
		g.translate(transformComp.getX() + (width / 2d), transformComp.getY() + (height / 2d));
		g.rotate(transformComp.getTheta());
		
		g.drawImage(texture, -width / 2d, -height / 2d, width, height);
		
		g.rotate(-transformComp.getTheta());
		g.translate(-width / 2d, -height / 2d);
		
		for(ComponentRender<? extends Component> compRender: componentRenders) {
			compRender.render(g, e);
		}
		
		g.translate(-transformComp.getX(), -transformComp.getY());
		
	}
	
	/**
	 * Returns a list of the current {@code ComponentRender} objects.
	 * 
	 * @return
	 */
	public ArrayList<ComponentRender<? extends Component>> getComponentRenders() {
		return componentRenders;
	}
	
}
