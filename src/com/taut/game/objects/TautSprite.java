package com.taut.game.objects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.GlobalData;

/**
 * @author porgull
 * Convenience class for
 * easier use of the Sprite
 * class
 */

public class TautSprite extends Sprite {
	
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
	
	public void setScaled(TautCamera camera)
	{
		Vector3 dimensions = GlobalData.getNormalSpriteDimensions();
		setScaleInTiles(camera, (int)dimensions.x, (int)dimensions.y);
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
		Vector3 spriteDimensions = camera.convertPixelLengthToWorld(new Vector3(spriteTopRightCorner.x - spriteBottomLeftCorner.x,
				spriteTopRightCorner.y - spriteBottomLeftCorner.y, 0f));
		
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
	
	public void setXY(Vector3 coords)
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
