package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.scene.image.Image;

public class RenderingComponent extends Component {
	
	private int texture = TextureResource.UNKNOWN;
	
	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	public Image getTexture() {
		return ResourceManager.getTexture(getTextureLocation());
	}
	
	public TextureResource getTextureObject() {
		return ResourceManager.getTextureObject(getTextureLocation());
	}
	
	public int getTextureLocation() {
		return texture;
	}
	
}
