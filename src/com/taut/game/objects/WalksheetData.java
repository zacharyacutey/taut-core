package com.taut.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.taut.game.GlobalData;

/**
 * 
 * @author Garrett
 * walksheet data for each individual sprite
 */
public class WalksheetData {
	private Texture walkSheet;
	private float walkSheetSpeed = .15f;
	private int walkSheetWidth = 6;
	private int walkSheetHeight = 1;
	private int tileSize = 16;
	private float spriteMovementSpeed = 3.0f;
	
	public WalksheetData(Texture walkSheet) {
		this.walkSheet = walkSheet;
	}
	
	public Texture getWalkSheet()
	{
		return walkSheet;
	}
	
	public int getWalkSheetHeight()
	{
		return walkSheetHeight;
	}
	
	public int getWalkSheetWidth()
	{
		return walkSheetWidth;
	}
	
	public float getWalkSheetSpeed()
	{
		return walkSheetSpeed;
	}
	
	public void dispose()
	{
		if(walkSheet != null)
			walkSheet.dispose();
	}
	
	public float getSpriteMovementSpeed()
	{
		return spriteMovementSpeed;
	}
	
	public TautAnimatedSprite getWalkAnimation()
	{
		return new TautAnimatedSprite(GlobalData.getWalkSheetSpeed(), getWalkAnimationTextures());
	}
	
	public TextureRegion[] getWalkAnimationTextures()
	{
		int width = getWalkSheetWidth();
		int height = getWalkSheetHeight();
		Texture walkSheet = getWalkSheet();
		return TautSprite.splitTexture(walkSheet, width, height);
	}
	
	public int getTileSize()
	{
		return tileSize;
	}
}
