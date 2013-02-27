package org.yiouli.algorithm.sequence;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.yiouli.testutil.RandomGenerator;

public class MaxDistanceTest {

	@Test
	public void testMaxDistance() {
		int tc = 10000;
		int minLen = 1, maxLen = 30;
		int minVal = 0, maxVal = 99;
		int fc = 0;
		for(int i=0;i<tc;i++) {
			int[] a = RandomGenerator.getRandomArray(minLen, maxLen, minVal, maxVal);
			int resNlogN = MaxDistance.maxDistanceNlogN(a);
			int resLinear = MaxDistance.maxDistanceLinear(a);
			if(resNlogN != resLinear) {
				fc++;
				System.out.println(Arrays.toString(a));
				System.out.print("Answer : "+ MaxDistance.maxDistanceBrutal(a));
				System.out.println(", n log n : " + resNlogN +" , linear : " + resLinear);
			}
		}
		assertTrue(fc == 0);
	}

}
