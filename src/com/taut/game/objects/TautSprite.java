package com.taut.game.objects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

/**
 * @author porgull
 * Convenience class for
 * easier use of the Sprite
 * class
 */

public class TautSprite extends Sprite {
	public TautAnimatedSprite walkAnimation;
	
	public TautSprite()
	{
		super();
	}
	public TautSprite(Texture texture)
	{
		super(texture);
	}
	public TautSprite(TextureRegion texture)
	{
		super(texture);
	}
	
	public TautSprite(Texture texture, TautAnimatedSprite walkAnimation)
	{
		super(texture);
		this.walkAnimation = walkAnimation;
	}
	
	public TautSprite getScaledSprite(TautCamera camera)
	{
		return getScaledSprite(camera, 1, 1);
	}
	
	public TautSprite getUnscaledSprite()
	{
		TautSprite sprite = walkAnimation.getSpriteKeyFrame(true);
		return sprite;
	}
	
	public TautSprite getScaledSprite(TautCamera camera, int x, int y)
	{
		TautSprite sprite = getUnscaledSprite();
		
		sprite.setScaleInTiles(camera, x, y);
		
		return sprite;
	}
	
	
	public void setScaleInTiles(TautCamera camera, float x, float y)
	{
		setOrigin(0f, 0f); // set origin of sprite to the bottom left corner
		Vector3 scaleFactors = getTileScalingFactor(camera);
		setScale( scaleFactors.x * x, scaleFactors.y * y);
	}
	public void setScaleInTiles(TautCamera camera, float xy)
	{
		setScaleInTiles(camera, xy, xy);
	}
	
	private Vector3 getTileScalingFactor(TautCamera camera)
	{
		Vector3 spriteBottomLeftCorner = new Vector3(0f, 0f, 0f);
		Vector3 spriteTopRightCorner = new Vector3(getRegionWidth(), 
				getRegionHeight(), 0f);
		Vector3 spriteDimensions = new Vector3(camera.convertPixelLengthToWorld(spriteTopRightCorner.x - spriteBottomLeftCorner.x),
				camera.convertPixelLengthToWorld(spriteTopRightCorner.y - spriteBottomLeftCorner.y), 0f);
		
		return new Vector3(1f / spriteDimensions.x, 1f / spriteDimensions.y, 0f);		
	}

	
	public static TextureRegion[] splitTexture(Texture texture, int width, int height)
	{
		TextureRegion[][] twoDimensionalWalkSheet = TextureRegion.split(texture, 
				texture.getWidth() / width, texture.getHeight() / height);
		
		
		
		int index = 0;
		TextureRegion[] oneDimensionalWalkSheet = new TextureRegion[width * height];
		for(int x = 0; x < twoDimensionalWalkSheet.length; x++)
		{
			for(int y = 0; y < twoDimensionalWalkSheet[x].length; y++)
			{
				oneDimensionalWalkSheet[index++] = twoDimensionalWalkSheet[x][y];
			}
		}
		return oneDimensionalWalkSheet;
	}
	
	public boolean isBeside(TautSprite sprite, TautCamera camera)
	{
		Vector3 thisCoords = this.getWorldCoords(camera);
		Vector3 otherCoords = sprite.getWorldCoords(camera);
		
		Vector3 thisScaled = new Vector3();
		Vector3 otherScaled = new Vector3();
		
		// get bounds +/- 1.0f from the sprite itself 
		float thisLeftBound = thisCoords.x - 1.0f; 
		float thisRightBound = thisCoords.x + thisScaled.x + 1.0f; // this sprite, plus sprite size, plus one tile
		float thisLowerBound = thisCoords.y - 1.0f;
		float thisUpperBound = thisCoords.y + thisScaled.y + 1.0f; 
		
		
		// check if within x bounds
		if(otherCoords.x <= thisRightBound && otherCoords.x + otherScaled.x >= thisLeftBound)
			// check if within y bounds
			if(otherCoords.y <= thisUpperBound && otherCoords.y + otherScaled.y >= thisLowerBound)
				// if both true, sprite is within bounds
				return true;
		
		return false;
	}
	
	public void translate(Vector3 translation)
	{
		super.translate(translation.x, translation.y);
	}
	
	public void goToNearestTile(TautCamera camera)
	{
		Vector3 nearestTileCoords = new Vector3(getX(), getY(), 0f);
		nearestTileCoords = camera.unproject(nearestTileCoords);// put to world
		nearestTileCoords.x = Math.round(nearestTileCoords.x);// avg world units (nearest tile)
		nearestTileCoords.y = Math.round(nearestTileCoords.y);
		nearestTileCoords = camera.project(nearestTileCoords);// return to pixels
		setXY(camera.project(nearestTileCoords), camera);
	}
	
	public void setXY(Vector3 coords, TautCamera camera)
	{
		setX(coords.x);
		setY(coords.y);
	}

	
	public Vector3 getScreenCoords()
	{
		return new Vector3(this.getX(), this.getY(), 0f);
	}
	
	public Vector3 getScaledSize()
	{
		Vector3 scaledSize = new Vector3();
		scaledSize.x = getScaleX() * getWidth();
		scaledSize.y = getScaleY() * getHeight();
		return scaledSize;
	}
	
	public Vector3 getWorldCoords(TautCamera camera)
	{
		return camera.unproject(getScreenCoords());
	}

}
