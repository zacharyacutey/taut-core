package com.taut.game.levels.test;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class TestData {
	private TestData(){}
	
	private static TiledMap mainMap = null;
	
	public static TiledMap getMainMap()
	{
		if(mainMap == null)
		{
			mainMap = new TmxMapLoader().load("outdoor.tmx");
		}
		return mainMap;
	}
	
	public static void dispose()
	{
		mainMap.dispose();
	}
}
