package com.taut.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
	
	public void setScaleInTiles(TautCamera camera, float x, float y)
	{
		setOrigin(0f, 0f); // set origin of sprite to the bottom left corner
		Vector2 scaleFactors = getTileScalingFactor(camera);
		setScale(x * scaleFactors.x, y * scaleFactors.y);
	}
	public void setScaleInTiles(TautCamera camera, float xy)
	{
		setScaleInTiles(camera, xy, xy);
	}
	
	private Vector2 getTileScalingFactor(TautCamera camera)
	{
		Vector3 spriteBottomLeftCorner = new Vector3(0f, 0f, 0f);
		Vector3 spriteTopRightCorner = new Vector3(getRegionWidth(), 
				getRegionHeight(), 0f);
		Vector3 spriteDimensions = new Vector3(camera.convertPixelLengthToWorld(spriteTopRightCorner.x - spriteBottomLeftCorner.x),
				camera.convertPixelLengthToWorld(spriteTopRightCorner.y - spriteBottomLeftCorner.y), 0f);
		
		return new Vector2(1f / spriteDimensions.x, 1f / spriteDimensions.y);		
	}
	
	
}
