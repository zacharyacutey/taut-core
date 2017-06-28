package com.taut.game.models.quest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.taut.game.models.Item;
import com.taut.game.objects.Player;

/**
 * @author Garrett
 * Stuff to give the player on quest completion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteActions {
	// to see stats/inventory/etc.
	Player player;
	
	// we can expand how this works later
	private String completeDialogue;
	
	// add to player stats/inventory
	private int garyPower;
	private Item item;
	
	private List<CompleteActionFunction> combinedActionsList = new ArrayList<>();
	
	// I realize player probably shouldn't be imported here, so if you want to refactor this, you can while I'm gone
	public void fillCombinedActionsList() {		
		// add action for NPC to say the dialogue
		this.addToCombinedActionsList((player) -> {
			player.getInteractableNPC().say(completeDialogue);
		}); 
		
		// add action for player to get more GP
		this.addToCombinedActionsList((player) -> {
			player.getStats().addGP(garyPower);
		});
		
		// add item with certain ID to player inventory
		if (item != null) {
			this.addToCombinedActionsList((player) -> {
				player.getInventory().giveItem(item.getID(), item.getItemType());
			});
		}
	}
	
	public List<CompleteActionFunction> getCombinedActionsList() {
		return combinedActionsList;
	}
	public void addToCombinedActionsList(CompleteActionFunction action) {
		this.combinedActionsList.add(action);
	}
	public String getCompleteDialogue() {
		return completeDialogue;
	}
	public void setCompleteDialogue(String completeDialogue) {
		this.completeDialogue = completeDialogue;
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
}
