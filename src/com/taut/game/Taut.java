package com.taut.game;

import com.badlogic.gdx.Game;
import com.taut.game.levels.test.screens.TestScreen;

/**
 * @author porgull
 * Main game class
 */

public class Taut extends Game {

	
	@Override
	public void create () {
		System.out.println("Created game");
		this.setScreen(new TestScreen(this, "Test", "Test"));
	}

	
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
   