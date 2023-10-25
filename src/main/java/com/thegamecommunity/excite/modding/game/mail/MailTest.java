package com.thegamecommunity.excite.modding.game.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.thegamecommunity.excite.modding.game.propritary.crc.CRCTester;

public class MailTest {

	private static final File f = new File("./src/test/resources/com/thegamecommunity/excite/modding/test/mail/Aug 6, 2022, 2:05:50 AM(1).email");
	
	public static void main(String[] args) throws MessagingException, IOException {
		Mail mail = Mail.getMail(new MimeMessage(null, new FileInputStream(f)));
		if(mail instanceof ExciteMail) {
			System.out.println("yay");
			MimeMessage message = mail.getMimeMessage();
			System.out.println(message.getClass().getCanonicalName());
			MimeMultipart m = (MimeMultipart) message.getContent();
			System.out.println("Part Count: " + m.getCount());
			for(int i = 0; i < m.getCount(); i++) {
				System.out.println(m.getBodyPart(i).getContent());
				System.out.println(m.getBodyPart(i).getClass().getCanonicalName());
				System.out.println();
			}

			System.out.println(m.getBodyPart(1).getContent().getClass().getCanonicalName());
			byte[] bytes = ((InputStream)m.getBodyPart(1).getContent()).readAllBytes();
			System.out.println(new String(bytes));
			System.out.println(Integer.toHexString(new CRCTester((InputStream) m.getBodyPart(1).getContent()).test()));
		}
		else {
			System.out.println("nay");
		}
	}
	
}
