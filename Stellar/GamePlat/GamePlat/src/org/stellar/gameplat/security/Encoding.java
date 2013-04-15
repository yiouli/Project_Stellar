package org.stellar.gameplat.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Encoding {
	
	public static String toHexString(byte[] b) {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	public static byte[] toSHA1(String key) {
	    MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } 
	    return md.digest(key.getBytes());
	}
	
	public static String toBase64(byte[] value) {
		return Base64.encode(value);
	}
	
	//----test stub-----------------------------------------------------------
	
	public static void main(String[] args) {
		String key = "x3JJHMbDL1EzLkh9GBhXDw==";
		String salt = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		System.out.println(toBase64(toSHA1(key.concat(salt))));
	}
}
