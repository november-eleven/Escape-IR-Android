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
 * This class implements the {@link MoonShot}.
 * 
 * @see AbstractShot
 */
public class MoonShot extends AbstractShot {
	private final Texture coreMoonShot;
	
	private boolean isVisible;

	/**
	 * {@link MoonShot} constructor.
	 * 
	 * @param body : The {@link Shot} JBox2D {@link Body}.
	 * @param container : The {@link EntityContainer} that contains the {@link Shot}.
	 * @param collisionBehavior : The {@link CollisionBehavior} use by the {@link Shot}
	 */
	public MoonShot(Engine engine, Body body, EntityContainer container, CollisionBehavior collisionBehavior) {
		super(engine, body, container, container, collisionBehavior, 5);

		this.coreMoonShot = engine.getResources().getTexture(TextureLoader.MOON_SPECIAL);
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
				isVisible = true;
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
			Rect area = getEdge();
			graphics.draw(coreMoonShot, area.left, area.top, area.right, area.bottom, getAngle());
		}
	}

	@Override
	public void update(Graphics graphics, long delta) {
		Objects.requireNonNull(graphics);
		
		draw(graphics);
		
		if(!getEdgeNotifier().isInside(getEdge())) {
			getEdgeNotifier().edgeReached(this);
		}
	}

	@Override
	protected Rect getEdge() {
		int x = getEngine().getConverter().toPixelX(getX());
		int y = getEngine().getConverter().toPixelY(getY());
		
		return new Rect(x - (coreMoonShot.getWidth() / 2), y - (coreMoonShot.getHeight() / 2), x + (coreMoonShot.getWidth() / 2), y + (coreMoonShot.getHeight() / 2));
	}

}
