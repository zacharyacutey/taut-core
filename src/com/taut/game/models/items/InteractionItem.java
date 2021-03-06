package com.taut.game.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taut.game.models.Item;

public class InteractionItem extends Item {
	private final ItemType itemType = ItemType.INTERACTIONITEM;

	@JsonCreator
    public InteractionItem(@JsonProperty("id")int id) {
        super(id);
    }
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setID(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setImagePath(String imagePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemType getItemType() {
		return itemType;
	}

}
