package gameworld;

import java.util.Random;

public enum WorldStates {

	OCCUPIED(0), EMPTY(1), ENEMY(20), AI(0), MY_FLAG(2), ENEMY_FLAG(3), FLAG(-40);

	private int code;

	private WorldStates(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static WorldStates random(Random random) {
		if(random.nextBoolean())
			return EMPTY;
		return OCCUPIED;
	}

	public static WorldStates getState(int i) {

		for(WorldStates worldState:values()) {
			if(worldState.getCode() == i)
				return worldState;
		}
		
		return null;
	}

}
