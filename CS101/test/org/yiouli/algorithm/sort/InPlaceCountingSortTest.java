package org.yiouli.algorithm.sort;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.yiouli.testutil.RandomGenerator;

public class InPlaceCountingSortTest {

	protected Character[] toCharacterArray(String s) {
		int n = s.length();
		Character[] ret = new Character[n];
		for(int i=0;i<n;i++)
			ret[i] = s.charAt(i);
		return ret;
	}
	
	@Test
	public void testSort() {
		InPlaceCountingSort<Character> obj = new InPlaceCountingSort<Character>();
		Character[] alphabet = new Character[]{'a', 'b', 'c'};
		int tc = 20000;
		int minLen = 1, maxLen = 20;
		int fc = 0;
		for(int i=0;i<tc;i++) {
			Character[] arr = toCharacterArray(RandomGenerator.getRandomString(minLen, maxLen, 'a', 'c'));
			Character[] copy = Arrays.copyOf(arr, arr.length);
			Arrays.sort(copy);
			obj.sort(alphabet, arr);
			if(!Arrays.deepEquals(arr, copy))
				fc++;
		}
		assertTrue(fc == 0);
	}

}
