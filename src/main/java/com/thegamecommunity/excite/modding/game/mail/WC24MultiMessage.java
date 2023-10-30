package com.thegamecommunity.excite.modding.game.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;

import com.sun.mail.handlers.multipart_mixed;

public class WC24MultiMessage extends Mail {

	private static final Pattern CD_PATTERN = Pattern.compile("cd=(?<cd>-?\\d{1,8})"); //cd=(?<pd>-?\d{1,8})
	private static final Pattern MSG_PATTERN = Pattern.compile("msg=(?<msg>.{0,256})"); //msg=(?<msg>.{0,256})
	private static final Pattern MAILNUM_PATTERN = Pattern.compile("mailnum=(?<mailnum>\\d{1,3})"); //mailnum=(?<mailnum>\d{1,3})
	private static final Pattern MAILSIZE_PATTERN = Pattern.compile("mailsize=(?<mailsize>\\d{1,8})");
	private static final Pattern ALLNUM_PATTERN = Pattern.compile("allnum=(?<allnum>\\d{1,3})"); //mailnum=(?<mailnum>\d{1,3})
	
	private static final Pattern[] PATTERNS = new Pattern[] {CD_PATTERN, MSG_PATTERN, MAILNUM_PATTERN, MAILSIZE_PATTERN, ALLNUM_PATTERN};
	
	private final int cd;
	private final String msg;
	private final int mailnum;
	private final int mailsize;
	private final int allnum;
	private final WiiMail[] messages;
	
	protected WC24MultiMessage(Part message) throws IOException, MessagingException {
		super(convertMessage(message));
		
		int cd = -1;
		String msg = "";
		int mailnum = 0;
		int mailsize = 0;
		int allnum = 0;
		
		
		MimeMultipart multipart = (MimeMultipart) message.getContent();
		
		{
			String content;
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				multipart.writeTo(baos);
				content = new String(baos.toByteArray());
			}
			
			Pattern pattern = PATTERNS[0];
			Matcher matcher = pattern.matcher(content);
			if(matcher.find()) {
				try {
					cd = Integer.parseInt(matcher.group("cd"));
				} catch(NumberFormatException e) {}
			}
			
			pattern = PATTERNS[1];
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				msg = matcher.group("msg");
			}
			
			pattern = PATTERNS[2];
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				try {
					mailnum = Integer.parseInt(matcher.group("mailnum"));
				} catch(NumberFormatException e) {}
			}
			
			pattern = PATTERNS[3];
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				try {
					mailsize = Integer.parseInt(matcher.group("mailsize"));
				} catch(NumberFormatException e) {}
			}
			
			pattern = PATTERNS[4];
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				try {
					allnum = Integer.parseInt(matcher.group("allnum"));
				} catch(NumberFormatException e) {}
			}
		}
		
		this.cd = cd;
		this.msg = msg;
		this.mailnum = mailnum;
		this.mailsize = mailsize;
		this.allnum = allnum;
		checkValid();
		
		WiiMail[] messages = new WiiMail[mailnum];
		for(int i = 0; i < mailnum; i++) {
			BodyPart part = multipart.getBodyPart(i);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			part.writeTo(baos);
			String content = new String(baos.toByteArray());
			content = content.substring(content.indexOf("\r\n") + 2);
			content = content.substring(content.indexOf("\r\n") + 2);
			messages[i] = (WiiMail) Mail.getMail(new MimeMessage(null, IOUtils.toInputStream(content)));
		}
		this.messages = messages;
	}
	
	public WiiMail getMessage(int index) throws MessagingException, IOException {
		BodyPart part = getMultipart().getBodyPart(index);
		part.writeTo(null);
		return (WiiMail) Mail.getMail(getMultipart().getBodyPart(index));
	}
	
	public List<WiiMail> getMessages() {
		return List.of(messages);
	}
	
	public MimeMultipart getMultipart() throws IOException, MessagingException {
		return (MimeMultipart) message.getContent();
	}
	
	public void checkValid() throws MessagingException {
		if(cd != 100) {
			throw new MessagingException("cd is not valid (got " + cd + ") expected 100");
		}
		if(!msg.equals("Success.")) {
			throw new MessagingException("Non-successful status message. Got (" + msg + ")");
		}
		if(mailnum < 1) {
			throw new MessagingException("Less than one mail object: " + mailnum);
		}
		if(mailnum > 999) {
			throw new MessagingException("More than 999 mail objects! " + mailnum);
		}
		if(mailsize < 0) {
			throw new MessagingException("Mail size less than 1: " + mailsize);
		}
		if(mailsize > 99999999) {
			throw new MessagingException("Mail size greater than 99999999: " + mailsize);
		}
	}
	
	private static Part convertMessage(Part message) throws MessagingException, IOException {
		String boundary = message.getAllHeaders().nextElement().getName().substring(2);
		message.setHeader("Content-Type", "multipart/mixed;\n boundary=\"" + boundary + "\"");
		multipart_mixed mix = new multipart_mixed();
		MimeMultipart multipart = (MimeMultipart) mix.getContent(message.getDataHandler().getDataSource());
		multipart.getParent().setContent(multipart);
		return multipart.getParent();
	}
	
}
