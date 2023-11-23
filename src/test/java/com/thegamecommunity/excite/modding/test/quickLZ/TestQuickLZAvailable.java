package com.thegamecommunity.excite.modding.test.quickLZ;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.thegamecommunity.excite.modding.util.QuickLZHelper;

public class TestQuickLZAvailable {

	@Test
	public void testQuickLZAvailable() {
		assertTrue(QuickLZHelper.isAvailable());
	}
	
}
