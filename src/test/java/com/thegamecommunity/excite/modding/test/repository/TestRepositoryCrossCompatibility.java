package com.thegamecommunity.excite.modding.test.repository;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

public class TestRepositoryCrossCompatibility {

	private static final Pattern ILLEGAL_CHARS =
		Pattern.compile("[<>:\"/\\\\|?*]");

	@Test
	void noIllegalWindowsCharactersInGitTrackedFiles() throws IOException, InterruptedException {
		Process process = new ProcessBuilder("git", "ls-files")
			.redirectErrorStream(true)
			.start();

		List<String> violations = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {

			String path;
			while ((path = reader.readLine()) != null) {
				String name = path.substring(path.lastIndexOf('/') + 1);

				if (ILLEGAL_CHARS.matcher(name).find()) {
					violations.add("Illegal character: " + path);
				}
				if (name.endsWith(" ") || name.endsWith(".")) {
					violations.add("Illegal trailing character: " + path);
				}
			}
		}

		int exit = process.waitFor();
		if (exit != 0) {
			fail("git ls-files failed with exit code " + exit);
		}

		if (!violations.isEmpty()) {
			fail("Windows-illegal filenames detected:\n" + String.join("\n", violations));
		}
	}
}
