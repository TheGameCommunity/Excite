package com.thegamecommunity.excite.modding.test.quickLZ;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import com.thegamecommunity.excite.modding.util.foreign.c.dependency.ForeignDependencies;
import static com.thegamecommunity.excite.modding.util.foreign.c.dependency.ForeignDependencies.*;

@TestMethodOrder(OrderAnnotation.class)
class TestQuickLZ {

	static @TempDir Path testDir;
	static Path uncompiledFile;
	static Path compiledFile; 
	
	@BeforeAll
	public static void init() {
		uncompiledFile = testDir.resolve("Equicklz.c").toAbsolutePath();
		compiledFile = testDir.resolve("Equicklz").toAbsolutePath();
	}
	
	@Test()
	@Order(1)
	void downloadandCompileEQuickLZ() throws IOException, InterruptedException, LinkageError {
		ForeignDependencies.downloadAndCompileAllDeps(true);
	}
	
	@Test
	@Order(2)
	void testCompiledFileExists() throws IOException, InterruptedException {
		assertTrue(Files.exists(EQUICKLZ.getCompiledLocation()));
	}
	
	@Test
	@Order(3)
	void testHelp() throws InterruptedException, IOException, ExecutionException, TimeoutException {
		Process process = startProcess(EQUICKLZ.getCompiledLocation().toAbsolutePath().toString(), "help");
		assertTrue(process.exitValue() == 1);
	}
	
	@Test
	@Order(4)
	void testNoArgs() throws InterruptedException, ExecutionException, TimeoutException, IOException {
		Process process = startProcess(EQUICKLZ.getCompiledLocation().toAbsolutePath().toString());
		assertTrue(process.exitValue() == 3);
	}
	
	private static Process startProcess(String... command) throws InterruptedException, ExecutionException, TimeoutException, IOException {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		Process process = processBuilder.start().onExit().get(5000, TimeUnit.SECONDS);
		return process;
	}
	
}
