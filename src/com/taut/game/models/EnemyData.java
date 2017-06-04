package com.taut.game.models;

public class EnemyData {

	public EnemyData(){} // TODO: add constructor which builds class from JSON file
	
	public static class FightTrigger
	{
		public static final FightTrigger beside = new FightTrigger(0);
		public static final FightTrigger interaction = new FightTrigger(1);
		public static final FightTrigger sameScreen = new FightTrigger(2);
		
		int value;
		protected FightTrigger(int value)
		{
			this.value = value;
		}
	}
	
}
