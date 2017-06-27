package com.taut.game.models.quest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.taut.game.models.Item;

/**
 * @author Garrett
 * Conditions to check before giving a quest
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeginConditions  {
	// TODO: make complete conditions not just be strings
	private ArrayList<String> specialFlags;
	private int garyPower;
	
	// stuff just for the json parser
	private Item item;
	
	private List<QuestConditionFunction> questConditionFunctions = new ArrayList<>();
	
	// I realize player probably shouldn't be imported here, so if you want to refactor this, you can while I'm gone
	// must be called after createItem()
	public void fillCombinedActionsList() {
		// check for player flags
		specialFlags.forEach(condition -> {
			this.addToQuestConditionFunctions((player) -> {
				return player.getAchievementFlags().contains(condition);
			});
		});
		
		// add action for player to get more GP
		this.addToQuestConditionFunctions((player) -> {
			return player.getStats().getGP() >= this.garyPower;
		});
		
		// add item with certain ID to player inventory
		this.addToQuestConditionFunctions((player) -> {
			return player.getInventory().hasItem(item.getID(), item.getItemType());
		});
	}
	
	public List<QuestConditionFunction> getQuestConditionFunctions() {
		return questConditionFunctions;
	}
	public void addToQuestConditionFunctions(QuestConditionFunction condition) {
		this.questConditionFunctions.add(condition);
	}
	public ArrayList<String> getSpecialFlags() {
		return specialFlags;
	}
	public void setSpecialFlags(ArrayList<String> specialFlags) {
		this.specialFlags = specialFlags;
	}
	public int getItemID() {
		return item.getID();
	}
	public void setItemID(int itemID) {
		this.item.setID(itemID);
	}
	public int getGaryPower() {
		return garyPower;
	}
	public void setGaryPower(int garyPower) {
		this.garyPower = garyPower;
	}

//	@Override
//	public boolean isSatisfied(Player player) {
//		return false;
//	}
}
