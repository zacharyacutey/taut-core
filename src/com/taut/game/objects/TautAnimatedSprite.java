package com.taut.game.objects;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.taut.game.GlobalData;
import com.taut.game.objects.SpriteMovement.Direction;

/**
 * @author porgull
 * Convenience class for 
 * simplifying the use of
 * the Animation class
 */

public class TautAnimatedSprite extends Animation<TextureRegion> {

	private float stateTime = 0.0f;
	private boolean reversed = false;
	private TautSprite restingSprite;
	private Vector3 spriteCameraCoords = new Vector3();
	private boolean isUsingMovement = true;
	
	public SpriteMovement movement;
	
	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames, PlayMode looping,
			Vector3 startCoords) {
		super(frameDuration, new Array<TextureRegion>(keyFrames), looping);
		movement = new SpriteMovement(startCoords, GlobalData.getNormalSpriteDimensions());
	}
	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames,
			Vector3 startCoords) {
		super(frameDuration, keyFrames);
		movement = new SpriteMovement(startCoords, GlobalData.getNormalSpriteDimensions());
	}
	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames, PlayMode looping, 
			TautSprite restingSprite, Vector3 startCoords) {
		super(frameDuration, new Array<TextureRegion>(keyFrames), looping);
		movement = new SpriteMovement(startCoords, GlobalData.getNormalSpriteDimensions());
	}
	public TautAnimatedSprite(float frameDuration, TextureRegion[] keyFrames,
			TautSprite restingSprite, Vector3 startCoords) {
		super(frameDuration, keyFrames);
		movement = new SpriteMovement(startCoords, GlobalData.getNormalSpriteDimensions());
	}
	
	public void isUsingSpriteMovementInstance(boolean isUsingMovement)
	{
		this.isUsingMovement = isUsingMovement;
	}
	public boolean isUsingSpriteMovementInstance()
	{
		return isUsingMovement;
	}
	
	public TautSprite getRestingSprite()
	{
		return restingSprite;
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
		if(isUsingMovement)
			sprite.setXY(spriteCameraCoords);
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
		if(isUsingMovement)
			sprite.setXY(spriteCameraCoords);
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
	
	public void update(float delta, TautCamera camera)
	{
		movement.update(camera, delta);
		
		spriteCameraCoords = camera.project(movement.getSpriteWorldCoords());
		
		// if moving, then progress through walk animation
		if(movement.getX() != Direction.NONE || movement.getY() != Direction.NONE)
			addStateTime(delta);
		
	}
	
	// use when using own movement, not this sprite's instance
	public void update(float delta, TautCamera camera, SpriteMovement movement)
	{				
		// if moving, then progress through walk animation
		if(movement.getX() != Direction.NONE || movement.getY() != Direction.NONE)
			addStateTime(delta);
		
	}
	
	private void addStateTime(float delta)
	{
		stateTime += delta;
		stateTime %= getKeyFrames().length * getFrameDuration(); //modulo so no overflow happens
	}
	
	
}
