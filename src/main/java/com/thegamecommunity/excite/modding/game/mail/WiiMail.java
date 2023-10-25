package com.thegamecommunity.excite.modding.game.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class WiiMail extends Mail {

	protected final String appID;
	
	protected WiiMail(MimeMessage message) throws MessagingException {
		super(message);
		this.appID = message.getHeader(Mail.APP_ID_HEADER)[0];
	}
	
	public String getAppID() {
		return appID;
	}

}
