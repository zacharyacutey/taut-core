package com.taut.game.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taut.game.models.Item;

/** 
 * @author Garrett
 * any types of clothing or armor
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Armor extends Item {
	private final ItemType itemType = ItemType.ARMOR;
	
	public int id;
	public String imagePath;
	
	// armor specific stuff
	public int defense;
	public enum BodyArea {HEAD, SHOULDERS, KNEES, TOES}
	
	@JsonCreator
    public Armor(@JsonProperty("id")int id) {
        super(id);
    }
	
	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public ItemType getItemType() {
		return itemType;
	}
}
