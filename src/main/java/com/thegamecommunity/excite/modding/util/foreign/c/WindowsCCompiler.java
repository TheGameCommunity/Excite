package com.thegamecommunity.excite.modding.util.foreign.c;

import java.io.IOException;
import java.nio.file.Path;

public class WindowsCCompiler implements CLangCompiler {

	@Override
	public void compile(Path source, Path dest) throws InterruptedException, IOException, LinkageError {
		throw new LinkageError("Windows Compiler Not Yet Implemented!");
	}

}
