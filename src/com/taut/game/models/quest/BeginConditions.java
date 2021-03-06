package com.taut.game.models.quest;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.taut.game.models.Item;
import com.taut.game.objects.Player;

/**
 * @author Garrett
 * Conditions to check before giving a quest
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeginConditions  {
	// TODO: potentially make flags not just be strings
	private ArrayList<String> specialFlags;
	private int garyPower;
	private Item item;
	private List<QuestConditionFunction> questConditionFunctions = new ArrayList<>();
	
	public void fillCombinedActionsList() {
		// check for player flags
		for(int i = 0; i < specialFlags.size(); i++) {
			final int j = i;
			this.addToQuestConditionFunctions(new QuestConditionFunction() {public boolean isSatisfied(Player player) {
				return player.getAchievementFlags().contains(specialFlags.get(j));
			}});
		}
		
		// add action for player to get more GP
		this.addToQuestConditionFunctions(new QuestConditionFunction() {public boolean isSatisfied(Player player) {
			return player.getStats().getGP() >= garyPower;
		}});
		
		// check if item with certain ID is in inventory
		if (item != null) {
			this.addToQuestConditionFunctions(new QuestConditionFunction() {public boolean isSatisfied(Player player) {
				return player.getInventory().hasItem(item.getID(), item.getItemType());
			}});
		}
		
		System.out.println(item);
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
	public int getGaryPower() {
		return garyPower;
	}
	public void setGaryPower(int garyPower) {
		this.garyPower = garyPower;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	

//	@Override
//	public boolean isSatisfied(Player player) {
//		return false;
//	}
}
