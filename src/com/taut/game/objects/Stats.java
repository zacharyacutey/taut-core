package com.taut.game.objects;

public class Stats {
	private int hp;
	private int gp;
	
	// GPIR stuff would be inserted here
	private float speed;

	
	// functions to mutate the stats
	public int getHP() {
		return hp;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}
	
	public void addHP(int hp) {
		this.hp += hp;
	}

	public int getGP() {
		return gp;
	}

	public void setGP(int gp) {
		this.gp = gp;
	}
	
	public void addGP(int garyPower) {
		this.gp += garyPower;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
