package com.taut.game.models;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.taut.game.models.quest.Quest;
import com.taut.game.objects.TautSprite;

/**
 * @author 19smitgr
 * Class defining NPCs
 * and their behavior
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class NPC {
    public String name;
    public String dialogue;
    public int spriteId;
    public String imageName;
    public Texture texture;
    public double distanceToPlayer;
    
    // placement
    public int screenId;
    public int[] coords = new int[2];
 
    // quests
    public ArrayList<Quest> quests = new ArrayList<Quest>(); 
    
    public Texture getTexture() {
    	return texture;
    }
    
    public void setTexture() {
    	this.texture = new Texture(Gdx.files.internal(imageName));
    }
    
    public TautSprite getSprite(){
    	return new TautSprite(getTexture());
    }

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDialogue() {
		return dialogue;
	}

	public void setDialogue(String dialogue) {
		this.dialogue = dialogue;
	}

	public int getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public int[] getCoords() {
		return coords;
	}

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public Vector3 getCoordsInVector3() {
		return new Vector3(coords[0], coords[1], 0f);
	}

	public void setCoordsInVector3(Vector3 coords) {
		this.coords[0] = (int) coords.x;
		this.coords[1] = (int) coords.y;
	}
	
	/**
	 * the JSON parser only maps onto normal arrays, but an arraylist is more convenient, hence 2 different lists for the quests
	 */
	public ArrayList<Quest> getQuests() {
		return quests;
	}

	public void setQuests(ArrayList<Quest> quests) {
		this.quests = quests;
	}

	public void setDistanceToPlayer(double distance) {
		this.distanceToPlayer = distance;
	}
    
	public double getDistanceToPlayer() {
		return this.distanceToPlayer;
	}

	public void say(String dialogue) {
		// TODO: add UI to this
		System.out.println(dialogue);
	}
    
}