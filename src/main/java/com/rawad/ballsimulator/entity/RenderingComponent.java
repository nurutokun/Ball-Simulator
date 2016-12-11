package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;

import javafx.scene.image.Image;

public class RenderingComponent extends Component {
	
	private Image texture;
	
	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	public Image getTexture() {
		return texture;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof RenderingComponent) {
			
			RenderingComponent renderingComp = (RenderingComponent) comp;
			
			renderingComp.setTexture(getTexture());
			
		}
		
		return comp;
	}
	
}
