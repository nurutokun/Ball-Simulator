package com.rawad.ballsimulator.game;

/**
 * @author Rawad
 *
 */
public final class MovementRequest {
	
	private final boolean up;
	private final boolean down;
	private final boolean right;
	private final boolean left;
	
	/**
	 * @param up
	 * @param down
	 * @param right
	 * @param left
	 */
	public MovementRequest(boolean up, boolean down, boolean right, boolean left) {
		super();
		
		this.up = up;
		this.down = down;
		this.right = right;
		this.left = left;
		
	}
	
	public boolean isUp() {
		return up;
	}
	
	public boolean isDown() {
		return down;
	}
	
	public boolean isRight() {
		return right;
	}
	
	public boolean isLeft() {
		return left;
	}
	
}
