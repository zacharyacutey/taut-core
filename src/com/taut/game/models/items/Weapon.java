package com.taut.game.models.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taut.game.models.Item;

/** 
 * @author Garrett
 * anything related to attacking
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weapon extends Item {
	private final ItemType itemType = ItemType.WEAPON;
	
	int id;
	int damageMin;
	int damageMax;
	String imagePath;
	
	@JsonCreator
    public Weapon(@JsonProperty("id")int id) {
        super(id);
    }
	
    public Texture getTexture() {
    	return new Texture(Gdx.files.internal(imagePath));
    }

	public int getDamageMin() {
		return damageMin;
	}

	public void setDamageMin(int damageMin) {
		this.damageMin = damageMin;
	}

	public int getDamageMax() {
		return damageMax;
	}

	public void setDamageMax(int damageMax) {
		this.damageMax = damageMax;
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
