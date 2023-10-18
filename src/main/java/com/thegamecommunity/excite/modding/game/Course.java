package com.thegamecommunity.excite.modding.game;

public class Course {

	public final Cup cup;
	public String name;
	
	public Course(Cup cup, String name) {
		this.cup = cup;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return cup + " " + name;
	}
	
	public String toFullString() {
		if(cup.isMode(Mode.SUPER_EXCITE)) {
			return cup + " cup: " + name;
		}
		else {
			return cup + ": " + name;
		}
	}
	
	public boolean acceptingChallenges() {
		if(cup.isTutorial() || cup.isMode(Mode.POKER) || cup.isMode(Mode.MINIGAME)) { //the game can send replays and challenges for poker and minigames, but the bot doesn't support this for now.
			return false;
		}
		return true;
	}
	
	public static Course fromString(String courseString) {
		Cup cup = Cup.fromString(courseString);
		for(Course course : cup.getCourses()) {
			if(courseString.contains(course.toString())) {
				return course;
			}
		}
		throw new IllegalArgumentException("Unknown Course: " + courseString);
	}
	
}
