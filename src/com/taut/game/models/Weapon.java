package com.taut.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/** 
 * @author Garrett
 * anything related to attacking
 */
public class Weapon extends Item {
	int id;
	int damageMin;
	int damageMax;
	String imagePath;
	
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
}
