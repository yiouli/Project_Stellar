package org.yiouli.challenge.leetcode;

import org.junit.Test;
import org.yiouli.challenge.leetcode.GetChanges;

public class GetChangesTest {

	@Test
	public void testGetChanges() {
		int[] coins = new int[]{10,5,2,1};
		System.out.println(GetChanges.getChanges(17, coins));
	}
}
