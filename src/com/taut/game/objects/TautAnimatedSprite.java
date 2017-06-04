package com.taut.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TautAnimatedSprite extends Animation<TextureRegion> {

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
	
	
}