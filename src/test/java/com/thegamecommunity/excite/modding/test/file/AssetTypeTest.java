package com.thegamecommunity.excite.modding.test.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.thegamecommunity.excite.modding.game.file.AssetType;

class AssetTypeTest {
	
	@TestFactory
	List<DynamicTest> createAssetTypeTests() {
		ArrayList<DynamicTest> tests = new ArrayList<>();
		for(AssetType type : AssetType.values()) {
			tests.add(
				DynamicTest.dynamicTest("Verify AssetType " + type.name().toUpperCase(), () -> {
					assertEquals(AssetType.getAssetType("example." + type.toString()), type);
				})
			);
		}
		
		tests.add(DynamicTest.dynamicTest("Verify Unrecognized types - unknown extension", () -> {
			assertEquals(AssetType.getAssetType("example.xxx"), AssetType.UNRECOGNIZED);
		}));
		
		tests.add(DynamicTest.dynamicTest("Verify Unrecognized types - no extension", () -> {
			assertEquals(AssetType.getAssetType("example"), AssetType.UNRECOGNIZED);
		}));
		
		tests.add(DynamicTest.dynamicTest("Verify Unrecognized types - empty", () -> {
			assertEquals(AssetType.getAssetType(""), AssetType.UNRECOGNIZED);
		}));
		
		return tests;
	}
	
}
