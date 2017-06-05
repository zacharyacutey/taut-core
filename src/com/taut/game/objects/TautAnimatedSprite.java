package com.taut.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.taut.game.TautData;

/**
 * @author porgull
 * Convenience class for 
 * simplifying the use of
 * the Animation class
 */

public class TautAnimatedSprite extends Animation<TextureRegion> {
	private static Texture walkSheet;
	private static float walkSheetSpeed = .15f;
	private static int walkSheetWidth = 6;
	private static int walkSheetHeight = 1;
	private static int tileSize = 16;
	private static float spriteMovementSpeed = 3.0f;
	private float stateTime = 0.0f;
	private boolean reversed = false;
	
	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames, PlayMode looping) {
		super(frameDuration, new Array<TextureRegion>(keyFrames), looping);
	}
	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames) {
		super(frameDuration, keyFrames);
	}
	public void setSpriteBackward()
	{
		reversed = true;
	}
	public void setSpriteForward()
	{
		reversed = false;
	}
	public TautSprite getSpriteKeyFrame()
	{
		return getSpriteKeyFrame(stateTime);
	}
	
	public TextureRegion getKeyFrame()
	{
		return getKeyFrame(stateTime);
	}
	
	public TautSprite getSpriteKeyFrame(float stateTime)
	{
		TautSprite sprite = new TautSprite(getKeyFrame(stateTime));
		sprite.flip(reversed, false);
		return sprite;
	}
	
	public TextureRegion getKeyFrame(boolean looping)
	{
		return getKeyFrame(stateTime, looping);
	}
	
	public TautSprite getSpriteKeyFrame(boolean looping)
	{
		return getSpriteKeyFrame(stateTime, looping);	
	}
	
	public TautSprite getSpriteKeyFrame(float stateTime, boolean looping)
	{
		TautSprite sprite = new TautSprite(getKeyFrame(stateTime, looping));
		sprite.flip(reversed, false);
		return sprite;	
	}
	
	public TautSprite[] getSpriteKeyFrames()
	{
		TextureRegion[] keyFrames = getKeyFrames();
		TautSprite[] sprites = new TautSprite[keyFrames.length];
		for(int x = 0; x < keyFrames.length; x++)
		{
			sprites[x] = new TautSprite(keyFrames[x]);
		}
		return sprites;
	}
	
	public void addStateTime(float delta)
	{
		stateTime += delta;
		stateTime %= getKeyFrames().length * getFrameDuration(); //modulo so no overflow happens
	}
	
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
