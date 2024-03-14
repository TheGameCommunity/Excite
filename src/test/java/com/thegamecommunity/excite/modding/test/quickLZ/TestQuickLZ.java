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

import com.thegamecommunity.excite.modding.util.foreign.c.CLangCompiler;

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
	
	@Test
	@Order(1)
	void compileQuickLZ() throws IOException, InterruptedException {
		Files.copy(getClass().getResourceAsStream("/Equicklz.c"), uncompiledFile);
		CLangCompiler compiler = CLangCompiler.get();
		compiler.compile(uncompiledFile, compiledFile);
	}
	
	@Test
	@Order(2)
	void testCompiledFileExists() throws IOException, InterruptedException {
		assertTrue(Files.exists(compiledFile));
	}
	
	@Test
	@Order(3)
	void testHelp() throws InterruptedException, IOException, ExecutionException, TimeoutException {
		Process process = startProcess(compiledFile.toString(), "help");
		assertTrue(process.exitValue() == 2);
	}
	
	@Test
	@Order(4)
	void testSegFault() throws InterruptedException, ExecutionException, TimeoutException, IOException {
		Process process = startProcess(compiledFile.toString());
		assertTrue(process.exitValue() == 139);
	}
	
	private static Process startProcess(String... command) throws InterruptedException, ExecutionException, TimeoutException, IOException {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		Process process = processBuilder.start().onExit().get(5000, TimeUnit.SECONDS);
		return process;
	}
	
}
