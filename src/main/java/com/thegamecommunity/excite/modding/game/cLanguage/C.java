package com.thegamecommunity.excite.modding.game.cLanguage;

public class C {

	public static @Unsigned int toUnsignedByte(@Signed byte b) {
		return b & 0xFF;
	}
	
}
