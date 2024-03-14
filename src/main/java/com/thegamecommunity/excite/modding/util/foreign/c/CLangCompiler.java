package com.thegamecommunity.excite.modding.util.foreign.c;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.lang3.SystemUtils;

public interface CLangCompiler {

	/**
	 * Implementations should block the current thread until compilation either
	 * completes or fails.
	 * 
	 * @param source the source file to compile
	 * @param dest the destination file
	 * @throws IOException if a compilation error occurs
	 * @throws LinkageError if no compatible compiler was found
	 */
	public void compile(Path source, Path dest) throws InterruptedException, IOException, LinkageError;
	
	public static CLangCompiler get() {
		if(SystemUtils.IS_OS_WINDOWS) {
			return new WindowsCCompiler();
		}
		else if (SystemUtils.IS_OS_LINUX) {
			return new LinuxCCompiler();
		}
		else {
			return new UnknownOSCNonCompiler();
		}
	}
	
}
