package com.taut.game.models;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.taut.game.objects.TautSprite;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NPC {
    public String name;
    public String dialogue;
    public int spriteId;
    public String imageName;
    
    // placement
    int screenId;
    int[] coords = new int[2];
 
    // quests
    List<Quest> quests = new ArrayList<>();
    
    public Texture getTexture() {
    	return new Texture(Gdx.files.internal(imageName));
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

	public List<Quest> getQuests() {
		return quests;
	}

	public void setQuests(List<Quest> quests) {
		this.quests = quests;
	}
    
    
}