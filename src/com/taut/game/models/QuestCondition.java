package com.taut.game.models;

import com.taut.game.objects.Player;

/**
 * @author 19smitgr
 * Providing basis
 * for defining a quest
 * condition
 */

public interface QuestCondition {
	public void isSatisfied(Player player);
}
