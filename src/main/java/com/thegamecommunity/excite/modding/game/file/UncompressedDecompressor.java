package com.thegamecommunity.excite.modding.game.file;

import java.nio.ByteBuffer;

public class UncompressedDecompressor implements ResourceDecompressor {

	private final ResourceFile file;
	
	public UncompressedDecompressor(ResourceFile file) {
		this.file = file;
	}
	
	@Override
	public ByteBuffer decompress() {
		return file.getResourceBytes();
	}

}
