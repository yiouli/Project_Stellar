package org.yiouli.challenge.topcoder.srm.m560;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yiouli.challenge.topcoder.srm.m560.DrawingPointsDivTwo;

public class DrawingPointsDivTwoTest {

	@Test
	public void test1() {
		DrawingPointsDivTwo o = new DrawingPointsDivTwo();
		String[] t = new String[]{"*..*"};
		assertEquals(1, o.maxSteps(t));
	}
	
	@Test
	public void test4() {
		DrawingPointsDivTwo o = new DrawingPointsDivTwo(true);
		String[] t = new String[]
						{"....................",
						 "..........*.........",
						 "....................",
						 ".........*..........",
						 "....................",
						 "....................",
						 "....................",
						 "....................",
						 "....................",
						 "....................",
						 "....................",
						 "....................",
						 ".*..................",
						 "....................",
						 "*.............**....",
						 "....................",
						 "....................",
						 "................*...",
						 "....................",
						 "...................."};
		assertEquals(11, o.maxSteps(t));
	}
}
