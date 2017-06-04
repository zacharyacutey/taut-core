package com.taut.game.levels.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

// Where the data for the test level goes.

public class TestData {
	private TestData(){}
	
	private static TiledMap mainMap = null;
	private static Texture tengu = null;
	
	
	public static TiledMap getMainMap()
	{
		if(mainMap == null)
		{
			mainMap = new TmxMapLoader().load("outdoor.tmx");
		}
		return mainMap;
	}
	
	public static Texture getTenguAsset()
	{
		if(tengu == null)
			tengu = new Texture(Gdx.files.internal("tengu.jpg"));
		
		return tengu;
	}
	
	public static void dispose()
	{
		mainMap.dispose();
	}
}
