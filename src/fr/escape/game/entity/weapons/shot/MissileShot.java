/*****************************************************************************
 * 
 * Copyright 2012 See AUTHORS file.
 * 
 * This file is part of Escape-IR.
 * 
 * Escape-IR is free software: you can redistribute it and/or modify
 * it under the terms of the zlib license. See the COPYING file.
 * 
 *****************************************************************************/

package fr.escape.game.entity.weapons.shot;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import android.graphics.Rect;

import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.app.Graphics;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.EntityContainer;
import fr.escape.graphics.Texture;
import fr.escape.resources.TextureLoader;

/**
 * This class implements the {@link MissileShot}.
 * 
 * @see AbstractShot
 */
public class MissileShot extends AbstractShot {
	
	private final Texture coreMissile;
	
	private boolean isVisible;

	/**
	 * {@link MissileShot} constructor.
	 * 
	 * @param body : The {@link Shot} JBox2D {@link Body}.
	 * @param container : The {@link EntityContainer} that contains the {@link Shot}.
	 * @param collisionBehavior : The {@link CollisionBehavior} use by the {@link Shot}
	 */
	public MissileShot(Engine engine, Body body, EntityContainer container, ShotCollisionBehavior collisionBehavior) {
		super(engine, body, container, container, collisionBehavior, 2);
		
		this.coreMissile = engine.getResources().getTexture(TextureLoader.WEAPON_MISSILE_SHOT);
		this.isVisible = false;
	}

	@Override
	public void receive(int message) {
		switch(message) {
			case Shot.MESSAGE_LOAD: {
				isVisible = true;
				break;
			}
			case Shot.MESSAGE_FIRE: {
				break;
			}
			case Shot.MESSAGE_CRUISE: {
				
				break;
			}
			case Shot.MESSAGE_HIT: {
				getBody().setType(BodyType.STATIC);
				getBody().setLinearVelocity(new Vec2(0.0f, 0.0f));
			}
			case Shot.MESSAGE_DESTROY: {
				
				isVisible = false;
				
				getEngine().post(new Runnable() {
					
					@Override
					public void run() {
						toDestroy();
					}
					
				});
	
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown Message: "+message);
			}
		}
	}

	@Override
	public void draw(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		if(isVisible) {
			drawCoreMissile(Objects.requireNonNull(graphics));
		}
	}

	private void drawCoreMissile(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		int x = getEngine().getConverter().toPixelX(getBody().getPosition().x) - coreMissile.getWidth() / 2;
		int y = getEngine().getConverter().toPixelY(getBody().getPosition().y) - coreMissile.getHeight() / 2;
		
		graphics.draw(coreMissile, x, y, getAngle());
	}

	@Override
	public void update(Graphics graphics, long delta) {
		Objects.requireNonNull(graphics);
		
		draw(Objects.requireNonNull(graphics));
		
		if(!getEdgeNotifier().isInside(getEdge())) {
			getEdgeNotifier().edgeReached(this);
		}
	}

	@Override
	protected Rect getEdge() {
		
		int x = getEngine().getConverter().toPixelX(getX());
		int y = getEngine().getConverter().toPixelY(getY());
		
		return new Rect(x - (coreMissile.getWidth() / 2), y - (coreMissile.getHeight() / 2), x + (coreMissile.getWidth() / 2), y + (coreMissile.getHeight() / 2));
	}

}
