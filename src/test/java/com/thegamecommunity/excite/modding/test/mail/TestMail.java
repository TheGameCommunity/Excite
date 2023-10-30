package com.thegamecommunity.excite.modding.test.mail;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.thegamecommunity.excite.modding.game.mail.ExciteMail;
import com.thegamecommunity.excite.modding.game.mail.Mail;
import com.thegamecommunity.excite.modding.game.mail.WC24MultiMessage;
import com.thegamecommunity.excite.modding.game.mail.WiiMail;
import com.thegamecommunity.excite.modding.game.propritary.crc.CRCTester;

class TestMail {

	static {
		try {
			System.out.println(new File(".").getCanonicalPath());
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	private static final File RESOURCES_DIR = new File("./src/test/resources/com/thegamecommunity/excite/modding/test/mail");
	private static final ArrayList<File> RESOURCES = new ArrayList<>(Arrays.asList(RESOURCES_DIR.listFiles()));
	private static final LinkedHashMap<File, WC24MultiMessage> MULTI_MAIL = new LinkedHashMap<>();
	
	@BeforeAll
	static void checkTestFilesExist() throws IOException {
		RESOURCES.removeIf((f) -> f.isDirectory());
		System.out.println("Found " + RESOURCES.size() + " mail files to check:\n");
		for(File f : RESOURCES) {
			System.out.println(f.getCanonicalPath());
		}
		assertTrue(RESOURCES.size() > 0);
	}
	
	@TestFactory
	List<DynamicTest> testMail() throws IOException, MessagingException {
		ArrayList<DynamicTest> tests = new ArrayList<>();
		for(File f : RESOURCES) {
			tests.add(DynamicTest.dynamicTest("Check mail " + f.getName(), () -> {
				testMail(f);
			}));
		}
		return tests;
	}
	
	@TestFactory
	List<DynamicTest> testMultimail() {
		ArrayList<DynamicTest> tests = new ArrayList<>();
		Assertions.assertNotEquals(0, MULTI_MAIL.size());
		for(Entry<File, WC24MultiMessage> multimailEntry : MULTI_MAIL.entrySet()) {
			File f = multimailEntry.getKey();
			List<WiiMail> mail = multimailEntry.getValue().getMessages();
			for(int i = 0; i < mail.size(); i++) {
				final int j = i;
				tests.add(DynamicTest.dynamicTest("Checking part " + j  + " of " + f, () -> {
					testMail(mail.get(j));
				}));
			}
		}
		return tests;
	}
	
	private static void testMail(File f) throws FileNotFoundException, MessagingException, IOException {
		Mail mail = Mail.getMail(f);
		Assumptions.assumeFalse(mail.getClass() == WiiMail.class);
		if(mail instanceof ExciteMail) {
			testMail((ExciteMail)mail);
		}
		else if(mail instanceof WC24MultiMessage) {
			MULTI_MAIL.put(f, (WC24MultiMessage) mail);
		}
		else {
			throw new AssertionError();
		}
	}
	
	private static void testMail(ExciteMail mail) throws IOException, MessagingException {
		ExciteMail exMail = (ExciteMail) mail;
		Part message = mail.getMessage();
		MimeMultipart m = (MimeMultipart) message.getContent();
		System.out.println("Part Count: " + m.getCount());
		byte[] bytes = ((InputStream)m.getBodyPart(1).getContent()).readAllBytes();
		//System.out.println(new String(bytes));
		assertEquals(new CRCTester((InputStream) m.getBodyPart(1).getContent()).test(), exMail.getCRC());
	}
	
	private static void testMail(WiiMail mail) {
		
	}
	
}
