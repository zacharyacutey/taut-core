package com.taut.game.models.quest;

import com.taut.game.objects.Player;

/**
 * @author 19smitgr
 * Providing basis
 * for defining a quest
 * condition
 */

public interface QuestConditionFunction {
	public boolean isSatisfied(Player player);
}
