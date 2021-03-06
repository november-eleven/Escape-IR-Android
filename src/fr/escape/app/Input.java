/*****************************************************************************
 * 
 * Copyright 2012-2013 See AUTHORS file.
 * 
 * This file is part of Escape-IR.
 * 
 * Escape-IR is free software: you can redistribute it and/or modify
 * it under the terms of the zlib license. See the COPYING file.
 * 
 *****************************************************************************/

package fr.escape.app;

import android.view.MotionEvent;
import fr.escape.Objects;

/**
 * <p>
 * A Wrapper for User Input.
 * 
 * <p>
 * This class can handle User Input with various information.
 * 
 */
public final class Input {
	
	/**
	 * Delta to apply on filter.
	 */
	private static final int DELTA_FILTER = 4;
	
	/**
	 * User Input X Coordinate.
	 */
	private final int x;
	
	/**
	 * User Input Y Coordinate.
	 */
	private final int y;
	
	/**
	 * User Input X Velocity.
	 */
	private final float velocityX;
	
	/**
	 * User Input Y Velocity.
	 */
	private final float velocityY;
	
	/**
	 * User Input Kind
	 */
	private final Action action;
	
	/**
	 * Default Constructor
	 * 
	 * @param event Mouse Input Event
	 */
	public Input(MotionEvent event) {
		this(event, 0.0f, 0.0f);
	}
	
	/**
	 * Constructor with Velocity Information.
	 * 
	 * @param event Mouse Input Event
	 * @param x X Velocity
	 * @param y Y Velocity
	 */
	public Input(MotionEvent event, float x, float y) {
		
		Objects.requireNonNull(event);
		this.x = (int) event.getX();
		this.y = (int) event.getY();
		this.velocityX = x;
		this.velocityY = y;
		this.action = Action.create(event.getAction());
	}

	/**
	 * Get Mouse Input X Coordinate.
	 * 
	 * @return X coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get Mouse Input Y Coordinate.
	 * 
	 * @return Y coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Get Mouse Input X Velocity.
	 * 
	 * @return X velocity
	 */
	public float getXVelocity() {
		return velocityX;
	}
	
	/**
	 * Get Mouse Input Y Velocity.
	 * 
	 * @return Y velocity
	 */
	public float getYVelocity() {
		return velocityY;
	}
	
	/**
	 * Get Mouse Input Action.
	 * 
	 * @return Mouse Action
	 */
	public Action getAction() {
		return action;
	}
	
	@Override
	public String toString() {
		return getX()+" "+getY()+": "+getAction();
	}
	
	/**
	 * Should we need to filter and delete this new Input from the last one.
	 */
	public boolean filter(Input input) {
		
		double mDelta = Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
		double aDelta = Math.sqrt(Math.pow(input.getX(), 2) + Math.pow(input.getY(), 2));
		
		if(Math.abs(mDelta - aDelta) < DELTA_FILTER) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Filter for MotionEvent Action: Keep it simple.
	 */
	public enum Action {
		
		ACTION_DOWN, ACTION_UP, ACTION_MOVE, ACTION_UNKNOWN;
		
		static Action create(int action) {
			switch(action) {
				case MotionEvent.ACTION_UP: {
					return ACTION_UP;
				}
				case MotionEvent.ACTION_DOWN: {
					return ACTION_DOWN;
				}
				case MotionEvent.ACTION_MOVE: {
					return ACTION_MOVE;
				}
				default: {
					return ACTION_UNKNOWN;
				}
			}
		}
		
	}
	
}
