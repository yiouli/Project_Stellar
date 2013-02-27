package org.yiouli.algorithm.sequence;

import java.util.Arrays;
import java.util.LinkedList;

class Item implements Comparable<Item> {
	
	int index;
	int value;
	
	Item(int index, int value) {
		this.index = index;
		this.value = value;
	}
	
	@Override
	public int compareTo(Item item) {
		if(value == item.value)
			return index - item.index;
		return value - item.value;
	}
}

public class MaxDistance {

	public static int maxDistanceBrutal(int[] a) {
		int n = a.length;
		int ret = 0;
		for(int i=0;i<n;i++)
			for(int j=n-1;j>i;j--)
				if(a[j]>a[i] && j-i>ret)
					ret = j-i;
		return ret;
	}
	
	public static int maxDistanceNlogN(int[] a) {
		int n = a.length;
		Item[] items = new Item[n];
		for(int i=0;i<n;i++)
			items[i] = new Item(i, a[i]);
		Arrays.sort(items);
		int[] endIndices = new int[n];
		int endIndex = endIndices[n-1] = -1;
		for(int i=n-2;i>=0;i--) {
			if(items[i+1].index > endIndex)
				endIndex = items[i+1].index;
			if(items[i].value == items[i+1].value)
				endIndices[i] = endIndices[i+1];
			else
				endIndices[i] = endIndex;
		}
		int ret = 0;
		for(int i=0;i<n;i++)
			if(endIndices[i]-items[i].index>ret)
				ret = endIndices[i]-items[i].index;
		return ret;
	}
	
	
	//max j-i for a[i]<a[j]
	public static int maxDistanceLinear(int[] a) {
		int ret = 0;
		int n = a.length;
		LinkedList<Integer> startIndices = new LinkedList<Integer>();
		int min = Integer.MAX_VALUE;
		for(int i=0;i<n;i++)
			if(a[i] < min) {
				min = a[i];
				startIndices.add(i);
			}
		for(int i=n-1;i>=0; ) {
			if(startIndices.isEmpty())
				break;
			int startIndex = startIndices.getLast();
			if(a[i] > a[startIndex]) {
				ret = Math.max(ret, i-startIndex);
				startIndices.removeLast();
			}
			else
				i--;
		}
		return ret;
	}
}
