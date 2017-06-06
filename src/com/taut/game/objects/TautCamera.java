package com.taut.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;

/**
 * @author porgull
 * Convenience class for
 * simplifying the use of
 * OrthographicCameras
 */

public class TautCamera extends OrthographicCamera {
	public TautCamera(int tileSize)
	{
		super();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		zoom = tileSize / width;

		setToOrtho(false, width, height);
		viewportHeight = height;
		viewportWidth = width;
		
		update();
	}
	public TautCamera()
	{
		super();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		setToOrtho(false, width, height);
		viewportHeight = height;
		viewportWidth = width;
		
		update();
	}
	
	public void setCameraPositionFromPlayer(Player player, TautSprite sprite, TiledMap map)
	{
		position.set(getCameraPositionFromPlayer(player, sprite , map));
	}
	
	private Vector3 getCameraPositionFromPlayer(Player player, TautSprite sprite, TiledMap map)
	{
		Vector3 playerPosition = player.getPlayerWorldPosition();

		float spriteWidth = convertPixelLengthToWorld(sprite.getWidth())*sprite.getScaleX();
		float spriteHeight = convertPixelLengthToWorld(sprite.getHeight())*sprite.getScaleY();
				
		Vector3 coords = new Vector3(playerPosition.x + (spriteWidth/2f), 
				playerPosition.y + (spriteHeight/2f), 0f);
		
		putCoordsInMapBounds(coords, map);

		return coords;
	}
	
	private void putCoordsInMapBounds(Vector3 coords, TiledMap map)
	{
		
		int mapWidth = map.getProperties().get("width", Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		float halfViewHeightInWorld = convertPixelLengthToWorld(viewportHeight / 2f);
		float halfViewWidthInWorld = convertPixelLengthToWorld(viewportWidth / 2f);
		
		//make sure camera is within map bounds
		coords.x = Math.max(coords.x, halfViewWidthInWorld);
		coords.x = Math.min(coords.x, mapWidth - halfViewWidthInWorld);
		coords.y = Math.max(coords.y, halfViewHeightInWorld);
		coords.y = Math.min(coords.y, mapHeight - halfViewHeightInWorld); 
	}
	
	public float convertPixelLengthToWorld(float pixels)
	{
		Vector3 start = new Vector3(0f, 0f, 0f); // in pixels
		Vector3 end = new Vector3(pixels, 0f, 0f);
		start = unproject(start); // in world
		end = unproject(end);
		return Math.abs(end.x - start.x);
	}
	public float convertPixelLengthToWorld(int pixels)
	{
		return convertPixelLengthToWorld(pixels);
	}
	public float convertWorldLengthToPixel(float world)
	{
		Vector3 start = new Vector3(0f, 0f, 0f); // in world
		Vector3 end = new Vector3(world, 0f, 0f);
		start = project(start); // in pixels
		end = project(end);
		return Math.abs(end.x - start.x);
	}
}
