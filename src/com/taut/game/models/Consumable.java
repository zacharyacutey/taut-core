package com.taut.game.models;

/** 
 * @author Garrett
 *	anything related to eating food, potions, etc.
 */
public class Consumable extends Item {
	public int id;
	public String imagePath;
	
	// consumable specific stuff
	public int hpToRestore;
	public int gpToRestore;
	
	// we'll continually add to this list
	public enum BuffTypes {HEALING, DEFENSE, SPEED, ATTACK} 

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

}
