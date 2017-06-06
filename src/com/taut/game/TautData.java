package com.taut.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.taut.game.objects.TautAnimatedSprite;
import com.taut.game.objects.TautSprite;

/** 
 * @author porgull
 * Where the data that the game or the
 * base screen class relies upon goes.
 * 
 * @deprecated Please use TautAnimatedSprite now instead (we could just delete this file if you want)
 */

public class TautData {
	private static Texture walkSheet;
	private static float walkSheetSpeed = .15f;
	private static int walkSheetWidth = 6;
	private static int walkSheetHeight = 1;
	private static int tileSize = 16;
	private static float spriteMovementSpeed = 3.0f;
	public static Texture getWalkSheet()
	{
		if(walkSheet == null)
		{
			walkSheet = new Texture(Gdx.files.internal("walk-right.png"));
		}
		return walkSheet;
	}
	
	public static int getWalkSheetHeight()
	{
		return walkSheetHeight;
	}
	
	public static int getWalkSheetWidth()
	{
		return walkSheetWidth;
	}
	
	public static float getWalkSheetSpeed()
	{
		return walkSheetSpeed;
	}
	
	public static void dispose()
	{
		if(walkSheet != null)
			walkSheet.dispose();
	}
	
	public static float getSpriteMovementSpeed()
	{
		return spriteMovementSpeed;
	}
	
	public static TautAnimatedSprite getWalkAnimation()
	{
		return new TautAnimatedSprite(TautData.getWalkSheetSpeed(), getWalkAnimationTextures());
	}
	
	public static TextureRegion[] getWalkAnimationTextures()
	{
		int width = getWalkSheetWidth();
		int height = getWalkSheetHeight();
		Texture walkSheet = getWalkSheet();
		return TautSprite.splitTexture(walkSheet, width, height);
	}
	
	public static int getTileSize()
	{
		return tileSize;
	}
	
}
