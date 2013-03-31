package org.stellar.gameplat.service.web;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UrlMapperTest {

	private static String path;
	private static UrlMapper mapper;
	
	@BeforeClass
	public static void setup() throws IOException {
		String dir = "testInput";
		File f = new File(dir);
		f.mkdirs();
		path = dir + "/UrlMapperTestConfig.web.json";
		f = new File(path);
		f.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write("{\"service\" :[{\"name\" : \"help\",\"className\" : \"some manual html presenter\"},"
				+"{\"name\" : \"userInfo\",\"className\" : \"org.stellar.gameplat.service.userinfostore.UserInfoStore\"}],"
				+"\"mapping\" :[{\"name\" : \"help\",\"method\" : \"GET\",\"pattern\" : \"/gameplatform/user/help\","
				+"\"service\" : \"help\"},{\"name\" : \"userInfoGet\",\"method\" : \"GET\",\"pattern\" : \"/gameplatform/user/#{username}\","
				+"\"service\" : \"userInfo\",\"exclude\" : \"/gameplatform/user/help\"},{\"name\" : \"userInfoPost\","
				+"\"exclude\" : \"/gameplatform/user/help\",\"method\" : \"POST\",\"pattern\" : \"/gameplatform/user/#{username}\""
				+",\"service\" : \"userInfo\"}]}");
		writer.close();
		UrlMapper.init(path);
		mapper = UrlMapper.instance();
	}
	
	@AfterClass
	public static void cleanup() {
		File f = new File(path);
		f.delete();
	}
	
	@Test
	public void testGetServiceClassNameByServiceName() {
		assertEquals(mapper.getServiceClassName("help"), "some manual html presenter");
		assertEquals(mapper.getServiceClassName("userInfo"), "org.stellar.gameplat.service.userinfostore.UserInfoStore");
		assertNull(mapper.getServiceClassName("randomName"));
	}

	@Test
	public void testGetServiceClassNameByRequest() {
		assertEquals("help with GET", "some manual html presenter", 
				mapper.getServiceClassName("Get", "/gameplatform/user/help"));
		assertNull("help with POST", mapper.getServiceClassName("Post", "/gameplatform/user/help"));
		assertEquals("userInfo with GET", "org.stellar.gameplat.service.userinfostore.UserInfoStore",
				mapper.getServiceClassName("Get", "/gameplatform/user/user1"));
		assertNull("other service", mapper.getServiceClassName("Get", "otherservice/user1"));
	}

	@Test
	public void testGetMappingNames() {
		String[] mappings = mapper.getMappingNames("userInfo");
		assertEquals("mappings count", 2, mappings.length);
		assertEquals("userInfoGet", mappings[0]);
		assertEquals("userInfoPost", mappings[1]);
	}
	
	@Test
	public void testGetUrl() {
		assertEquals("user1 GET", "/gameplatform/user/user1", 
				mapper.getUrl("userInfoGet", new String[]{"user1"}));
		assertEquals("user2 POST", "/gameplatform/user/user2", 
				mapper.getUrl("userInfoPost", new String[]{"user2"}));
		assertEquals("help", "/gameplatform/user/help", 
				mapper.getUrl("help", new String[0]));
		assertNull("other service", mapper.getUrl("otherservice", new String[0]));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetUrlWrongArgs() {
		mapper.getUrl("userInfoGet", new String[]{"a","b"});
	}
	
	@Test
	public void testGetVariables() {
		Hashtable<String, String> var;
		var = mapper.getParameters("/gameplatform/user/user1", "GET");
		assertEquals("user1", var.get("username"));
		var = mapper.getParameters("userInfoPost", "/gameplatform/user/user1", "POST");
		assertEquals("user1", var.get("username"));
		var = mapper.getParameters("help", "/gameplatform/user/help", "GET");
		assertEquals("help var count", 0, var.size());
		var = mapper.getParameters("otherservice", null, "GET");
		assertEquals("other service var count", 0, var.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetVariablesNotMatching() {
		mapper.getParameters("userInfoGet", "/gameplatform/user/help", "GET");
	}
}
