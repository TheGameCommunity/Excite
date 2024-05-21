package com.thegamecommunity.excite.modding.test.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.thegamecommunity.excite.modding.game.file.AssetType;

class AssetTypeTest {
	@Test
	void testAssetTypes() {
		for(AssetType type : AssetType.values()) {
			assertTrue(AssetType.getAssetType("example." + type.toString()) == type);
		}
	}
}
