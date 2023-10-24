package com.thegamecommunity.excite.modding.test.crc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.thegamecommunity.excite.modding.game.propritary.crc.CRCTester;

class TestCRC {

	static {
		try {
			System.out.println(new File(".").getCanonicalPath());
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	private static final File RESOURCES_DIR = new File("./src/test/resources/com/thegamecommunity/excite/modding/test/crc");
	private static final ArrayList<File> RESOURCES = new ArrayList<>(Arrays.asList(RESOURCES_DIR.listFiles()));
	
	@BeforeAll
	static void checkTestFilesExist() throws IOException {
		for(File f : RESOURCES) {
			if(f.isDirectory()) {
				RESOURCES.remove(f);
			}
		}
		System.out.println("Found " + RESOURCES.size() + " resources to CRC:\n");
		for(File f : RESOURCES) {
			System.out.println(f.getCanonicalPath());
		}
		assertTrue(RESOURCES.size() > 0);
	}
	
	@TestFactory
	List<DynamicTest> createCRCTests() {
		ArrayList<DynamicTest> tests = new ArrayList<>();
		for(File f : RESOURCES) {
			if(f.getName().endsWith(".mail")) { //mail data is crc'd in base64, so we must use base64 decoder
				tests.add(DynamicTest.dynamicTest("CRC " + f.getName(), () -> assertEquals(Integer.parseUnsignedInt(f.getName(), 16), new CRCTester(new FileInputStream(f)).test())));
			}
			else { //all other resources
				tests.add(DynamicTest.dynamicTest("CRC " + f.getName(), () -> assertEquals(Integer.parseUnsignedInt(f.getName(), 16), CRCTester.test(new FileInputStream(f).readAllBytes()))));
			}
		}
		return tests;
	}

}
