package com.taut.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen extends ScreenAdapter implements InputProcessor {

	Taut game;
	Texture splashScreen;
	SpriteBatch batch;
	float time = 0f;
	
	
	public SplashScreen(Taut game)
	{
		this.game = game;
	}
	
	@Override
	public void show()
	{
		batch = new SpriteBatch();
		splashScreen = new Texture(Gdx.files.internal("splash-screen.png"));
	}
	
	
	@Override
	public void render(float delta) {
		time += delta;
		if(time > 3.0f)
		{
			game.setScreen(new MainMenu(game));
		}
		
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		batch.begin();
		batch.draw(splashScreen, 0f, 0f, width, height);
		batch.end();
		
	}
	
	
	@Override
	public void dispose()
	{
		super.dispose();
		batch.dispose();
		splashScreen.dispose();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		game.setScreen(new MainMenu(game));
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		game.setScreen(new MainMenu(game));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
