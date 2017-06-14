package com.taut.game.models;

import java.util.HashMap;
import java.util.Map;

import com.taut.game.models.items.Weapon;

public class Inventory {
	// id, amount
	public Map<Integer, Integer> weaponAmounts = new HashMap<>();
	// id lookup table for every weapon
	public static Map<Integer, Weapon> weaponTable = new HashMap<>(); 
	
	// fill id lookup table with all initial values at 0
//	public static void fillWeaponTable() {
//		ArrayList<NPC> npcs = new ArrayList<>();
//		
//		
//		
//		
//		return npcs;
//	}
	
	public void getItem(Item item) {
		
	}
	
	public void hasItem(Item item) {
		
	}
	
	public void removeItem(Item item) {
		
	}
}
