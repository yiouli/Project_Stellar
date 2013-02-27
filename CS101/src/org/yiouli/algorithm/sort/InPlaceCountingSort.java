package org.yiouli.algorithm.sort;

import java.util.Hashtable;

public class InPlaceCountingSort<T> {

	protected Hashtable<T, Integer> createMapping(T[] alphabet) {
		Hashtable<T, Integer> ret = new Hashtable<T, Integer>();
		for(int i=0;i<alphabet.length;i++) {
			if(ret.containsKey(alphabet[i]))
				throw new IllegalArgumentException();
			ret.put(alphabet[i], i);
		}
		return ret;
	}
	
	protected void swap(T[] arr, int ai, int bi) {
		T tmp = arr[ai];
		arr[ai] = arr[bi];
		arr[bi] = tmp;
	}
	
	public void sort(T[] alphabet, T[] arr) {
		int m = alphabet.length;
		Hashtable<T, Integer> map = createMapping(alphabet);
		int[] firstIndices = new int[m];
		for(int i=0;i<arr.length; i++) {
			if(!map.containsKey(arr[i]))
				throw new IllegalArgumentException();
			int idx = map.get(arr[i]);
			int current = i;
			for(int j = m-1; j>idx; j--) {
				swap(arr, firstIndices[j], current);
				current = firstIndices[j];
				firstIndices[j]++;
			}
		}
	}
}
