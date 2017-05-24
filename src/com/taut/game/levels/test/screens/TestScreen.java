package com.taut.game.levels.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.taut.game.Taut;
import com.taut.game.TautData;
import com.taut.game.levels.test.TestData;

public class TestScreen extends ScreenAdapter implements InputProcessor {
	// TODO: Clean up code
	// TODO: Convert this into an extendable base class for all screens
	// TODO: Convert classes in here into extended versions (i.e., TautSprite, TautCamera, etc) 
		// cleaner code (less boilerplate) and more flexible
	Taut game;
	OrthographicCamera camera;
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	Animation<TextureRegion> walkAnimation;
	SpriteBatch batch;
	Vector3 playerPosition;
	float walkAnimationTime;
	Direction direction;
	
	/** 
	 * used for sprite direction
	 */
	enum Direction {FORWARD,BACKWARD}
	
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
	}
	
	public TestScreen(final Taut game)
	{
		super();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		this.game = game;
		map = TestData.getMainMap();
		walkAnimation = TautData.getWalkAnimation();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);		
		camera.zoom = .04f;
		camera.position.set((width / 2f) * camera.zoom, (height / 2f) * camera.zoom, 0f);
		camera.update();
		renderer = new OrthogonalTiledMapRenderer(map, 1f/16f);
		batch = new SpriteBatch();
		playerPosition = new Vector3(0f,0f,0f);
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void render(float delta)
	{
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
		handleInput(delta);
		
		Sprite currentSprite = new Sprite(walkAnimation.getKeyFrame(walkAnimationTime, true));

		Vector3 cameraPosition = getCameraPosition(playerPosition, currentSprite); // applies bounds to the player position before moving camera
		camera.position.set(cameraPosition);
		camera.update();
		
		Vector3 projectedPlayerPosition = 
				camera.project(new Vector3(playerPosition)); // convert from world units to camera units
		currentSprite.setScale((1f / currentSprite.getWidth())/camera.zoom, 
				((1f / currentSprite.getHeight())/camera.zoom) * (currentSprite.getHeight()/currentSprite.getWidth()));
		currentSprite.setX(projectedPlayerPosition.x);
		currentSprite.setY(projectedPlayerPosition.y);
		if(direction == Direction.BACKWARD) currentSprite.flip(true, false);
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		currentSprite.draw(batch);
		batch.end();
	}
	
	public Vector3 getCameraPosition(Vector3 playerPosition, Sprite playerSprite)
	{
		// set camera coords to center of sprite and account for the fact that sprites are drawn from bottom left and are scaled
		float camX = playerPosition.x + (playerSprite.getWidth() * camera.zoom / 2f); 
		float camY = playerPosition.y + (playerSprite.getHeight() * camera.zoom / 2f); 
		// TODO: make these actually the center of the sprite
		
		float halfHeight = (Gdx.graphics.getHeight() / 2f) * camera.zoom;
		float halfWidth = (Gdx.graphics.getWidth() / 2f) * camera.zoom;
		
		// add left/right/up/down bounds to camera motion 
		camX = Math.max(camX, halfWidth); // left
		camX = Math.min(camX, map.getProperties().get("width",Integer.class) - halfWidth); // right
		camY = Math.max(camY, halfHeight); // up
		camY = Math.min(camY, map.getProperties().get("height",Integer.class) - halfHeight); // down
		
		
		return new Vector3(camX, camY, 0f);
	}
	
	
	private void handleInput(float delta)
	{
		Vector3 translation = new Vector3();
		boolean addedDeltaToWalk = false;
		if(Inputs.left.isPressed)
		{
			direction = Direction.BACKWARD; 
			translation.x -= movementMagnitude(Inputs.left.timeSincePressed);
			Inputs.left.timeSincePressed += delta;
			if(addedDeltaToWalk == false)
			{
				walkAnimationTime += delta;
				addedDeltaToWalk = true;
			}
		}
		if(Inputs.right.isPressed)
		{
			direction = Direction.FORWARD;
			translation.x += movementMagnitude(Inputs.right.timeSincePressed);
			Inputs.right.timeSincePressed += delta;
			if(addedDeltaToWalk == false)
			{
				walkAnimationTime += delta;
				addedDeltaToWalk = true;
			}
		}
		if(Inputs.up.isPressed)
		{
			translation.y += movementMagnitude(Inputs.up.timeSincePressed);
			Inputs.up.timeSincePressed += delta;
			if(addedDeltaToWalk == false)
			{
				walkAnimationTime += delta;
				addedDeltaToWalk = true;
			}
		}
		if(Inputs.down.isPressed)
		{
			translation.y -= movementMagnitude(Inputs.down.timeSincePressed);
			Inputs.down.timeSincePressed += delta;
			if(addedDeltaToWalk == false)
			{
				walkAnimationTime += delta;
				addedDeltaToWalk = true;
			}
		}
		
		if(Inputs.left.isPressed && Inputs.right.isPressed)
		{
			direction = Direction.FORWARD; 
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
		
		if(translation.x == 0.0f && translation.y == 0.0f && addedDeltaToWalk)
		{
			walkAnimationTime -= delta;
		}
		
		playerPosition.add(translation);
		// TODO: Limit player position to where it doesn't 
		// let the sprite leave the map
		// (harder than it sounds, limiting by (0,0) and
		// map height/width doesn't work)
	}

	private float movementMagnitude(double time)
	{
		//TODO: Convert movement to being tile based opposed to free movement
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
		
		if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
		{
			Inputs.left.isPressed = false;
			Inputs.left.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
		{
			Inputs.right.isPressed = false;
			Inputs.right.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
		{
			Inputs.up.isPressed = false;
			Inputs.up.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
		{
			Inputs.down.isPressed = false;
			Inputs.down.timeSincePressed = 0.0;
		}
		
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
		{
			Inputs.left.isPressed = true;
		}else if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
		{
			Inputs.right.isPressed = true;
		}else if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
		{
			Inputs.up.isPressed = true;
		}else if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
		{
			Inputs.down.isPressed = true;
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