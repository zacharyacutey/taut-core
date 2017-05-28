package com.taut.game.objects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.TautData;

public class Player implements InputProcessor {
	
	public final float TILES_PER_SECOND = 3.0f;
	private TautAnimatedSprite walkAnimation;
	private Vector3 playerPosition;
	private SpriteDirection spriteDirection;
	private float walkAnimationTime;

	
	public Player()
	{
		walkAnimation = TautData.getWalkAnimation();
		playerPosition = new Vector3(0f,0f,0f);
	}
	
	enum SpriteDirection {FORWARD,BACKWARD}
	
	private static class Inputs
	{
		public static class Key
		{
			protected Key(){}
			public boolean isPressed = false;
			@SuppressWarnings("unused")
			public double timeSincePressed = 0.0; // unused; may be used in future for accel/decel
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
	
	
	
	public boolean isPlayerInMapBounds(TiledMap map)
	{
		int mapWidth = map.getProperties().get("width",Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		
		if(playerPosition.x < 0.0f)
			return false;
		if(playerPosition.x > ((float)mapWidth)-1f)
			return false;
		if(playerPosition.y < 0.0f)
			return false;
		if(playerPosition.y > ((float)mapHeight)-1f)
			return false;
		
		return true;
	}
	
	public void setPlayerInBounds(TiledMap map)
	{
		int mapWidth = map.getProperties().get("width",Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		
		if(playerPosition.x < 0.0f)
			playerPosition.x = 0.0f;
		if(playerPosition.x > ((float)mapWidth)-1f)
			playerPosition.x = ((float)mapWidth)-1f;
		if(playerPosition.y < 0.0f)
			playerPosition.y = 0.0f;
		if(playerPosition.y > ((float)mapHeight)-1f)
			playerPosition.y = ((float)mapHeight)-1f;
	}
	
	
	public void update(float delta, TiledMap map)
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
			addWalkAnimationTime(delta);
		}
		PlayerMovement.pastTranslation = translation; // set pastTranslation for next run
		playerPosition.add(translation);
		
		if(!isPlayerInMapBounds(map))
			setPlayerInBounds(map);
	}
	
	public Vector3 getPlayerWorldPosition()
	{
		return playerPosition;
	}
	
	private void addWalkAnimationTime(float delta)
	{
		walkAnimationTime += delta;
		walkAnimationTime %= walkAnimation.getAnimationDuration(); // to prevent overflow with enough time
	}
	
	public TautSprite getSprite()
	{
		TautSprite sprite = walkAnimation.getSpriteKeyFrame(walkAnimationTime, true);
				
		if(spriteDirection == SpriteDirection.BACKWARD)
			sprite.flip(true, false);
		
		return sprite;
	}
	
	public TautSprite getScaledSprite(TautCamera camera, int x, int y)
	{
		TautSprite sprite = getSprite();
		
		sprite.setScaleInTiles(camera, x, y);
		
		return sprite;
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
		return TILES_PER_SECOND * delta; // linearly scale tiles per second for speed
	}
	
	
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

	
	public boolean keyTyped(char character) {
		return false;
	}

	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}
	
}
