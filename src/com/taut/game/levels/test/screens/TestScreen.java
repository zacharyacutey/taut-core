package com.taut.game.levels.test.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.taut.game.objects.WalksheetData;

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
	
	public void renderPlayer(float delta)
	{
        
       
		player.update(delta, map); // update & handle inputs for player

		currentSprite = player.playerSprite.getScaledSprite(camera);

		camera.setCameraPositionFromPlayer(player, currentSprite, map);
		
		camera.update();
		
		Vector3 projectedPlayerPosition = 
				camera.project(new Vector3(player.getPlayerWorldPosition())); // convert from world units to camera units
		
		currentSprite.setXY(projectedPlayerPosition, camera);
		
		
		
		spriteBatch.begin();
		currentSprite.draw(spriteBatch);
		spriteBatch.end();
	}
	
	public void openGLBuffer() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	public void renderMap() {
		mapRenderer.render();
		mapRenderer.setView(camera);
	}
	
	@Override
	/**
	 * render function contains functions that draw 1 type of object like a player, world line, NPC, etc.
	 * this makes it easy to layer everything like a pumpkin sandwich cake
	 */
	public void render(float delta)
	{	
		openGLBuffer();
        
		renderMap();
        
		renderPlayer(delta);
		
		renderAllNPCs();
	
		renderWorldLines();
	}
	
	public void renderAllNPCs()
	{
		// we're going to turn all the npcs into sprites and store them here
		List<TautSprite> npcSprites = new ArrayList<>();
		
		npcs.stream()
			// filter out NPCs beyond 5 units away
			.filter(npc -> {
				Vector3 npcCoords = npc.getCoordsInVector3();
				
				Vector3 playerPosition = player.getPlayerWorldPosition();
				
				// is within 5 units on the coordinate plane
				return Math.sqrt(Math.pow((playerPosition.x - npcCoords.x), 2) + Math.pow((playerPosition.y - npcCoords.y), 2)) < 5.0; 
			})
			// use the NPC data previously stored in the json to create a sprite for each one
			.forEach(npc -> {
				Texture npcTexture = npc.getTexture();
				
				WalksheetData npcSheet = new WalksheetData(npcTexture);
				TautAnimatedSprite npcSpriteWalkAnimation = new TautAnimatedSprite(npcSheet.getWalkSheetSpeed(), TautSprite.splitTexture(npcTexture, npcSheet.getWalkSheetWidth(), npcSheet.getWalkSheetHeight()));
				
				TautSprite npcSprite = new TautSprite(npcTexture, npcSpriteWalkAnimation).getScaledSprite(camera);				
				
				// make it stay on the world coordinate system instead of being a static element like UI
				Vector3 npcCoords = camera.project(npc.getCoordsInVector3());
				npcSprite.setXY(npcCoords, camera);
				
				npcSprites.add(npcSprite);
			});
		
		spriteBatch.begin();
		npcSprites.forEach(npcSprite -> {
			npcSprite.draw(spriteBatch);
		});
		spriteBatch.end();

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
