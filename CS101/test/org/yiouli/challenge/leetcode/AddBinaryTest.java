package org.yiouli.challenge.leetcode;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yiouli.challenge.leetcode.AddBinary;

public class AddBinaryTest {

	@Test
	public void testAddBinary() {
		String res = AddBinary.addBinary("11", "1");
		assertEquals("100",res);
	}
}
