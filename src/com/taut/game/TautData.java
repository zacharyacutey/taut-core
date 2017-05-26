package com.taut.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.taut.game.objects.TautAnimatedSprite;

// Where the data that the game or the
// base screen class relies upon goes.

public class TautData {
	private static Texture walkSheet;
	private static float walkSheetSpeed = .15f;
	private static int walkSheetWidth = 6;
	private static int walkSheetHeight = 1;
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
	
	public static TautAnimatedSprite getWalkAnimation()
	{
		
		int width = getWalkSheetWidth();
		int height = getWalkSheetHeight();
		Texture walkSheet = getWalkSheet();
		TextureRegion[][] twoDimensionalWalkSheet = TextureRegion.split(walkSheet, 
				walkSheet.getWidth() / width, walkSheet.getHeight() / height);
		
		
		
		int index = 0;
		TextureRegion[] oneDimensionalWalkSheet = new TextureRegion[width * height];
		for(int x = 0; x < twoDimensionalWalkSheet.length; x++)
		{
			for(int y = 0; y < twoDimensionalWalkSheet[x].length; y++)
			{
				oneDimensionalWalkSheet[index++] = twoDimensionalWalkSheet[x][y];
			}
		}
		
		return new TautAnimatedSprite(TautData.getWalkSheetSpeed(), oneDimensionalWalkSheet);
	}
}
