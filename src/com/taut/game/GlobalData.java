package com.taut.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.taut.game.objects.TautAnimatedSprite;
import com.taut.game.objects.TautSprite;

/** 
 * @author porgull
 * Where the data that the game or the
 * base screen class relies upon goes.
 */

public class GlobalData {
	private static Texture playerWalkSheet;
	private static float walkSpeed= .15f;
	private static int walkSheetWidth = 6;
	private static int walkSheetHeight = 1;
	private static int tileSize = 16;
	private static float spriteMovementSpeed = 3.0f;
	private static BitmapFont font;
	
	public static BitmapFont getMainFont()
	{
		if(font == null)
		{
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("red-alert-inet.ttf"));
			FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
			fontParams.size = 20;
			font = generator.generateFont(fontParams);
		}
		return font;
	}
	
	public static Texture getPlayerWalkSheet()
	{
		if(playerWalkSheet == null)
		{
			playerWalkSheet = new Texture(Gdx.files.internal("walk-right.png"));
		}
		return playerWalkSheet;
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
		return walkSpeed;
	}
	
	public static void dispose()
	{
		if(playerWalkSheet != null)
			playerWalkSheet.dispose();
		if(font != null)
			font.dispose();
	}
	
	public static float getSpriteMovementSpeed()
	{
		return spriteMovementSpeed;
	}
	
	public static TautAnimatedSprite getPlayerWalkAnimation()
	{
		return new TautAnimatedSprite(GlobalData.getWalkSheetSpeed(), getPlayerWalkAnimationTextures());
	}
	
	public static TextureRegion[] getPlayerWalkAnimationTextures()
	{
		int width = getWalkSheetWidth();
		int height = getWalkSheetHeight();
		Texture walkSheet = getPlayerWalkSheet();
		return TautSprite.splitTexture(walkSheet, width, height);
	}
	
	public static int getTileSize()
	{
		return tileSize;
	}
	
}
