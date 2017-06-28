package com.taut.game.models.items;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taut.game.models.Item;
import com.taut.game.models.items.consumables.Buff;

/** 
 * @author Garrett
 *	anything related to eating food, potions, etc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Consumable extends Item {
	private final ItemType itemType = ItemType.CONSUMABLE;
	
	public int id;
	public String imagePath;
	
	// consumable specific stuff
	public int hpToRestore;
	public int gpToRestore;
	
	// to be populated by JSON Mapper
	public List<String> buffStrings = new ArrayList<>();
	// will be populated after JSON is read
	public List<Buff> buffs = new ArrayList<>();
	
	// we'll continually add to this list
	public enum BuffTypes {HEALING, DEFENSE, SPEED, ATTACK} 
	
	@JsonCreator
    public Consumable(@JsonProperty("id")int id) {
        super(id);
    }

	public int getHpToRestore() {
		return hpToRestore;
	}

	public void setHpToRestore(int hpToRestore) {
		this.hpToRestore = hpToRestore;
	}

	public int getGpToRestore() {
		return gpToRestore;
	}

	public void setGpToRestore(int gpToRestore) {
		this.gpToRestore = gpToRestore;
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
