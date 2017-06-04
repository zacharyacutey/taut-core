package com.taut.game.models;

import java.util.ArrayList;
import java.util.List;

import com.taut.game.objects.GPIR;

/**
 * @author porgull
 * Interface for all things
 * that are able to fight 
 * i.e. enemies & player
 */

public interface Combatant {
	public GPIR gpir = new GPIR();
	List<GaryAbility> garyAbilities = new ArrayList<GaryAbility>();
	List<StandardAbility> standardAbilities = new ArrayList<StandardAbility>();
}
