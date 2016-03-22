package com.github.sdong.gradle.coverityfortify;

import static org.junit.Assert.*

import com.github.sdong.gradle.coverityfortify.util.Utils

import org.junit.Test


public class UtilsTest {

	@Test
	public void testZipFolder() {
		def zipFileName = "D:/temp/logs.zip"
		def inputDir = "D:/temp/logs/logs"
		println 'Start to zip'
		Utils.zipFolder( zipFileName,  inputDir)
		//fail("Not yet implemented");
	}

}
