package com.taut.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class TautCamera extends OrthographicCamera {
	public TautCamera(int tileSize)
	{
		super();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		zoom = (float)tileSize / width;

		setToOrtho(false, width, height);
		viewportHeight = height;
		viewportWidth = width;
		
		update();
	}
	
	public void setCameraPositionFromPlayer(Sprite player, Vector3 playerPosition, int mapWidth, int mapHeight)
	{
		position.set(getCameraPositionFromPlayer(player, playerPosition, mapWidth, mapHeight));
	}
	
	private Vector3 getCameraPositionFromPlayer(Sprite player, Vector3 playerPosition, int mapWidth, int mapHeight)
	{
		float halfSpriteWidthInWorld = (convertPixelLengthToWorld(player.getWidth())*player.getScaleX())/2f;
		float halfSpriteHeightInWorld = (convertPixelLengthToWorld(player.getHeight())*player.getScaleY())/2f;
		
		
		float camX = playerPosition.x + halfSpriteWidthInWorld; 
		float camY = playerPosition.y + halfSpriteHeightInWorld; 
		
		
		float halfViewHeightInWorld = convertPixelLengthToWorld(viewportHeight / 2f);
		float halfViewWidthInWorld = convertPixelLengthToWorld(viewportWidth / 2f);
		
		
		//make sure camera is within map bounds
		camX = Math.max(camX, halfViewWidthInWorld);
		camX = Math.min(camX, mapWidth - halfViewWidthInWorld);
		camY = Math.max(camY, halfViewHeightInWorld);
		camY = Math.min(camY, mapHeight - halfViewHeightInWorld); 
		
		
		System.out.println("Player pos x " + playerPosition.x + "\tPlayer pos y " + playerPosition.y);
		System.out.println("Camera pos x " + camX + "\tCamera pos y " + camY);


		return new Vector3(camX, camY, 0f);
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
