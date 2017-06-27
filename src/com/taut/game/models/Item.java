package com.taut.game.models;

public abstract class Item {
	ItemType itemType;
	
	public abstract int getID();
	public abstract void setID(int id);

	public abstract String getImagePath();
	public abstract void setImagePath(String imagePath);
	
	public enum ItemType {
		WEAPON,
		ARMOR,
		CONSUMABLE
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	
	public ItemType getItemType() {
		return itemType;
	}
}
