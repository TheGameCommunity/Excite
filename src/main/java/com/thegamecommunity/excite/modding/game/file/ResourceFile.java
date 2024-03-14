package com.thegamecommunity.excite.modding.game.file;

import java.nio.ByteBuffer;

public interface ResourceFile {
	
	public byte[] getHeader();
	
	public ByteBuffer getResourceBytes();
	
	public boolean isCompressed();
	
	public default ResourceDecompressor getDecompressor() {
		return new UncompressedDecompressor(this);
	};
	
	
	
}
