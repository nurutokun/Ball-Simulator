package com.rawad.ballsimulator.entity;

import com.rawad.ballsimulator.client.Texture;
import com.rawad.gamehelpers.game.entity.Component;

public class RenderingComponent extends Component {
	
	private Texture texture;
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof RenderingComponent) {
			
			RenderingComponent renderingComp = (RenderingComponent) comp;
			
			renderingComp.setTexture(getTexture());
			
			return renderingComp;
			
		}
		
		return comp;
	}
	
}
