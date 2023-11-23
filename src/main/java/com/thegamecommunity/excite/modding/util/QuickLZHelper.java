package com.thegamecommunity.excite.modding.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.quicklz.QuickLZ;

public class QuickLZHelper {

	private static final Logger LOGGER = Logger.getLogger("QuickLZHelper");
	private static boolean AVAILABLE = false;
	static {
		try {
			Class.forName("com.quicklz.QuickLZ");
			LOGGER.log(Level.INFO, "QuickLZ loaded");
			AVAILABLE = true;
		} catch (ClassNotFoundException e) {
			NoClassDefFoundError noDefErr = new NoClassDefFoundError();
			noDefErr.initCause(e);
			LOGGER.log(Level.SEVERE, "Unable to load QuickLZ", noDefErr);
		}
	}
	
	public static boolean isAvailable() {
		return AVAILABLE;
	}
	
	public static byte[] compress(byte[] source, int level) {
		return QuickLZ.compress(source, level);
	}
	
	public static byte[] decompress(byte[] source) {
		return QuickLZ.decompress(source);
	}
	
	public static long sizeCompressed(byte[] source) {
		return QuickLZ.sizeCompressed(source);
	}
	
	public static long sizeDecompressed(byte[] source) {
		return QuickLZ.sizeDecompressed(source);
	}
	
}
