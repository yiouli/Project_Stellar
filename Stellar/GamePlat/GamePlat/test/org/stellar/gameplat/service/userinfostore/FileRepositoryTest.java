package org.stellar.gameplat.service.userinfostore;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import org.junit.Test;
import org.yiouli.testutil.RandomGenerator;

public class FileRepositoryTest {

	private HashSet<String> CreateDummyFiles(String dir, String ext, int count) throws IOException {
		HashSet<String> ret = new HashSet<String>();
		for(int i = 0; i < count; i++) {
			String key;
			do {
				key = RandomGenerator.getRandomString(1, 30);
			} while (ret.contains(key));
			File f = new File(dir + File.separator + key + ext);
			f.createNewFile();
			ret.add(key);
		}
		return ret;
	}
	
	@Test
	public void testGet() throws IOException {
		String repoDir = "c:\\tmp\\testRepoGet";
		String ext = ".json";
		File fDir = new File(repoDir);
		fDir.mkdirs();
		try {
			HashSet<String> expectedKeys = CreateDummyFiles(repoDir, ext, 1000);
			FileRepository repo = new FileRepository(repoDir, ext);
			HashSet<String> keys = repo.getKeys();
			assertEquals("Key count", expectedKeys.size(), keys.size());
			for (String key : expectedKeys) {
				assertTrue("Get key", repo.get(key) != null);
				assertTrue("Contains key : " + key, keys.contains(key));
			}
		} finally {
			//clean up
			File[] files = fDir.listFiles();
			for (File file : files)
				file.delete();
			fDir.delete();
		}
	}
	
	private String getFileContent(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer sb = new StringBuffer();
		String line;
		while (null != (line = reader.readLine())) {
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
		}
		reader.close();
		return sb.toString();
	}
	
	@Test
	public void testPut() throws IOException {
		String repoDir = "c:\\tmp\\testRepoPut";
		String ext = ".json";
		File fDir = new File(repoDir);
		fDir.mkdirs();
		try {
			FileRepository repo = new FileRepository(repoDir, ext);
			String nl = System.getProperty("line.separator");
			String key = "testname";
			String content = "this is test content"+nl
					+"with multiple lines"+nl+nl+"final line"+nl;
			repo.put(key, content);
			assertEquals("content matching", content, repo.get(key));
			File f = new File(repoDir + File.separator + key + ext);
			assertTrue("file exists", f.exists());
			assertTrue("is file", f.isFile());
			assertEquals("file content", content, getFileContent(f));
			//test override
			String newContent = "new content" + nl;
			repo.put(key, newContent);
			assertEquals("override content matching", newContent, repo.get(key));
			f = new File(repoDir + File.separator + key + ext);
			assertTrue("override file exists", f.exists());
			assertEquals("override file content", newContent, getFileContent(f));
		} finally {
			//clean up
			File[] files = fDir.listFiles();
			for (File file : files)
				file.delete();
			fDir.delete();
		}
	}
}
