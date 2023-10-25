package com.thegamecommunity.excite.modding.test.crc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.thegamecommunity.excite.modding.game.mail.ExciteMail;
import com.thegamecommunity.excite.modding.game.mail.Mail;
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
		RESOURCES.removeIf((f) -> f.isDirectory());
		System.out.println("Found " + RESOURCES.size() + " resources to CRC:\n");
		for(File f : RESOURCES) {
			System.out.println(f.getCanonicalPath());
		}
		assertTrue(RESOURCES.size() > 0);
	}
	
	@TestFactory
	List<DynamicTest> createCRCTests() throws FileNotFoundException, IOException {
		ArrayList<DynamicTest> tests = new ArrayList<>();
		for(File f : RESOURCES) {
			String name = FilenameUtils.removeExtension(f.getName());
			if(f.getName().endsWith(".email")) { //mail data is crc'd in base64, so we must use base64 decoder
				tests.add(
					DynamicTest.dynamicTest("CRC " + f.getName(), 
						() -> {
							System.out.println("CRCing mail " + f.getName());
							Mail mail = (Mail) Mail.getMail(new MimeMessage(null, new FileInputStream(f)));
							Assumptions.assumeTrue(mail instanceof ExciteMail);

							ExciteMail exMail = (ExciteMail) mail;
							assertEquals(exMail.getCRC(), new CRCTester(exMail.getDataPart().getInputStream()).test());
						}
					)
				);
			}
			else { //all other resources
				CRCTester tester = new CRCTester(new FileInputStream(f).readAllBytes());
				tests.add(
					DynamicTest.dynamicTest("CRC " + f.getName(), 
						() -> {
							System.out.println("CRCing file " + f.getName());
							assertEquals(Integer.parseUnsignedInt(name, 16), tester.test());
						}
					)
				);
			}
		}
		return tests;
	}

}
