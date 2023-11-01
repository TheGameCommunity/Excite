package com.thegamecommunity.excite.modding.game.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;

public abstract class Mail {

	public static final String APP_ID_HEADER = "X-Wii-AppId";
	
	public static final String EXCITEBOTS_ID = "1-52583345";
	
	protected Part message;
	
	protected Mail(Part message) {
		this.message = message;
	}
	
	public Part getMessage() {
		return message;
	}
	
	public static Mail getMail(File f) throws FileNotFoundException, MessagingException, IOException {
		return getMail(new FileInputStream(f));
	}
	
	public static Mail getMail(InputStream i) throws MessagingException, IOException {
		Mail mail = getMail(new MimeMessage(null, i));
		i.close();
		return mail;
	}
	
	public Message getAsMessage() {
		return (Message)this.message;
	}
	
	public static Mail getMail(Part message) throws MessagingException, IOException {
		String[] appID = message.getHeader(APP_ID_HEADER);
		if(appID == null || appID.length != 1) {
			if(message.getContentType().equals("text/plain")) {
				Object contentObj = message.getContent();
				if(contentObj instanceof String) {
					String content = (String)contentObj;
					if(((String) contentObj).startsWith("This part is ignored.")) {
						return new WC24MultiMessage(message);
					}
				}
			}
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
