package com.taut.game.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 19smitgr
 * Class defining quest
 * objectives and its
 * completion status
 *
 */

public class Quest {
    String name;
    List<QuestCondition> questConditions = new ArrayList<>(); 
    List<CompleteAction> completeActions = new ArrayList<>();
    String beginDialogue;
    
    boolean done;
    boolean started; 
}