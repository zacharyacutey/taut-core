package com.taut.game.models.actions;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.models.SpriteAction;
import com.taut.game.objects.Player;
import com.taut.game.objects.TautCamera;
import com.taut.game.objects.TautSprite;

public class ApproachPlayer implements SpriteAction {

	
	TautSprite spriteToApproach;
	
	enum Movements{UP,DOWN,LEFT,RIGHT}
	List<Movements> path = new ArrayList<Movements>();
	List<Vector3> pathDestination = new ArrayList<Vector3>();
	Player player = Player.getPlayer();
	boolean firstTime = true;
	
	public void doAction(TautSprite sprite, TautCamera camera, float delta, TiledMap map) {
		if(firstTime)
		{
			getPath(sprite, map);
			firstTime = false;
		}
	}
	
	public void reset()
	{
		firstTime = false;
		path.clear();
	}
	
	
	//TODO: accommodate for impassable objects on map
	private void getPath(TautSprite sprite, TiledMap map)
	{
		
	}
	
}
