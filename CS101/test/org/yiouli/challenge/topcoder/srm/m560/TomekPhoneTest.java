package org.yiouli.challenge.topcoder.srm.m560;

import static org.junit.Assert.*;

import org.junit.Test;

public class TomekPhoneTest {

	@Test
	public void test0() {
		TomekPhone o = new TomekPhone();
		int[] occurences = new int[]{7,3,4,1};
		int[] keySizes = new int[]{2,2};
		assertEquals(19, o.minKeystrokes(occurences, keySizes));
	}

}
