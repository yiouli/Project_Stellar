package org.yiouli.challenge.leetcode;

import java.util.Arrays;

/**
 * Given an amount of money and a collection of coin's kinds, return
 * the total number of unique ways to get to the exact amount using those kinds
 * of coins.
 * 
 */
public class GetChanges {

	public static int getChanges(int total, int[] coins) {
		if(total == 0)
			return 1;
		if(coins.length == 0)
			return 0;
		int ret = 0;
		int val = 0;
		while(val <= total) {
			ret += getChanges(total-val,Arrays.copyOf(coins, coins.length-1));
			val += coins[coins.length-1];
		}
		return ret;
	}
}
