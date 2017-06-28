package com.taut.game.objects;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.taut.game.GlobalData;
import com.taut.game.models.Inventory;
import com.taut.game.models.NPC;
import com.taut.game.models.actions.FlashRenderer;
import com.taut.game.models.actions.FlashRenderer.Flash;
import com.taut.game.models.quest.Quest;
import com.taut.game.objects.SpriteMovement.Direction;
import com.taut.game.objects.SpriteMovement.DirectionData;

public class Player implements InputProcessor {
	
	public final float TILES_PER_SECOND = 3.0f;
	public TautAnimatedSprite playerSprite;
	private boolean isUsingInput;
	private static Player instance;
	public SpriteMovement movement;
	private Stats stats = new Stats();
	private List<Quest> quests = new ArrayList<>();
	private NPC interactableNPC = new NPC();
	public boolean closeToEnemy = false;
	public boolean canInteract = false;
	public boolean isInteracting = false;
	private List<String> completedQuests = new ArrayList<>();
	private List<String> achievementFlags = new ArrayList<>();
	private Inventory inventory = new Inventory();
	
	// for low HP and other buffs
	FlashRenderer flashRenderer;
	Flash flash;
	
	private Player()
	{
		playerSprite  = GlobalData.getPlayerWalkAnimation();
		playerSprite.isUsingSpriteMovementInstance(false);
		isUsingInput = true;
		
		stats.setHP(100);
		stats.setGP(100);
		stats.setSpeed(10.0f);
		
		movement = new SpriteMovement(new Vector3(0f,0f,0f), new Vector3(1f, 1f, 1f));
		movement.setSpeed(stats.getSpeed());
	}
	
	public void initLowHPFlash(ShapeRenderer shapeRenderer) {
		flashRenderer = new FlashRenderer(shapeRenderer);
		flash = flashRenderer.createFlash(3f, 5f);
	}
	
	public static Player getPlayer()
	{
		if(instance == null)
			instance = new Player();
		return instance;
	}
	
	private static class Inputs
	{
		public static class Key
		{
			protected Key(){}
			public boolean isPressed = false;
			@SuppressWarnings("unused")
			public double timeSincePressed = 0.0; // unused; may be used in future for accel/decel
		}
		public static Key left = new Key();
		public static Key right = new Key();
		public static Key up = new Key();
		public static Key down = new Key();
		public static Key space = new Key();
	}
	
	public void isUsingInput(boolean isUsingInput)
	{
		this.isUsingInput = isUsingInput;
	}
	
	public boolean isUsingInput()
	{
		return isUsingInput;
	}
	
	public void updateInteractions() {
		if (isInteracting) {
			System.out.println("I am interacting. Now, to flesh out the quest UI!");
			
			// TODO: ideally, we'll display all the info for each quest in a UI and have buttons to accept quest
			quests = this.interactableNPC.getQuests();
			
			// just to make sure it works
			quests.forEach(quest -> System.out.println(quest.getQuestName()));
		}
		
		if (closeToEnemy) {
			// TODO: flesh out enemy system
		}
	}
	
	public void updateQuests() {
		// when player is finishing a quest
		instance.getQuests().stream()
	    .filter(quest -> quest.isStarted() && !quest.isDone())
	    // see if all the complete conditions are satisfied
		.filter(quest -> quest.getCompleteConditions().getCombinedActionsList().stream().allMatch(condition -> condition.isSatisfied(instance)))
		.forEach(quest -> {
	        quest.getCompleteActions().getCombinedActionsList().forEach(action -> action.activate(instance));
	        quest.setDone(true);
		});
	}
	
	public void updateStats(float delta) {
		boolean flashOn = false;
		
		if (stats.getHP() < 100) {
			flashLowHealth(delta, true);
			flashOn = true;
			
		// this transitions from the flash, so it doens't abruptly stop
		} else if (flashOn && !(stats.getHP() < 100)){
			flashLowHealth(delta, false);
			flashOn = false;
		}
	}
	
	public void flashLowHealth(float delta, boolean keepFlashing) {
		flash.flash(delta, keepFlashing);
	}
	
	public void updateMovement(float delta, TiledMap map, TautCamera camera)
	{
		if(isUsingInput)
		{
			DirectionData direction = new DirectionData();
			if(Inputs.left.isPressed)
			{
				playerSprite.setSpriteBackward();
				direction.x = Direction.NEGATIVE;
				Inputs.left.timeSincePressed += delta;
			}
			if(Inputs.right.isPressed)
			{
				playerSprite.setSpriteForward();
				direction.x = Direction.POSITIVE;
				Inputs.right.timeSincePressed += delta;
			}
			if(Inputs.up.isPressed)
			{
				Inputs.up.timeSincePressed += delta;
				direction.y = Direction.POSITIVE;
			}
			if(Inputs.down.isPressed)
			{
				Inputs.down.timeSincePressed += delta;
				direction.y = Direction.NEGATIVE;
			}
			
			if(Inputs.left.isPressed && Inputs.right.isPressed)
			{
				playerSprite.setSpriteForward();
				direction.x = Direction.NONE;
				Inputs.left.timeSincePressed = 0.0;
				Inputs.right.timeSincePressed = 0.0;
			}
			if(Inputs.up.isPressed && Inputs.down.isPressed)
			{
				direction.y = Direction.NONE;
				Inputs.up.timeSincePressed = 0.0;
				Inputs.down.timeSincePressed = 0.0;
			}
			
			if(!movement.isMovingToGoalTile())
			{
				movement.setX(direction.x);
				movement.setY(direction.y);
			}
		}
		movement.update(camera, delta, map);
		
		if(movement.isMovingToGoalTile())
		{
			if(movement.getX() == Direction.POSITIVE)
				playerSprite.setSpriteForward();
			if(movement.getX() == Direction.NEGATIVE)
				playerSprite.setSpriteBackward();
		}
		
		playerSprite.update(delta, camera, movement);
	}
	
	public Vector3 getPlayerWorldPosition()
	{
		return movement.getSpriteWorldCoords();
	}
	
	public boolean keyUp(int keycode) {
		
		if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
		{
			Inputs.left.isPressed = false;
			Inputs.left.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
		{
			Inputs.right.isPressed = false;
			Inputs.right.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
		{
			Inputs.up.isPressed = false;
			Inputs.up.timeSincePressed = 0.0;
		}else if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
		{
			Inputs.down.isPressed = false;
			Inputs.down.timeSincePressed = 0.0;
		}
		
		return false;
	}

	
	public boolean keyDown(int keycode) {
		
		if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
		{
			Inputs.left.isPressed = true;
		}else if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
		{
			Inputs.right.isPressed = true;
		}else if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
		{
			Inputs.up.isPressed = true;
		}else if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
		{
			Inputs.down.isPressed = true;
		}
		
		return false;
	}
	
	public boolean keyTyped(char character) {
		
		// toggle interaction with [space]
		if(character == ' ' && canInteract && !isInteracting) {
			canInteract = false;
			isInteracting = true;
			Inputs.space.isPressed = true;
		} else if (character == ' ' && isInteracting){
			Inputs.space.isPressed = false;
			Inputs.space.timeSincePressed = 0.0;
			isInteracting = false;
		}
		
		return false;
	}

	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}

	public void canInteract(boolean canInteract) {
		this.canInteract = canInteract;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public List<Quest> getQuests() {
		return quests;
	}

	public void addToQuests(Quest quest) {
		this.quests.add(quest);
	}

	public boolean isCloseToEnemy() {
		return closeToEnemy;
	}

	public void setCloseToEnemy(boolean closeToEnemy) {
		this.closeToEnemy = closeToEnemy;
	}

	public boolean isInteracting() {
		return isInteracting;
	}

	public void setInteracting(boolean isInteracting) {
		this.isInteracting = isInteracting;
	}

	public List<String> getCompletedQuests() {
		return completedQuests;
	}

	public void addToCompletedQuests(String completedQuest) {
		this.completedQuests.add(completedQuest);
	}

	public List<String> getAchievementFlags() {
		return achievementFlags;
	}

	public void addToAchievementFlags(String achievementFlag) {
		this.achievementFlags.add(achievementFlag);
	}

	public NPC getInteractableNPC() {
		return interactableNPC;
	}
	
	public void setInteractableNPC(NPC closestNPC) {
		this.interactableNPC = closestNPC;
	}

	public Inventory getInventory() {
		return inventory;
	}
}
