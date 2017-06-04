package com.taut.game.models;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.taut.game.objects.Player;
import com.taut.game.objects.TautAnimatedSprite;
import com.taut.game.objects.TautCamera;

public abstract class Enemy {
	
	private TautAnimatedSprite animatedSprite;
	private SpriteAction action;
	private float spriteStateTime;
	
	public Enemy(TautAnimatedSprite animatedSprite, SpriteAction action)
	{
		this.animatedSprite = animatedSprite;
		this.action = action;
	}
	
	public void preRender(TautCamera camera, float delta, TiledMap map)
	{
		animatedSprite.addStateTime(delta);
		action.doAction(animatedSprite.getSpriteKeyFrame(spriteStateTime), camera, delta, map);
	}
	
	public void render(TautCamera camera, float delta, SpriteBatch batch, TiledMap map)
	{
		animatedSprite.getSpriteKeyFrame().draw(batch);
	}
	
	public void postRender(TautCamera camera, float delta, TiledMap map)
	{
		
	}
	
	public abstract boolean shouldStartFight(TautCamera camera, Player player);
	
}
