package com.taut.game.models;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    String name;
    List<QuestCondition> questConditions = new ArrayList<>(); 
    //List<CompleteAction> completeActions = new ArrayList<>();
    String beginDialogue;
    
    boolean done;
    boolean started; 
}