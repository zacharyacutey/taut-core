package com.taut.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * @author porgull
 * Main game class
 */

public class Taut extends Game {

	
	@Override
	public void create () {
		System.out.println("Created game");
		this.setScreen(new SplashScreen(this));
	}

	
	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
		
		System.out.println("Screen switched to: " + screen.getClass().getName());
	}
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
   