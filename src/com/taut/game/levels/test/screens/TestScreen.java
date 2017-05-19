package com.taut.game.levels.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.taut.game.Taut;
import com.taut.game.levels.test.TestData;

public class TestScreen extends ScreenAdapter implements InputProcessor {
	
	Taut game;
	OrthographicCamera camera;
	TiledMap map;
	IsometricTiledMapRenderer renderer;
	
	private static class Inputs
	{
		public static class Key
		{
			protected Key(){}
			public boolean isPressed = false;
			public double timeSincePressed = 0.0;
		}
		public static Key left = new Key();
		public static Key right = new Key();
		public static Key up = new Key();
		public static Key down = new Key();
		public static Key pageUp = new Key();
		public static Key pageDown = new Key();
	}
	
	public TestScreen(final Taut game)
	{
		super();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		this.game = game;
		this.map = TestData.getMainMap();
		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.position.set(width / 2f, height / 2f, 0);
		camera.update();
		this.renderer = new IsometricTiledMapRenderer(map);
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void render(float delta)
	{
		handleInput(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.setView(camera);
		renderer.render();
	}
	
	private void handleInput(float delta)
	{
		Vector2 translation = new Vector2();
		float changeZoom = 0.0f;
		if(Inputs.left.isPressed)
		{
			translation.x -= movementMagnitude(Inputs.left.timeSincePressed);
			Inputs.left.timeSincePressed += delta;
		}
		if(Inputs.right.isPressed)
		{
			translation.x += movementMagnitude(Inputs.right.timeSincePressed);
			Inputs.right.timeSincePressed += delta;
		}
		if(Inputs.up.isPressed)
		{
			translation.y += movementMagnitude(Inputs.up.timeSincePressed);
			Inputs.up.timeSincePressed += delta;
		}
		if(Inputs.down.isPressed)
		{
			translation.y -= movementMagnitude(Inputs.down.timeSincePressed);
			Inputs.down.timeSincePressed += delta;
		}
		if(Inputs.pageDown.isPressed)
		{
			changeZoom += zoomMagnitude(Inputs.pageDown.timeSincePressed);
			Inputs.pageDown.timeSincePressed += delta;
		}
		if(Inputs.pageUp.isPressed)
		{
			changeZoom -= zoomMagnitude(Inputs.pageUp.timeSincePressed);
			Inputs.pageUp.timeSincePressed += delta;
		}
		
		if(Inputs.left.isPressed && Inputs.right.isPressed)
		{
			translation.x = 0.0f;
			Inputs.left.timeSincePressed = 0.0;
			Inputs.right.timeSincePressed = 0.0;
		}
		if(Inputs.up.isPressed && Inputs.down.isPressed)
		{
			translation.y = 0.0f;
			Inputs.up.timeSincePressed = 0.0;
			Inputs.down.timeSincePressed = 0.0;
		}
		if(Inputs.pageUp.isPressed && Inputs.pageDown.isPressed)
		{
			changeZoom = 0.0f;
			Inputs.pageUp.timeSincePressed = 0.0;
			Inputs.pageDown.timeSincePressed = 0.0;
		}
		
		camera.translate(translation);
		camera.zoom += changeZoom;
	}
	
	private float zoomMagnitude(double time)
	{
		float maxMagnitude = .03f;
		float timeToEven = 1.0f;
		if(time >= timeToEven)
		{
			return maxMagnitude;
		}else
		{
			float output = ((float)Math.pow(time, 1.1) / timeToEven) * maxMagnitude;
			return output;
		}
	}
	
	private float movementMagnitude(double time)
	{
		float maxMagnitude = 20.0f * camera.zoom;
		float timeToEven = 1.0f;
		if(time >= timeToEven)
		{
			return maxMagnitude;
		}else
		{
			float output = ((float)Math.pow(time, 1.1) / timeToEven) * maxMagnitude;
			return output;
		}
	}
	
	@Override
	public boolean keyUp(int keycode) {
		
		if(keycode == Input.Keys.LEFT)
		{
			Inputs.left.isPressed = false;
			Inputs.left.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.RIGHT)
		{
			Inputs.right.isPressed = false;
			Inputs.right.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.UP)
		{
			Inputs.up.isPressed = false;
			Inputs.up.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.DOWN)
		{
			Inputs.down.isPressed = false;
			Inputs.down.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.PAGE_UP)
		{
			Inputs.pageUp.isPressed = false;
			Inputs.pageUp.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.PAGE_DOWN)
		{
			Inputs.pageDown.isPressed = false;
			Inputs.pageDown.timeSincePressed = 0.0;
		}
		
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == Input.Keys.LEFT)
		{
			Inputs.left.isPressed = true;
		}else if(keycode == Input.Keys.RIGHT)
		{
			Inputs.right.isPressed = true;
		}else if(keycode == Input.Keys.UP)
		{
			Inputs.up.isPressed = true;
		}else if(keycode == Input.Keys.DOWN)
		{
			Inputs.down.isPressed = true;
		}else if(keycode == Input.Keys.PAGE_UP)
		{
			Inputs.pageUp.isPressed = true;
		}else if(keycode == Input.Keys.PAGE_DOWN)
		{
			Inputs.pageDown.isPressed = true;
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
