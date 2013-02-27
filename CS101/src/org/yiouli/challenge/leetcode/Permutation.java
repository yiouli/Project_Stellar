package org.yiouli.challenge.leetcode;

import java.util.Arrays;
import java.util.LinkedList;

public class Permutation {
    
    static void swap(int[] num, int i, int j) {
        int tmp = num[i];
        num[i] = num[j];
        num[j] = tmp;
    }

    //adding all permutation from num[i]..[n] with same prefix num[0]..[i-1]
    //num should be in same order after processing
    static void permute(int[] num, int idx, LinkedList<int[]> col) {
    	if(idx == num.length-1)
    		col.add(Arrays.copyOf(num, num.length));
    	else {
    		//add each letter to prefix and get permutations
    		for(int i=idx;i<num.length;i++) {
    			swap(num, i, idx);
    			permute(num, idx+1, col);
    			swap(num, i, idx);
    		}
    	}
    }
    
    /**
     * Trying to create new arrays at each level instead of swapping back at each level
     * and creating all at the leaf of permutation tree.
     * Notice that the original input array will also be part of output so modification
     * on input array after function call will have effect on the output list.
     * 			
     * 			abc
     * 		axx		bxx		cxx
     * abc	acb	  bac  bca  cab  cba
     * 
     */
    static void permuteNoSwapBack(int[] num, int idx, LinkedList<int[]> col) {
    	int n = num.length;
    	if(idx == n-1)
    		col.add(num);
    	for(int i=idx;i<n;i++) {
			int[] tmp = Arrays.copyOf(num, n);
			swap(tmp, i, idx);
			permuteNoSwapBack(tmp, idx+1, col);
    	}
    }
    
    public static LinkedList<int[]> permute(int[] num, boolean noSwapBack) {
    	LinkedList<int[]> ret = new LinkedList<int[]>();
    	if(noSwapBack)
    		permuteNoSwapBack(num, 0, ret);
    	else
    		permute(num, 0, ret);
    	return ret;
    }

    public static LinkedList<int[]> permuteDuplicates(int[] num) {
    	Arrays.sort(num);
    	LinkedList<int[]> ret = new LinkedList<int[]>();
    	permuteNoSwapBack(num, 0, ret);
    	return ret;
    }
}
