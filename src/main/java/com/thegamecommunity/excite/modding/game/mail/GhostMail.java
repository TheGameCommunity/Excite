package com.thegamecommunity.excite.modding.game.mail;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.NotImplementedException;

public class GhostMail extends ExciteMail {

	public GhostMail(MimeMessage message) throws MessagingException, IOException {
		super(message);
		throw new NotImplementedException();
	}

}
