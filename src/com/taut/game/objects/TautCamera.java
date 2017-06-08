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

		Vector3 spriteDimensions = convertPixelLengthToWorld(new Vector3(
				sprite.getWidth() * sprite.getScaleX(), 
				sprite.getHeight() * sprite.getScaleY(), 0f));

		Vector3 coords = new Vector3(playerPosition.x + (spriteDimensions.x/2f), 
				playerPosition.y + (spriteDimensions.y/2f), 0f);
		
		putCoordsInMapBounds(coords, map);

		return coords;
	}
	
	private void putCoordsInMapBounds(Vector3 coords, TiledMap map)
	{
		
		int mapWidth = map.getProperties().get("width", Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		Vector3 halfViewport = convertPixelLengthToWorld(new Vector3(
				viewportWidth/2f,
				viewportHeight/2f, 0f));
		
		
		//make sure camera is within map bounds
		coords.x = Math.max(coords.x, halfViewport.x);
		coords.x = Math.min(coords.x, mapWidth - halfViewport.x);
		coords.y = Math.max(coords.y, halfViewport.y);
		coords.y = Math.min(coords.y, mapHeight - halfViewport.y); 
	}
	
	public Vector3 convertPixelLengthToWorld(Vector3 pixels)
	{
		Vector3 start = new Vector3(0f, 0f, 0f); // in pixels
		Vector3 end = pixels;
		start = unproject(start); // in world
		end = unproject(end);
		return new Vector3(Math.abs(end.x - start.x), Math.abs(end.y - start.y), 0f);
	}

	public Vector3 convertWorldLengthToPixel(Vector3 world)
	{
		Vector3 start = new Vector3(0f, 0f, 0f); // in world
		Vector3 end = world;
		start = project(start); // in pixels
		end = project(end);
		return new Vector3(Math.abs(end.x - start.x), Math.abs(end.y - start.y), 0f);
	}
	
	public float getCircumscribedRadius()
	{
		float halfWidth = viewportWidth/2f;
		float halfHeight = viewportHeight/2f;
		return (float)Math.sqrt(Math.pow(halfWidth, 2) + Math.pow(halfHeight, 2));
	}
	
	public float getWorldCircumscribedRadius()
	{

		float halfCamWidth = viewportWidth/2f;
		float halfCamHeight = viewportHeight/2f;
		Vector3 worldHalf = unproject(new Vector3(halfCamWidth, halfCamHeight, 0f));
		
		return (float)Math.sqrt(Math.pow(worldHalf.x, 2) + Math.pow(worldHalf.y, 2));
	}
	
	
}
