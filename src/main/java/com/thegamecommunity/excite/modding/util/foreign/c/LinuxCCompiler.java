package com.thegamecommunity.excite.modding.util.foreign.c;

import java.io.IOException;
import java.nio.file.Path;

public class LinuxCCompiler implements CLangCompiler {

	@Override
	public void compile(Path source, Path dest) throws IOException, LinkageError {
		ProcessBuilder process = new ProcessBuilder(new String[] {"gcc", source.toAbsolutePath().toString(), "-o", dest.toAbsolutePath().toString()});
		process.inheritIO().redirectErrorStream(true);
		int exitCode = -1000;
		
		try {
			exitCode = process.start().waitFor();
		} catch (InterruptedException e) {
			IOException ioe = new IOException("Interrupted!");
			ioe.initCause(e);
			throw ioe;
		}
		
		if(exitCode != 0) {
			throw new IOException("Failed to compile. Exit code: " + exitCode);
		}
	}

}
