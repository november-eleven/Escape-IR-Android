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

package fr.escape.game.ui;

import android.graphics.Color;
import fr.escape.Objects;
import fr.escape.game.Escape;
import fr.escape.game.User;
import fr.escape.game.message.Receiver;
import fr.escape.graphics.Font;
import fr.escape.resources.FontLoader;

/**
 * <p>
 * An Overlay which show the User Highscore at the given time.
 * 
 * <p>
 * Information are received from {@link User}.
 */
public final class UIHighscore extends AbstractOverlay implements Receiver {

	/**
	 * Rendering
	 */
	private static final int TOP_PADDING = 20;
	private static final int LEFT_MARGIN = 10;
	
	private final Escape game;
	private final Font font;
	private final int color;
	
	/**
	 * Highscore to show
	 */
	private int highscore;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 */
	public UIHighscore(Escape game) {
		this.game = Objects.requireNonNull(game);
		this.font = new Font(game.getResources().getFont(FontLoader.VISITOR_ID));
		this.color = Color.WHITE;
		this.highscore = 0;
	}

	@Override
	public void render(long delta) {
		if(isVisible()) {
			game.getGraphics().draw("Highscore: "+highscore, LEFT_MARGIN, TOP_PADDING, font, color);
		}
	}

	@Override
	public void receive(int message) {
		setHighscore(message);
	}
	
	/**
	 * Set the Highscore to display.
	 * 
	 * @param highscore Highscore
	 */
	private void setHighscore(int highscore) {
		this.highscore = highscore;
	}

}
