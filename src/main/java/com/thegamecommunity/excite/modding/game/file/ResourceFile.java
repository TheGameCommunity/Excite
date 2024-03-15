package com.thegamecommunity.excite.modding.game.file;

import java.nio.ByteBuffer;

public interface ResourceFile {
	
	public ByteBuffer getRawBytes();
	
	public ByteBuffer getResourceBytes();
	
	public boolean isCompressed();
	
	public default ResourceDecompressor getDecompressor() {
		return new UncompressedDecompressor(this);
	};
	
	
	
}
