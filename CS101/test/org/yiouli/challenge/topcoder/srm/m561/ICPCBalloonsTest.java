package org.yiouli.challenge.topcoder.srm.m561;

import static org.junit.Assert.*;

import org.junit.Test;

public class ICPCBalloonsTest {

	@Test
	public void test0() {
		ICPCBalloons o = new ICPCBalloons();
		int[] bc = {100};
		String bs = "L";
		int[] ma = {1,2,3,4,5};
		assertEquals(10, o.minRepaintings(bc, bs, ma));
	}

	@Test
	public void test1() {
		ICPCBalloons o = new ICPCBalloons();
		int[] bc = {100};
		String bs = "M";
		int[] ma = {10,20,30,40,50};
		assertEquals(-1, o.minRepaintings(bc, bs, ma));
	}
	
	@Test
	public void test2() {
		ICPCBalloons o = new ICPCBalloons();
		int[] bc = {5,6,1,5,6,1,5,6,1};
		String bs = "MLMMLMMLM";
		int[] ma = {7,7,4,4,7,7};
		assertEquals(6, o.minRepaintings(bc, bs, ma));
	}	
	
	@Test
	public void test3() {
		ICPCBalloons o = new ICPCBalloons();
		int[] bc = {100,100};
		String bs = "ML";
		int[] ma = {50,51,51};
		assertEquals(-1, o.minRepaintings(bc, bs, ma));
	}

	@Test
	public void test4() {
		ICPCBalloons o = new ICPCBalloons();
		int[] bc = {8,5,1,4,1,1,3,1,3,3,5,4,5,6,9};
		String bs = "MMMLLLMMLLMLMLM";
		int[] ma = {3,5,3,3,5,6,4,6,4,2,3,7,1,5,2};
		assertEquals(5, o.minRepaintings(bc, bs, ma));
	}
}
