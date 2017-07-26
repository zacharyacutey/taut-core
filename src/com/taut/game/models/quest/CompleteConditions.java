package com.taut.game.models.quest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.taut.game.models.Item;
import com.taut.game.objects.Player;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteConditions {
	List<String> specialFlags;
	
	Item item;
	
	String talkedTo;
	JsonNode defeated;
	
	ArrayList<QuestConditionFunction> combinedActionsList = new ArrayList<>();
	
	public void fillCombinedActionsList() {
		// add player achievement flags
		for(int i = 0; i < specialFlags.size(); i++) {
			final int j = i;
			this.addToCombinedActionsList(new QuestConditionFunction() { public boolean isSatisfied(Player player) {
				return player.getAchievementFlags().contains(specialFlags.get(j));
			}});
		}
		
		// check if item with certain ID is in inventory
		if (item != null) {
			this.addToCombinedActionsList(new QuestConditionFunction() { public boolean isSatisfied(Player player) {
				return player.getInventory().hasItem(item.getID(), item.getItemType());
			}});
		}
		
		// TODO: implement a system to determine what the player has done since getting a quest including talkedTo and enemiesDefeated
	}

	public ArrayList<QuestConditionFunction> getCombinedActionsList() {
		return combinedActionsList;
	}
	public void setCombinedActionsList(ArrayList<QuestConditionFunction> combinedActionsList) {
		this.combinedActionsList = combinedActionsList;
	}
	private void addToCombinedActionsList(QuestConditionFunction condition) {
		this.combinedActionsList.add(condition);
	}
	public List<String> getSpecialFlags() {
		return specialFlags;
	}
	public void setSpecialFlags(List<String> specialFlags) {
		this.specialFlags = specialFlags;
	}
	public String getTalkedTo() {
		return talkedTo;
	}
	public void setTalkedTo(String talkedTo) {
		this.talkedTo = talkedTo;
	}
	public JsonNode getDefeated() {
		return defeated;
	}
	public void setDefeated(JsonNode defeated) {
		this.defeated = defeated;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}
