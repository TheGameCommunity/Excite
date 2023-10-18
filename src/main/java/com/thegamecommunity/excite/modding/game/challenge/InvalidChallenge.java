package com.thegamecommunity.excite.modding.game.challenge;

public final class InvalidChallenge implements Rewardable {

	public static final InvalidChallenge INSTANCE = new InvalidChallenge();

	private InvalidChallenge() {}
	
	@Override
	public int getReward() {
		return 0;
	}
	
}
