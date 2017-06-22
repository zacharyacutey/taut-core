package com.taut.game.models;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.taut.game.models.items.Armor;
import com.taut.game.models.items.ArmorGenerator;
import com.taut.game.models.items.Consumable;
import com.taut.game.models.items.ConsumableGenerator;
import com.taut.game.models.items.Weapon;
import com.taut.game.models.items.WeaponGenerator;
import com.taut.game.objects.FolderContents;

public class Inventory {
	public static ArrayList<Weapon> weapons = new ArrayList<>();
	public static WeaponGenerator weaponGenerator = new WeaponGenerator();
	public static FolderContents weaponFolderContents = new FolderContents("weapons");

	public static ArrayList<Consumable> consumables = new ArrayList<>();
	public static ConsumableGenerator consumableGenerator = new ConsumableGenerator();
	public static FolderContents consumableFolderContents = new FolderContents("consumables");
	
	static ArrayList<Armor> armor = new ArrayList<>();
	static ArmorGenerator armorGenerator = new ArmorGenerator();
	static FolderContents armorFolderContents = new FolderContents("armor");
	
	// id, amount
	public static Map<Integer, Integer> weaponAmounts = new HashMap<>();
	public static Map<Integer, Integer> armorAmounts = new HashMap<>();
	public static Map<Integer, Integer> consumableAmounts = new HashMap<>();
	
	public Inventory() {
		weaponFolderContents.generateAndStore(weaponGenerator, weapons);
		consumableFolderContents.generateAndStore(consumableGenerator, consumables);
		armorFolderContents.generateAndStore(armorGenerator, armor);
	}
	// fill id lookup table with all initial values at 0
	public static void fillItemTables(List<Weapon> weapons, List<Armor> armors, List<Consumable> consumables) {
		weapons.forEach(weapon -> {
			weaponAmounts.put(weapon.getID(), 0);
		});
		
		armors.forEach(armor -> {
			armorAmounts.put(armor.getID(), 0);
		});
		
		consumables.forEach(consumable -> {
			consumableAmounts.put(consumable.getID(), 0);
		});
	}
	
	public enum ItemType {
		WEAPON,
		ARMOR,
		CONSUMABLE
	}
	
	public void giveItem(int id, ItemType itemType) {
		int currentAmount;
		
		if (itemType.equals(ItemType.WEAPON)) {
			currentAmount = weaponAmounts.get(id);
			weaponAmounts.put(id, currentAmount+1);
		} else if (itemType.equals(ItemType.ARMOR)) {
			currentAmount = armorAmounts.get(id);
			armorAmounts.put(id, currentAmount+1);
		} else if (itemType.equals(ItemType.CONSUMABLE)) {
			currentAmount = consumableAmounts.get(id);
			consumableAmounts.put(id, currentAmount+1);
		}
	}
	
	public boolean hasItem(int id, ItemType itemType) {
		if (itemType.equals(ItemType.WEAPON)) {
			return weaponAmounts.get(id) > 0;
		} else if (itemType.equals(ItemType.ARMOR)) {
			return armorAmounts.get(id) > 0;
		} else if (itemType.equals(ItemType.CONSUMABLE)) {
			return consumableAmounts.get(id) > 0;
		}
		
		return false;
	}
	
	public void removeItem(int id, ItemType itemType) {
		int currentAmount;
		
		if (itemType.equals(ItemType.WEAPON)) {
			currentAmount = weaponAmounts.get(id);
			weaponAmounts.put(id, currentAmount-1);
		} else if (itemType.equals(ItemType.ARMOR)) {
			currentAmount = armorAmounts.get(id);
			armorAmounts.put(id, currentAmount-1);
		} else if (itemType.equals(ItemType.CONSUMABLE)) {
			currentAmount = consumableAmounts.get(id);
			consumableAmounts.put(id, currentAmount-1);
		}
	}
}
