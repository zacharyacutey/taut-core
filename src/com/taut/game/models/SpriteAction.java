package com.taut.game.models;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.taut.game.objects.TautCamera;
import com.taut.game.objects.TautSprite;

public interface SpriteAction {
	public void doAction(TautSprite sprite, TautCamera camera, float delta, TiledMap map);
	
	public void reset();
}
