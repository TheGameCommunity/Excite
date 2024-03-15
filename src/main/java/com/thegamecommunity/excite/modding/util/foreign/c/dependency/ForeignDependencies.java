package com.thegamecommunity.excite.modding.util.foreign.c.dependency;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.thegamecommunity.excite.modding.util.foreign.c.CLangCompiler;

public enum ForeignDependencies {
	EQUICKLZ("EQuickLZ",
		"@EQuickLZVersion@",
		"https://dl.wildermods.com/?user=TheGameCommunity&?project=EQuickLZ&?version=@EQuickLZVersion@&?artifact=EQuickLZ.c",
		"/EQuickLZ.c"
	);
	
	public static final Path cSourceDir = Path.of("./run/deps/src").toAbsolutePath();
	public static final Path cCompileDir = Path.of("./run/deps/bin").toAbsolutePath();
	
	static {
		try {
			Files.createDirectories(cCompileDir);
			Files.createDirectories(cCompileDir);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	
	private final String name;
	private String version;
	private final String url;
	private final String dest;
	private final Pattern obtainVersionRegex;
	
	private static final Pattern versionRegex = Pattern.compile("@.*?@");
	
	private ForeignDependencies(String name, String version, String url, String dest) {
		this.name = name;
		this.version = version;
		this.url = url;
		this.dest = dest;
		obtainVersionRegex = Pattern.compile(version.replace("@",  "") + " = (?<version>.*)");
	}
	
	public Path write(boolean overwrite) throws IOException {
		Path dest = cSourceDir.resolve(getFileName() + ".c").toAbsolutePath();
		if(!(Files.exists(dest) || Files.exists(getCompiledLocation())) || overwrite) {
			System.out.println("Downloading " + name + ":\nFrom: " + url + "\nInto: " + dest);
			FileUtils.copyInputStreamToFile(getURL().openStream(), dest.toFile());
		}
		else {
			System.out.println("Resource already exists: " + dest);
		}
		return dest;
	}

	public String getName() {
		return name;
	}
	
	public String getFileName() {
		return name + "-[" + getVersion() + "]";
	}
	
	public Path getCompiledLocation() {
		return cCompileDir.resolve(getFileName());
	}

	public String getVersion() {
		if(versionRegex.matcher(version).matches()) {
			try {
				String versions = IOUtils.resourceToString("./gradle.properties", Charset.defaultCharset());
				Matcher matcher = obtainVersionRegex.matcher(versions);
				if(matcher.find()) {
					version = matcher.group("version");
					System.out.println("Found version in gradle.properties RESOURCE: " + "");
				}
				else {
					throw new Error("gradle.properties exists, but no version was found for " + name);
				}
			} catch (IOException e) {
				try {
					System.out.println("There was either no gradle.properties in resources, or there was an error reading it. Looking for gradle.properties in devspace.");
					File gradleProperties = new File("./gradle.properties");
					if(gradleProperties.exists()) {
						String versions = new String(Files.readAllBytes(gradleProperties.toPath()));
						Matcher matcher = obtainVersionRegex.matcher(versions);
						if(matcher.find()) {
							version = matcher.group("version");
							System.out.println("Found version in gradle.properties DEVSPACE: " + "");
						}
						else {
							throw new Error("gradle.properties exists, but no version was found for " + name);
						}
					}
				}
				catch(IOException e2) {
					throw new IOError(e2);
				}
			}
			version = version.substring(version.indexOf('=') + 1).trim();
		}
		return version;
	}

	public URL getURL() {
		try {
			URL url = new URL(versionRegex.matcher(this.url).replaceAll(getVersion()));
			if(url.getProtocol().equals("http")) {
				throw new Error("HTTP protocol declared for URL in " + name + ". use HTTPS instead");
			}
			return url;
		} catch (MalformedURLException e) {
			throw new IOError(e);
		}
	}

	public File getDest() {
		return new File(versionRegex.matcher(dest).replaceAll(getVersion())).getAbsoluteFile();
	}
	
	public static void downloadAndCompileAllDeps() throws IOException, InterruptedException, LinkageError {
		downloadAndCompileAllDeps(true);
	}
	
	public static void downloadAndCompileAllDeps(boolean overwrite) throws IOException, InterruptedException, LinkageError {
		HashMap<ForeignDependencies, Path> deps = new HashMap<>();
		
		for(ForeignDependencies dep : values()) {
			Path f = dep.write(overwrite);
			if(f != null) {
				deps.put(dep, f);
			}
		}
		
		CLangCompiler compiler = CLangCompiler.get();
		for(Entry<ForeignDependencies, Path> e : deps.entrySet()) {
			compiler.compile(e.getValue(), (e.getKey().getCompiledLocation()));
		}
	}

}
