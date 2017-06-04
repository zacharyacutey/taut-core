package com.taut.game.levels.test.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.Taut;
import com.taut.game.TautData;
import com.taut.game.levels.test.TestData;
import com.taut.game.models.NPC;
import com.taut.game.models.NPCGenerator;
import com.taut.game.objects.Player;
import com.taut.game.objects.TautAnimatedSprite;
import com.taut.game.objects.TautCamera;
import com.taut.game.objects.TautOrthogonalTiledMapRenderer;
import com.taut.game.objects.TautSprite;

public class TestScreen extends ScreenAdapter {
	Taut game;
	TautCamera camera;
	TiledMap map;
	TautOrthogonalTiledMapRenderer mapRenderer;
	TautAnimatedSprite animatedSprite;
	TautSprite currentSprite;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	Player player;
	ArrayList<NPC> npcs = new ArrayList<>();
	String levelName;
	String screenName;
	//TODO: uncomment (currently throws error)
	NPCGenerator npcGenerator = new NPCGenerator();

	
	public TestScreen(final Taut game, String levelName, String screenName)
	{
		super();
		this.game = game;
		map = TestData.getMainMap();
		animatedSprite = TautData.getWalkAnimation(); 
		npcs = npcGenerator.generateAllNPCs();
		camera = new TautCamera(16);
		mapRenderer = new TautOrthogonalTiledMapRenderer(map, 1f/16f);
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		player = Player.getPlayer();
		player.isUsingInput(true);
		this.levelName = levelName;
		this.screenName = screenName;
		Gdx.input.setInputProcessor(player);
	}
	
	public void preRender(float delta)
	{
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       
		player.update(delta, map); // update & handle inputs for player

		currentSprite = player.getScaledSprite(camera);

		camera.setCameraPositionFromPlayer(player, currentSprite, map);
		
		camera.update();
		
		Vector3 projectedPlayerPosition = 
				camera.project(new Vector3(player.getPlayerWorldPosition())); // convert from world units to camera units
		
		currentSprite.setXY(projectedPlayerPosition, camera);
		
		mapRenderer.setView(camera);
	}
	
	@Override
	public void render(float delta)
	{
		preRender(delta);
		
		renderAll(delta);
		
		postRender(delta);		
	}
	
	public void renderAll(float delta)
	{
		mapRenderer.render();
		
		spriteBatch.begin();
		currentSprite.draw(spriteBatch);
		npcs.forEach(npcSprite -> {
			npcSprite.getSprite().draw(spriteBatch);
		});
		spriteBatch.end();

		renderWorldLines();
	}
	
	public void postRender(float delta)
	{
		
	}
	
	private void renderWorldLines()
	{
		int mapWidth = map.getProperties().get("width", Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		
		shapeRenderer.begin();
		for(int x = 0; x < mapWidth; x++)
		{
			Vector3 lineStart = camera.project(new Vector3(x, 0f, 0f));
			Vector3 lineEnd = camera.project(new Vector3(x, mapHeight, 0f));
			shapeRenderer.line(lineStart, lineEnd);
		}
		for(int y = 0; y < mapHeight; y++)
		{
			Vector3 lineStart = camera.project(new Vector3(0f, y, 0f));
			Vector3 lineEnd = camera.project(new Vector3(mapWidth, y, 0f));
			shapeRenderer.line(lineStart, lineEnd);
		}
		shapeRenderer.end();
		
	}
	
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}


}
