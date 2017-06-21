package com.taut.game.models.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.taut.game.models.Item;

/** 
 * @author Garrett
 * any types of clothing or armor
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Armor extends Item {
	public int id;
	public String imagePath;
	
	// armor specific stuff
	public int defense;
	public enum BodyArea {HEAD, SHOULDERS, KNEES, TOES}
	
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

}
