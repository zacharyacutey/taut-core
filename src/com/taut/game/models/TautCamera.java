package com.taut.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class TautCamera extends OrthographicCamera {
	public TautCamera(int tileSize)
	{
		super();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		zoom = (float)tileSize / width;
		setToOrtho(false, width, height);
		
		
		//position.set(getViewWidth()/2f, getViewHeight()/2f, 0f);
		update();
	}
	
	public void setCameraPositionFromPlayer(Vector3 playerPosition, int mapWidth, int mapHeight)
	{
		position.set(getCameraPositionFromPlayer(playerPosition, mapWidth, mapHeight));
	}
	
	private Vector3 getCameraPositionFromPlayer(Vector3 playerPosition, int mapWidth, int mapHeight)
	{
		
		float camX = playerPosition.x + convertPixelLengthToWorld(viewportWidth)/2f; // this follows w/ sprite at bottom left
		float camY = playerPosition.y + convertPixelLengthToWorld(viewportHeight)/2f; 
		//System.out.println("test " + convertPixelLengthToWorld(viewportWidth)/2f);
		/*float halfViewHeight = getViewHeight()/ 2f;
		float halfViewWidth = getViewWidth() /2f;
		
		camX = Math.max(camX, halfViewWidth);
		camX = Math.min(camX, mapWidth - halfViewWidth);
		camY = Math.max(camY, halfViewHeight);
		camY = Math.min(camY, mapHeight - halfViewHeight);*/
		
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
