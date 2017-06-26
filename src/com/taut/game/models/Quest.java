package com.taut.game.models;

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
    String questName;
    QuestBeginCondition[] questConditions;
    //List<QuestCondition> questConditionsList = new ArrayList<>();
    CompleteAction[] completeActions;
    //List<CompleteAction> completeActionsList = new ArrayList<>();
    String questDialogue;
    
    boolean done;
    boolean started; 
}