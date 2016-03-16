package com.github.sdong.gradle.coverityfortify.util;

import static org.junit.Assert.*

import org.junit.Test


public class testUtils {

	@Test
	public void testZipFolder() {
		def zipFileName = "D:/temp/logs.zip"
		def inputDir = "D:/temp/logs/logs"
		groovyzip.zipFolder( zipFileName,  inputDir)
		//fail("Not yet implemented");
	}

}
