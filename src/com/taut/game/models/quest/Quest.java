package com.taut.game.models.quest;

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
    private BeginConditions beginConditions;
    private CompleteConditions completeConditions;
    private CompleteActions completeActions;
    
    private boolean done = false;
    private boolean started = false;
    
	public String getQuestName() {
		return questName;
	}
	public void setQuestName(String questName) {
		this.questName = questName;
	}
	public BeginConditions getBeginConditions() {
		return beginConditions;
	}
	public void setBeginConditions(BeginConditions beginConditions) {
		this.beginConditions = beginConditions;
	}
	public CompleteActions getCompleteActions() {
		return completeActions;
	}
	public void setCompleteActions(CompleteActions completeActions) {
		this.completeActions = completeActions;
	}
	public CompleteConditions getCompleteConditions() {
		return completeConditions;
	}
	public void setCompleteConditions(CompleteConditions completeConditions) {
		this.completeConditions = completeConditions;
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