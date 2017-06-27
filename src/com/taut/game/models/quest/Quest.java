package com.taut.game.models.quest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author 19smitgr
 * Class defining quest
 * objectives and its
 * completion status
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quest {
    private String questName;
    private String questDialogue;
    
    // converting JSON object to our custom classes
    private BeginConditions questBeginConditions;
    private CompleteActions completeActions;
    
    private boolean done = false;
    private boolean started;
    
	public String getQuestName() {
		return questName;
	}
	public void setQuestName(String questName) {
		this.questName = questName;
	}
	public BeginConditions getQuestConditions() {
		return questBeginConditions;
	}
	public void setQuestConditions(BeginConditions questConditions) {
		this.questBeginConditions = questConditions;
	}
	public CompleteActions getCompleteActions() {
		return completeActions;
	}
	public void setCompleteActions(CompleteActions completeActions) {
		this.completeActions = completeActions;
	}
	public String getQuestDialogue() {
		return questDialogue;
	}
	public void setQuestDialogue(String questDialogue) {
		this.questDialogue = questDialogue;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	public boolean isStarted() {
		return started;
	}
	public void setStarted(boolean started) {
		this.started = started;
	} 
}