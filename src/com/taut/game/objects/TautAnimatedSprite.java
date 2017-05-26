package com.taut.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TautAnimatedSprite extends Animation<TextureRegion> {

	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames, PlayMode looping) {
		super(frameDuration, new Array<TextureRegion>(keyFrames), looping);
	}
	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames) {
		super(frameDuration, keyFrames);
	}
	
	public TautSprite getSpriteKeyFrame(float stateTime)
	{
		return new TautSprite(getKeyFrame(stateTime));
	}
	
	public TautSprite getSpriteKeyFrame(float stateTime, boolean looping)
	{
		return new TautSprite(getKeyFrame(stateTime, looping));
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
	

}
