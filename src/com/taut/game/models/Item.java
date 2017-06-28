package com.taut.game.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.taut.game.models.items.Armor;
import com.taut.game.models.items.Consumable;
import com.taut.game.models.items.Weapon;

@JsonTypeInfo(use = Id.NAME,
include = JsonTypeInfo.As.PROPERTY,
property = "type")
@JsonSubTypes({
	@Type(value = Weapon.class),
	@Type(value = Consumable.class),
	@Type(value = Armor.class),
})
public abstract class Item {
	ItemType itemType;
	
	public Item(int id, ItemType itemType) {
		this.setID(id);
		this.setItemType(itemType);
	}
	
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
