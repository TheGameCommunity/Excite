package com.thegamecommunity.excite.modding.game.mail;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public abstract class Mail {

	public static final String APP_ID_HEADER = "X-Wii-AppId";
	
	public static final String EXCITEBOTS_ID = "1-52583345";
	
	protected MimeMessage message;
	
	protected Mail(MimeMessage message) {
		this.message = message;
	}
	
	public final MimeMessage getMimeMessage() {
		return message;
	}
	
	public static Mail getMail(MimeMessage message) throws MessagingException, IOException {
		String[] appID = message.getHeader(APP_ID_HEADER);
		if(appID == null || appID.length != 1) {
			return new NonWiiMail(message);
		}
		else {
			String id = appID[0];
			System.out.println(id);
			if(id.startsWith(EXCITEBOTS_ID)) {
				return new ExciteMail(message);
			}
			else {
				return new WiiMail(message);
			}
		}
	}
	
}
