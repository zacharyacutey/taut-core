package com.taut.game.objects;

import com.badlogic.gdx.math.Vector3;
import com.taut.game.GlobalData;

public class SpriteMovement {

	Vector3 location;
	Vector3 lastTile;
	Vector3 spriteDimensions;
	
	public SpriteMovement(Vector3 startLocation, Vector3 spriteDimensions)
	{
		location = startLocation;
		directionData = new DirectionData();
		lastTile = new Vector3(Math.round(startLocation.x), Math.round(startLocation.y), 0f);
		this.spriteDimensions = spriteDimensions;
	}
	
	public static class DirectionData
	{
		public Direction x = Direction.NONE;
		public int xTiles = 0;
		public Direction y = Direction.NONE;
		public int yTiles = 0;
		public Vector3 pastTranslation;
		
		public Vector3 goalTile = new Vector3();
	}
	
	private DirectionData directionData;
	
	public static enum Direction {NEGATIVE, NONE, POSITIVE}
	
	
	public void update(TautCamera camera, float delta)
	{
		Vector3 worldTranslation = getWorldTranslation(delta);
		
		updateLastTile(camera, worldTranslation);
				
		location.add(worldTranslation);
		
		directionData.pastTranslation = worldTranslation;
	}
	
	public void setLocation(Vector3 location)
	{
		this.location = location;
		directionData.pastTranslation = null;
		directionData.xTiles = 0;
		directionData.yTiles = 0;
	}
	
	public Direction getX()
	{
		return directionData.x;
	}

	public Direction getY()
	{
		return directionData.y;
	}
	
	public Vector3 getSpriteCoords(TautCamera camera)
	{
		return camera.project(location);
	}
	
	public Vector3 getSpriteWorldCoords()
	{
		return location;
	}
	
	private void updateLastTile(TautCamera camera, Vector3 translation)
	{
		Vector3 newLocation = new Vector3(location.x + translation.x, 
				location.y + translation.y, 0f);
		
		
		if(newLocation.x%1.0f == 0.0f)
		{
			lastTile.x = newLocation.x;
		}
		if(newLocation.y%1.0f == 0.0f)
		{
			lastTile.y = newLocation.y;
		}
		if(directionData.x == Direction.NEGATIVE)
		{
			float spriteWidth = spriteDimensions.x;
			// add sprite as if going negative, 
			float currXWithSprite = location.x + spriteWidth;
			float newXWithSprite = newLocation.x + spriteWidth;
			
			// if a full tile change in sprite location happens w/o hitting zero, set lastTile anyway
			if(Math.floor(newXWithSprite) != Math.floor(currXWithSprite))
			{
				lastTile.x = (float) Math.floor(newXWithSprite);
			}
		}else if(directionData.x == Direction.POSITIVE)
		{
			if(Math.floor(newLocation.x) != Math.floor(location.x))
			{
				lastTile.x = (float)Math.floor(newLocation.x);
			}
		}
		if(directionData.y == Direction.NEGATIVE)
		{
			float spriteHeight = spriteDimensions.y;
			// add sprite as if going negative, 
			float currYWithSprite = location.y + spriteHeight;
			float newYWithSprite = newLocation.y + spriteHeight;
			
			// if a full tile change in sprite location happens w/o hitting zero, set lastTile anyway
			if(Math.floor(newYWithSprite) != Math.floor(currYWithSprite))
			{
				lastTile.y = (float) Math.floor(newYWithSprite);
			}
		}else if(directionData.y == Direction.POSITIVE)
		{
			if(Math.floor(newLocation.y) != Math.floor(location.y))
			{
				lastTile.y = (float)Math.floor(newLocation.y);
			}
		}
	}
	
	public Vector3 getPastTranslation()
	{
		return directionData.pastTranslation;
	}
	
	public void setX(Direction direction, int tiles)
	{
		directionData.x = direction;
		directionData.xTiles = tiles;
		if(direction == Direction.NONE)
		{
			directionData.xTiles = 0;
		}
	}
	public void setY(Direction direction, int tiles)
	{
		directionData.y = direction;
		directionData.yTiles = tiles;
		if(direction == Direction.NONE)
		{
			directionData.yTiles = 0;
		}
	}
	public void setX(Direction direction)
	{
		directionData.x = direction;
		directionData.xTiles = 0;
	}
	public void setY(Direction direction)
	{
		directionData.y = direction;
		directionData.yTiles = 0;
	}
	
	public void stopMovement()
	{
		directionData.x = Direction.NONE;
		directionData.xTiles = 0;
		directionData.y = Direction.NONE;
		directionData.yTiles = 0;
	}
	
	private Vector3 getWorldTranslation(float delta)
	{
		Vector3 translation = new Vector3(0f,0f,0f);
		if(directionData.x == Direction.POSITIVE)
		{
			float movement = getMovementMagnitude(delta);
			
			if(directionData.xTiles != 0 && translation.y + movement > directionData.goalTile.y)
			{
				movement = directionData.goalTile.y - location.y;
				stopX();
			}
			
			translation.x = movement;
			
		}else if(directionData.x == Direction.NEGATIVE)
		{
			float movement = -getMovementMagnitude(delta);
			
			if(directionData.xTiles != 0 && translation.x + movement < directionData.goalTile.x)
			{
				movement = location.x - directionData.goalTile.x;
				stopX();
			}
			
			translation.x = -getMovementMagnitude(delta);
		}else if(directionData.x == Direction.NONE)
		{
			if(directionData.pastTranslation != null && location.x%1.0 != 0.0f)// if not currently at tile, but not set to move 
			{ 
				if(directionData.pastTranslation.x > 0.0f) // end on next positive tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.ceil(location.x);
										
					if(location.x + magnitude > nextTile) // if magnitude would overshoot next tile
					{
						magnitude = nextTile - location.x; // set magnitude to not overshoot
					}
					
					translation.x = magnitude;
				}else if(directionData.pastTranslation.x < 0.0f) // end on next negative tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.floor(location.x);
					
					if(location.x - magnitude < nextTile) // if magnitude would overshoot next tile
					{
						magnitude = location.x - nextTile; // set magnitude to not overshoot
					}
					
					translation.x = -magnitude;
				}
			}
		}
		if(directionData.y == Direction.POSITIVE)
		{
			float movement = getMovementMagnitude(delta);
			
			if(directionData.yTiles != 0 && translation.y + movement > directionData.goalTile.y)
			{
				movement = directionData.goalTile.y - location.y;
				stopY();
			}
			
			translation.y = movement;
		}else if(directionData.y == Direction.NEGATIVE)
		{
			float movement = -getMovementMagnitude(delta);
			
			if(directionData.yTiles != 0 && translation.y + movement < directionData.goalTile.y)
			{
				movement = location.y - directionData.goalTile.y;
				stopY();
			}
			
			translation.y = movement;
		}else if(directionData.y == Direction.NONE)
		{
			if(directionData.pastTranslation != null && location.y%1.0 != 0.0f)// if not currently at tile, but not set to move 
			{ 
				if(directionData.pastTranslation.y > 0.0f) // end on next positive tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.ceil(location.y);
					
					if(location.y + magnitude > nextTile) // if magnitude would overshoot next tile
					{
						magnitude = nextTile - location.y; // set magnitude to not overshoot
					}
					
					translation.y = magnitude;
				}else if(directionData.pastTranslation.y < 0.0f) // end on next negative tile
				{
					float magnitude = getMovementMagnitude(delta);
					float nextTile = (float)Math.floor(location.y);
					
					if(location.y - magnitude < nextTile) // if magnitude would overshoot next tile
					{
						magnitude = location.y - nextTile; // set magnitude to not overshoot
					}
					
					translation.y = -magnitude;
				}
			}
		}
		
		return translation;
	}
	
	public void stopX()
	{
		setX(Direction.NONE);
	}
	
	public void stopY()
	{
		setY(Direction.NONE);
	}
	
	private float getMovementMagnitude(float delta)
	{
		return GlobalData.getSpriteMovementSpeed() * delta; // linearly scale tiles per second for speed
	}
	
}
