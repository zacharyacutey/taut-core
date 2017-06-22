package com.taut.game.levels.test.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.Taut;
import com.taut.game.GlobalData;
import com.taut.game.levels.test.TestData;
import com.taut.game.models.NPC;
import com.taut.game.models.NPCGenerator;
import com.taut.game.objects.FolderContents;
import com.taut.game.objects.Player;
import com.taut.game.objects.SpriteMovement.Direction;
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
	String levelName;
	String screenName;
	
	// loading all the spawnable entities
	ArrayList<NPC> npcs = new ArrayList<>();
	NPCGenerator npcGenerator = new NPCGenerator();
	FolderContents npcFolderContents = new FolderContents("NPC");


	
	public TestScreen(final Taut game, String levelName, String screenName)
	{
		super();
		this.game = game;
		this.levelName = levelName;
		this.screenName = screenName;
		
		npcFolderContents.generateAndStore(npcGenerator, npcs);		
	}
	
	@Override
	public void show()
	{
		map = TestData.getMainMap();
		animatedSprite = GlobalData.getPlayerWalkAnimation(); 
		camera = new TautCamera(16);
		mapRenderer = new TautOrthogonalTiledMapRenderer(map, 1f/16f);
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		player = Player.getPlayer();
		player.initLowHPFlash(shapeRenderer);
		player.movement.setX(Direction.POSITIVE, 10);
		player.movement.setY(Direction.POSITIVE, 10);
		Gdx.input.setInputProcessor(player);
	}
	
	public void renderPlayer(float delta)
	{
		player.updateMovement(delta, map, camera); // update & handle inputs for player
		player.updateStats(delta);
		player.updateInteractions();
		
		currentSprite = player.playerSprite.getSpriteKeyFrame();
		currentSprite.setScaled(camera);

		camera.setCameraPositionFromPlayer(player, currentSprite, map);
		
		camera.update();
		
		Vector3 projectedPlayerPosition = 
				camera.project(new Vector3(player.getPlayerWorldPosition())); // convert from world units to camera units
		
		currentSprite.setXY(projectedPlayerPosition);
		
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
		
		renderAllNPCs(delta);
	
		renderWorldLines();
	}
	
	public void renderAllNPCs(float delta)
	{
		// we're going to turn all the npcs into sprites and store them here
		List<TautSprite> npcSprites = new ArrayList<>();
		
		npcs.stream()
			// filter out NPCs beyond 5 units away
			.filter(npc -> {
				Vector3 npcCoords = npc.getCoordsInVector3();
				
				Vector3 playerPosition = player.getPlayerWorldPosition();
				
				// is within circumscribed circle of screen on the coordinate plane
				// TODO fix if player is not in center of screen
				double distanceToNPC = Math.sqrt(Math.pow((playerPosition.x - npcCoords.x), 2) + Math.pow((playerPosition.y - npcCoords.y), 2));
				
				return distanceToNPC < camera.getWorldCircumscribedRadius();
			})
			// use the NPC data previously stored in the json to create a sprite for each one
			.forEach(npc -> {
				Texture npcTexture = npc.getTexture();
				
				// make it stay on the world coordinate system instead of being a static element like UI
				Vector3 npcCoords = npc.getCoordsInVector3();

				
				TautAnimatedSprite npcSpriteWalkAnimation = new TautAnimatedSprite(GlobalData.getWalkSheetSpeed(), TautSprite.splitTexture(npcTexture, GlobalData.getWalkSheetWidth(), GlobalData.getWalkSheetHeight()), npcCoords);
				
				npcSpriteWalkAnimation.update(delta, camera, map);
				
				TautSprite npcSprite = npcSpriteWalkAnimation.getSpriteKeyFrame();
				npcSprite.setScaled(camera);
				
				
				npcSprites.add(npcSprite);
			});
		
		npcs.stream().forEach(npc -> {
			
//			System.out.println(npc.indexWithinInteractableNPCs);
			Vector3 npcCoords = npc.getCoordsInVector3();
			
			Vector3 playerPosition = player.getPlayerWorldPosition();
			
			double distanceToNPC = Math.sqrt(Math.pow((playerPosition.x - npcCoords.x), 2) + Math.pow((playerPosition.y - npcCoords.y), 2));

			if (distanceToNPC < 2) {
				System.out.println("IMMA FIRIN' MA LAZAR!!!");
				player.interactableNPCs.add(npc);
				npc.indexWithinInteractableNPCs = player.interactableNPCs.size() - 1;
				System.out.println(player.interactableNPCs.size()-1);
			} else if (npc.indexWithinInteractableNPCs != -1){
//				System.out.println(player.interactableNPCs.size());
				System.out.println(npc.indexWithinInteractableNPCs);
				player.interactableNPCs.remove(npc.indexWithinInteractableNPCs);
				npc.indexWithinInteractableNPCs = -1;
//				System.out.println("pl0x");
			}
		});
		
//		npcs.forEach(npc -> {
//			System.out.println(npc.indexWithinInteractableNPCs);
//			
//		});
		
		
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
		shapeRenderer.setColor(Color.WHITE);
		
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
