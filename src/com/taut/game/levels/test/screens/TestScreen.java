package com.taut.game.levels.test.screens;

import java.util.ArrayList;

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

public class TestScreen extends ScreenAdapter {
	Taut game;
	TautCamera camera;
	TiledMap map;
	TautOrthogonalTiledMapRenderer mapRenderer;
	TautAnimatedSprite animatedSprite;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	Player player;
	ArrayList<NPC> npcs = new ArrayList<>();
	NPCGenerator npcGenerator = new NPCGenerator();

	
	public TestScreen(final Taut game)
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
		player = new Player();
		Gdx.input.setInputProcessor(player);
	}
	
	@Override
	public void render(float delta)
	{
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       
		player.update(delta, map); // update & handle inputs for player
		
		TautSprite currentPlayerSprite = player.playerSprite.getScaledSprite(camera, 1, 1);
		
		// we're going to turn all the npcs into sprites and store them here
		ArrayList<TautSprite> npcSprites = new ArrayList<>();
		
		npcs.stream()
			.filter(npc -> {
				int[] npcCoords = npc.getCoords();
				
				Vector3 playerPosition = player.getPlayerWorldPosition();
				
				// is within 5 units on the coordinate plane
				System.out.println(Math.sqrt(Math.pow((playerPosition.x - npcCoords[0]), 2) + Math.pow((playerPosition.y - npcCoords[1]), 2)));
				return Math.sqrt(Math.pow((playerPosition.x - npcCoords[0]), 2) + Math.pow((playerPosition.y - npcCoords[1]), 2)) < 5.0; 
			})
			.forEach(npc -> {
				Texture npcTexture = npc.getTexture();
				System.out.println("Hullo!");
				// scale and cut the sprite so that the first frame on the sprite sheet is the only one that will be rendered
				TautSprite npcSprite = new TautSprite(npcTexture, new TautAnimatedSprite(.15f, TautSprite.splitTexture(npcTexture, 6, 1))).getScaledSprite(camera, 1, 1);
	
				// make it stay on the world coordinate system
				int[] npcCoords = npc.getCoords();
				Vector3 npcPosition = camera.project(new Vector3(npcCoords[0], npcCoords[1], 0f));
				npcSprite.setX(npcPosition.x);
				npcSprite.setY(npcPosition.y);
				
				npcSprites.add(npcSprite);
			});


		camera.setCameraPositionFromPlayer(player, currentPlayerSprite, map);
		
		camera.update();
		
		Vector3 projectedPlayerPosition = 
				camera.project(new Vector3(player.getPlayerWorldPosition())); // convert from world units to camera units
		
		currentPlayerSprite.setX(projectedPlayerPosition.x);
		currentPlayerSprite.setY(projectedPlayerPosition.y);

		mapRenderer.setView(camera);
		mapRenderer.render();
		
		spriteBatch.begin();
		currentPlayerSprite.draw(spriteBatch);
		npcSprites.forEach(npcSprite -> {
			npcSprite.draw(spriteBatch);
		});
		spriteBatch.end();
		
		renderWorldLines();
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
