package com.thegamecommunity.excite.modding.game.file;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ResourceFile {
	
	public ByteBuffer getRawBytes() throws IOException;
	
	public ByteBuffer getHeaderBytes() throws IOException;
	
	public ByteBuffer getResourceBytes() throws IOException;
	
	public boolean isCompressedArchive() throws IOException;
	
	public ResourceDecompressor getDecompressor();
	
	
	
}
