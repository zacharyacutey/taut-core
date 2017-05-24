package com.taut.game.levels.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.Taut;
import com.taut.game.TautData;
import com.taut.game.levels.test.TestData;
import com.taut.game.models.TautCamera;

public class TestScreen extends ScreenAdapter implements InputProcessor {
	Taut game;
	TautCamera camera;
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	Animation<TextureRegion> walkAnimation;
	SpriteBatch batch;
	Vector3 playerPosition;
	float walkAnimationTime;
	Direction direction;
	ShapeRenderer shapeRenderer;
	
	enum Direction{FORWARD,BACKWARD}
	
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
		this.game = game;
		map = TestData.getMainMap();
		walkAnimation = TautData.getWalkAnimation();
		camera = new TautCamera(16);
		renderer = new OrthogonalTiledMapRenderer(map, 1f/16f);
		batch = new SpriteBatch();
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
		
		camera.setCameraPositionFromPlayer(playerPosition, mapWidth, mapHeight);
		
		camera.update();
		Vector3 projectedPlayerPosition = 
				camera.project(new Vector3(playerPosition)); // convert from world units to camera units
		

		Sprite currentSprite= new Sprite(walkAnimation.getKeyFrame(walkAnimationTime, true));
		
		Vector3 spriteBottomLeftCorner = new Vector3(projectedPlayerPosition.x, projectedPlayerPosition.y, 0f);
		Vector3 spriteTopRightCorner = new Vector3(projectedPlayerPosition.x + currentSprite.getRegionWidth(), 
				projectedPlayerPosition.y + currentSprite.getRegionHeight(), 0f);
		
		
		Vector3 spriteDimensions = new Vector3(camera.convertPixelLengthToWorld(spriteTopRightCorner.x - spriteBottomLeftCorner.x),
				camera.convertPixelLengthToWorld(spriteTopRightCorner.y - spriteBottomLeftCorner.y), 0f);
				
		currentSprite.setScale(1f / spriteDimensions.x, 1f / spriteDimensions.y);
		
		currentSprite.setX(projectedPlayerPosition.x);
		currentSprite.setY(projectedPlayerPosition.y);
		currentSprite.setOrigin(projectedPlayerPosition.x, projectedPlayerPosition.y);
		if(direction == Direction.BACKWARD)
			currentSprite.flip(true, false);
		renderer.setView(camera);
		renderer.render();
		
		
		batch.begin();
		currentSprite.draw(batch);
		batch.end();
		
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
