package com.rawad.ballsimulator.client.renderengine;

import java.util.ArrayList;

import com.rawad.ballsimulator.client.renderengine.components.CollisionComponentRender;
import com.rawad.ballsimulator.client.renderengine.components.ComponentRender;
import com.rawad.ballsimulator.client.renderengine.components.HealthComponentRender;
import com.rawad.ballsimulator.client.renderengine.components.SelectionComponentRender;
import com.rawad.ballsimulator.client.renderengine.components.UserComponentRender;
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
		componentRenders.add(new UserComponentRender());
		componentRenders.add(new CollisionComponentRender());
		
	}
	
	public void render(GraphicsContext g, Entity e) {
		
		TransformComponent transformComp = e.getComponent(TransformComponent.class);
		RenderingComponent renderingComp = e.getComponent(RenderingComponent.class);
		
		Image texture = renderingComp.getTexture();
		
		g.translate(transformComp.getX(), transformComp.getY());
		g.rotate(transformComp.getTheta());
		g.scale(transformComp.getScaleX(), transformComp.getScaleY());
		
		g.drawImage(texture, -texture.getWidth() / 2d, -texture.getHeight() / 2d);// Centers texture on origin.
		
		for(ComponentRender<? extends Component> compRender: componentRenders) {
			compRender.render(g, e);
		}
		
		g.rotate(-transformComp.getTheta());
		g.scale(1d / transformComp.getX(), 1d / transformComp.getY());
		
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
