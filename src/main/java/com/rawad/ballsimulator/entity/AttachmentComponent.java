package com.rawad.ballsimulator.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

/**
 * Represents an {@code Entity} that can attach to another entity.
 * 
 * @author Rawad
 *
 */
public class AttachmentComponent extends Component {
	
	private Entity attachedTo;
	
	/**
	 * @return the attachedTo
	 */
	public Entity getAttachedTo() {
		return attachedTo;
	}
	
	/**
	 * @param attachedTo the attachedTo to set
	 */
	public void setAttachedTo(Entity attachedTo) {
		this.attachedTo = attachedTo;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof AttachmentComponent) {
			
			AttachmentComponent attachmentComp = (AttachmentComponent) comp;
			
			attachmentComp.setAttachedTo(getAttachedTo());
			
		}
		
		return comp;
		
	}
	
}
