package de.pfannekuchen.lotas.utils;

import java.util.Random;

public class FakeRandom extends Random {
	private static final long serialVersionUID = 3700871326855463177L;

	@Override
	public float nextFloat() {
		if (new Random().nextFloat() < (ConfigManager.getInt("hidden", "explosionoptimization") / 100F)) return 0;
		else return 1;
	}
	
}