package org.stellar.gameplat.service.userinfostore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

class FileRepository {

	private final String repoDir;
	private final String fileExt;
	
	String getRepoDir() {
		return repoDir;
	}

	String getFileExt() {
		return fileExt;
	}

	FileRepository(String dir) {
		repoDir = dir;
		fileExt = ".json";
	}
	
	FileRepository(String dir, String ext) {
		repoDir = dir;
		fileExt = ext;
	}
	
	private String generateFileName(String key) {
		return repoDir + File.separator + key + fileExt;
	}
	
	void put(String key, String content) throws IOException
	{
		String filename = generateFileName(key);
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		writer.write(content);
		writer.flush();
		writer.close();
	}
	
	String get(String key) throws IOException
	{
		String filename = generateFileName(key);
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
		}
		reader.close();
		return sb.toString();
	}
	
	HashSet<String> getKeys() {
		HashSet<String> ret = new HashSet<String>();
		File dir = new File(repoDir);
		if(!dir.exists() || !dir.isDirectory())
			throw new IllegalArgumentException("Invalid repository directory");
		File[] files = dir.listFiles();
		for(File file : files) {
			String filename = file.getName();
			if(filename.endsWith(fileExt))
				ret.add(filename.substring(0, filename.lastIndexOf(fileExt)));
		}
		return ret;
	}
}
