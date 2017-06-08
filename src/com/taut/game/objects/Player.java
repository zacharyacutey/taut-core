package com.taut.game.objects;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.GlobalData;
import com.taut.game.objects.SpriteMovement.Direction;
import com.taut.game.objects.SpriteMovement.DirectionData;

public class Player implements InputProcessor {
	
	public final float TILES_PER_SECOND = 3.0f;
	public TautAnimatedSprite playerSprite;
	private boolean isUsingInput;
	private static Player instance;
	public SpriteMovement movement;
	
	
	private Player()
	{
		playerSprite  = GlobalData.getPlayerWalkAnimation();
		playerSprite.isUsingSpriteMovementInstance(false);
		isUsingInput = true;
		movement = new SpriteMovement(new Vector3(0f,0f,0f), new Vector3(1f, 1f, 1f));
	}
	
	public static Player getPlayer()
	{
		if(instance == null)
			instance = new Player();
		return instance;
	}
	
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
	
	
	
	public void isUsingInput(boolean isUsingInput)
	{
		this.isUsingInput = isUsingInput;
	}
	
	public boolean isUsingInput()
	{
		return isUsingInput;
	}
	
	public boolean isPlayerInMapBounds(TiledMap map)
	{
		int mapWidth = map.getProperties().get("width",Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		
		Vector3 playerPosition = movement.getSpriteWorldCoords();
		
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

		Vector3 playerPosition = movement.getSpriteWorldCoords();

		if(playerPosition.x < 0.0f)
			playerPosition.x = 0.0f;
		if(playerPosition.x > ((float)mapWidth)-1f)
			playerPosition.x = ((float)mapWidth)-1f;
		if(playerPosition.y < 0.0f)
			playerPosition.y = 0.0f;
		if(playerPosition.y > ((float)mapHeight)-1f)
			playerPosition.y = ((float)mapHeight)-1f;
	}
	
	
	public void update(float delta, TiledMap map, TautCamera camera)
	{
		if(!isUsingInput)
			return;
		
		DirectionData direction = new DirectionData();
		if(Inputs.left.isPressed)
		{
			playerSprite.setSpriteBackward();
			direction.x = Direction.NEGATIVE;
			Inputs.left.timeSincePressed += delta;
		}
		if(Inputs.right.isPressed)
		{
			playerSprite.setSpriteForward();
			direction.x = Direction.POSITIVE;
			Inputs.right.timeSincePressed += delta;
		}
		if(Inputs.up.isPressed)
		{
			Inputs.up.timeSincePressed += delta;
			direction.y = Direction.POSITIVE;
		}
		if(Inputs.down.isPressed)
		{
			Inputs.down.timeSincePressed += delta;
			direction.y = Direction.NEGATIVE;
		}
		
		if(Inputs.left.isPressed && Inputs.right.isPressed)
		{
			playerSprite.setSpriteForward();
			direction.x = Direction.NONE;
			Inputs.left.timeSincePressed = 0.0;
			Inputs.right.timeSincePressed = 0.0;
		}
		if(Inputs.up.isPressed && Inputs.down.isPressed)
		{
			direction.y = Direction.NONE;
			Inputs.up.timeSincePressed = 0.0;
			Inputs.down.timeSincePressed = 0.0;
		}
		
		
		movement.setX(direction.x);
		movement.setY(direction.y);
		
		movement.update(camera, delta);
		
		playerSprite.update(delta, camera, movement);
		
		setPlayerInBounds(map);

	}
	
	public Vector3 getPlayerWorldPosition()
	{
		return movement.getSpriteWorldCoords();
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
