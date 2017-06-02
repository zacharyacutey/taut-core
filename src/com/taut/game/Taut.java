package com.taut.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.taut.game.levels.test.screens.TestScreen;

public class Taut extends Game {

	
	@Override
	public void create () {
		System.out.println("Created game");
		this.setScreen(new TestScreen(this));
	}

	
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
   