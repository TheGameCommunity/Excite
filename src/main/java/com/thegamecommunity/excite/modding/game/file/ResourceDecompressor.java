package com.thegamecommunity.excite.modding.game.file;

import java.io.IOException;
import java.nio.file.Path;

public interface ResourceDecompressor {

	public Path decompress() throws IOException;
	
}
