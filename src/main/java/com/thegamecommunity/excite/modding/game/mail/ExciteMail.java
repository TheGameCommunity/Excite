package com.thegamecommunity.excite.modding.game.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;

public class ExciteMail extends WiiMail {

	// ver=(?<ver>\d{1,8}) type=(?<type>[0-9a-fA-F]{1,8}) crc=(?<crc>[0-9a-fA-F]{1,8})
	public static final Pattern CHECK_PATTERN = Pattern.compile("ver=(?<ver>\\d{1,8}) type=(?<type>[0-9a-fA-F]{1,8}) crc=(?<crc>[0-9a-fA-F]{1,8})");
	
	protected final int version;
	protected final int type;
	protected final int crc;
	
	public ExciteMail(Part message) throws MessagingException, IOException {
		super(message);
		if(!this.getAppID().startsWith(Mail.EXCITEBOTS_ID)) {
			throw new AssertionError();
		}
		String checkData = (String) getCheckPart().getContent();
		System.out.println(checkData);
		Matcher matcher = CHECK_PATTERN.matcher(checkData);
		matcher.find();
		version = Integer.parseInt(matcher.group("ver"));
		type = Integer.parseUnsignedInt(matcher.group("type"), 16);
		crc = Integer.parseUnsignedInt(matcher.group("crc"), 16);
	}
	
	public MimeMultipart getMultiPart() throws IOException, MessagingException {
		return (MimeMultipart) message.getContent();
	}
	
	public BodyPart getCheckPart() throws MessagingException, IOException {
		return getMultiPart().getBodyPart(0);
	}
	
	public BodyPart getDataPart() throws MessagingException, IOException {
		return getMultiPart().getBodyPart(1);
	}
	
	public final int getVersion() {
		return version;
	}
	
	public final int getType() {
		return type;
	}
	
	public final int getCRC() {
		return crc;
	}
	
	public final byte[] getDecodedData() throws IOException, MessagingException {
		return ((InputStream)getDataPart().getContent()).readAllBytes();
	}
	
	public final byte[] getEncodedData() throws MessagingException, IOException {
		BodyPart dataPart = getDataPart();
		String contentType = dataPart.getContentType();
		String encoding = dataPart.getHeader("Content-Transfer-Encoding")[0];
		dataPart.setHeader("Content-Type", "text/plain; charset=us-ascii");
		dataPart.setHeader("Content-Transfer-Encoding", "7bit");
		byte[] ret = ((InputStream)getDataPart().getContent()).readAllBytes();
		dataPart.setHeader("Content-Type", contentType);
		dataPart.setHeader("Content-Transfer-Encoding", encoding);
		return ret;
	}

}
