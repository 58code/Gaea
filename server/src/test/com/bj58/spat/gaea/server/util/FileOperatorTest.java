package com.bj58.spat.gaea.server.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.bj58.spat.gaea.server.util.FileHelper;

public class FileOperatorTest {

	@Test
	public void testCreateFile() throws IOException {
		FileHelper.createFile("D:/temp/test.txt", "content");
	}

	@Test
	public void testCreateFolder() {
		FileHelper.createFolder("D:/temp/temp1/temp2/temp3");
	}

	@Test
	public void testGetFiles() {
		List<File> fileList = FileHelper.getFiles("D:/serviceframe_v2_II/lib", "jar");
		for(File f : fileList) {
			System.out.println(f.getAbsolutePath());
		}
	}

	@Test
	public void testGetUniqueLibPath() throws IOException {
		List<String> pathList = FileHelper.getUniqueLibPath("D:/serviceframe_v2_II/lib/other","D:/serviceframe_v2_II/lib/sun");
		for(String item : pathList) {
			System.out.println(item);
		}
	}

}