package com.thegamecommunity.excite.modding.util.foreign.c;

import java.io.IOException;
import java.nio.file.Path;

public class UnknownOSCNonCompiler implements CLangCompiler {

	@Override
	public void compile(Path source, Path dest) throws InterruptedException, IOException, LinkageError {
		throw new LinkageError("Unknown Operating System, no linkage to the c compiler has been implemented for your operating system");
	}

}
