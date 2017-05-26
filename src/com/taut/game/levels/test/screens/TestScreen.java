package com.taut.game.levels.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.Taut;
import com.taut.game.TautData;
import com.taut.game.levels.test.TestData;
import com.taut.game.objects.TautAnimatedSprite;
import com.taut.game.objects.TautCamera;
import com.taut.game.objects.TautOrthogonalTiledMapRenderer;
import com.taut.game.objects.TautSprite;

public class TestScreen extends ScreenAdapter implements InputProcessor {
	Taut game;
	TautCamera camera;
	TiledMap map;
	TautOrthogonalTiledMapRenderer mapRenderer;
	TautAnimatedSprite animatedSprite;
	SpriteBatch spriteBatch;
	Vector3 playerPosition;
	float walkAnimationTime;
	SpriteDirection spriteDirection;
	ShapeRenderer shapeRenderer;
	
	enum SpriteDirection {FORWARD,BACKWARD}
	
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
	
	private static class PlayerMovement
	{
		public MovementDirection xMovement = MovementDirection.NONE;
		public MovementDirection yMovement = MovementDirection.NONE;
		public static Vector3 pastTranslation;
	}
	static enum MovementDirection {NEGATIVE, NONE, POSITIVE}

	
	public TestScreen(final Taut game)
	{
		super();
		this.game = game;
		map = TestData.getMainMap();
		animatedSprite = TautData.getWalkAnimation();
		camera = new TautCamera(16);
		mapRenderer = new TautOrthogonalTiledMapRenderer(map, 1f/16f);
		spriteBatch = new SpriteBatch();
		playerPosition = new Vector3(0f,0f,0f);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void render(float delta)
	{
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       
        
		handleInput(delta);

		int mapWidth = map.getProperties().get("width", Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		
		if(playerPosition.x < 0.0f)
			playerPosition.x = 0.0f;
		if(playerPosition.x > ((float)mapWidth)-1f)
			playerPosition.x = ((float)mapWidth)-1f;
		if(playerPosition.y < 0.0f)
			playerPosition.y = 0.0f;
		if(playerPosition.y > ((float)mapHeight)-1f)
			playerPosition.y = ((float)mapHeight)-1f;
		
		TautSprite currentSprite = animatedSprite.getSpriteKeyFrame(walkAnimationTime, true);
		currentSprite.setScaleInTiles(camera, 1f, 1f);

		camera.setCameraPositionFromPlayer(currentSprite, playerPosition, mapWidth, mapHeight);
		
		camera.update();
		Vector3 projectedPlayerPosition = camera.project(new Vector3(playerPosition)); // convert from world units to camera units
		
		
		currentSprite.setX(projectedPlayerPosition.x);
		currentSprite.setY(projectedPlayerPosition.y);
		if(spriteDirection == SpriteDirection.BACKWARD) 
			currentSprite.flip(true, false);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		
		spriteBatch.begin();
		currentSprite.draw(spriteBatch);
		spriteBatch.end();
		
		Rectangle boundingSpriteRect = currentSprite.getBoundingRectangle();
		
		shapeRenderer.begin();
		shapeRenderer.rect(boundingSpriteRect.x, boundingSpriteRect.y, boundingSpriteRect.width, boundingSpriteRect.height);
		for(int x = 0; x < mapWidth; x++)
		{
			Vector3 lineStart = camera.project(new Vector3(x, 0f, 0f));
			Vector3 lineEnd = camera.project(new Vector3(x, mapHeight, 0f));
			shapeRenderer.line(lineStart, lineEnd);
		}
		for(int y = 0; y < mapHeight; y++)
		{
			Vector3 lineStart = camera.project(new Vector3(0f, y, 0f));
			Vector3 lineEnd = camera.project(new Vector3(mapWidth, y, 0f));
			shapeRenderer.line(lineStart, lineEnd);
		}
		shapeRenderer.end();
		
		
		System.out.println("camera width: " + camera.viewportWidth + " camera height: " + camera.viewportHeight);
		System.out.println("screen width: " + Gdx.graphics.getWidth() + " screen height: " + Gdx.graphics.getHeight());
	}
	
	
	private void handleInput(float delta)
	{
		PlayerMovement playerMovement = new PlayerMovement();
		if(Inputs.left.isPressed)
		{
			spriteDirection = SpriteDirection.BACKWARD;
			playerMovement.xMovement = MovementDirection.NEGATIVE;
			Inputs.left.timeSincePressed += delta;
		}
		if(Inputs.right.isPressed)
		{
			spriteDirection = SpriteDirection.FORWARD;
			playerMovement.xMovement = MovementDirection.POSITIVE;
			Inputs.right.timeSincePressed += delta;
		}
		if(Inputs.up.isPressed)
		{
			Inputs.up.timeSincePressed += delta;
			playerMovement.yMovement = MovementDirection.POSITIVE;
		}
		if(Inputs.down.isPressed)
		{
			Inputs.down.timeSincePressed += delta;
			playerMovement.yMovement = MovementDirection.NEGATIVE;
		}
		
		if(Inputs.left.isPressed && Inputs.right.isPressed)
		{
			spriteDirection = SpriteDirection.FORWARD;
			playerMovement.xMovement = MovementDirection.NONE;
			Inputs.left.timeSincePressed = 0.0;
			Inputs.right.timeSincePressed = 0.0;
		}
		if(Inputs.up.isPressed && Inputs.down.isPressed)
		{
			playerMovement.yMovement = MovementDirection.NONE;
			Inputs.up.timeSincePressed = 0.0;
			Inputs.down.timeSincePressed = 0.0;
		}
		
		
		// based upon input, get player translation
		Vector3 translation = getPlayerTranslation(playerMovement, delta);
		
		if(translation.x != 0.0f || translation.y != 0.0f) // if sprite is moving, animate him
		{
			walkAnimationTime += delta;
		}
		PlayerMovement.pastTranslation = translation; // set pastTranslation for next run
		playerPosition.add(translation);
	}
	
	private Vector3 getPlayerTranslation(PlayerMovement playerMovement, float delta)
	{
		Vector3 translation = new Vector3(0f,0f,0f);
		if(playerMovement.xMovement == MovementDirection.POSITIVE)
		{
			translation.x = getMovementMagnitude(delta);
		}else if(playerMovement.xMovement == MovementDirection.NEGATIVE)
		{
			translation.x = -getMovementMagnitude(delta);
		}else if(playerMovement.xMovement == MovementDirection.NONE)
		{
			if(PlayerMovement.pastTranslation != null && playerPosition.x%1.0 != 0.0f)// if not currently at tile, but not set to move 
			{ 
				if(PlayerMovement.pastTranslation.x > 0.0f) // end on next positive tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.ceil(playerPosition.x);
					
					if(playerPosition.x + magnitude > nextTile) // if magnitude would overshoot next tile
					{
						magnitude = nextTile - playerPosition.x; // set magnitude to not overshoot
					}
					
					translation.x = magnitude;
				}else if(PlayerMovement.pastTranslation.x < 0.0f) // end on next negative tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.floor(playerPosition.x);
					
					if(playerPosition.x - magnitude < nextTile) // if magnitude would overshoot next tile
					{
						magnitude = playerPosition.x - nextTile; // set magnitude to not overshoot
					}
					
					translation.x = -magnitude;
				}
			}
		}
		
		
		
		if(playerMovement.yMovement == MovementDirection.POSITIVE)
		{
			translation.y = getMovementMagnitude(delta);
		}else if(playerMovement.yMovement == MovementDirection.NEGATIVE)
		{
			translation.y = -getMovementMagnitude(delta);
		}else if(playerMovement.yMovement == MovementDirection.NONE)
		{
			if(PlayerMovement.pastTranslation != null && playerPosition.y%1.0 != 0.0f)// if not currently at tile, but not set to move 
			{ 
				if(PlayerMovement.pastTranslation.y > 0.0f) // end on next positive tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.ceil(playerPosition.y);
					
					if(playerPosition.y + magnitude > nextTile) // if magnitude would overshoot next tile
					{
						magnitude = nextTile - playerPosition.y; // set magnitude to not overshoot
					}
					
					translation.y = magnitude;
				}else if(PlayerMovement.pastTranslation.y < 0.0f) // end on next negative tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.floor(playerPosition.y);
					
					if(playerPosition.y - magnitude < nextTile) // if magnitude would overshoot next tile
					{
						magnitude = playerPosition.y - nextTile; // set magnitude to not overshoot
					}
					
					translation.y = -magnitude;
				}
			}
		}
		return translation;
	}

	private float getMovementMagnitude(float delta)
	{
		return 3.0f * delta; // two tiles per second, linear
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		camera.viewportHeight = height;
		camera.viewportWidth = width;
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
