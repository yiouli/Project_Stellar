package org.yiouli.challenge.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Given an array of distinct/repeatable numbers, print out all possible distinct
 * subsets of the array.
 * 
 */
public class Subset {

	public static String getContentString(List<int[]> col) {
		StringBuffer sb = new StringBuffer();
		sb.append("[\n");
		boolean first = true;
		for(int[] item : col) {
			if(!first)
				sb.append(",\n");
			first = false;
			sb.append("\t" + Arrays.toString(item));
		}
		sb.append("\n]");
		return sb.toString();
	}
	
	static LinkedList<int[]> getSubsetsOfDistinctArray(int[] a, int l, int r) {
		if(l > r) {
			LinkedList<int[]> ret = new LinkedList<int[]>();
			ret.add(new int[]{});
			return ret;
		}
		LinkedList<int[]> subsubsets = getSubsetsOfDistinctArray(a, l+1, r);
		LinkedList<int[]> ret = new LinkedList<int[]>();
		for(int[] subset : subsubsets) {
			int[] newSubset = new int[subset.length+1];
			newSubset[0] = a[l];
			System.arraycopy(subset, 0, newSubset, 1, subset.length);
			ret.add(subset);
			ret.add(newSubset);
		}
		return ret;
	}
	
	public static LinkedList<int[]> getSubsetsOfDistinctArrayRecursive(int[] a) {
		if(a == null)
			throw new IllegalArgumentException();
		Arrays.sort(a);
		return getSubsetsOfDistinctArray(a, 0, a.length-1);
	}
	
	/**
	 * Then length of a should be less than 63
	 * @param a
	 * @return
	 */
	public static LinkedList<int[]> getSubsetsOfDistinctArrayBit(int[] a) {
		if(a == null)
			throw new IllegalArgumentException();
		Arrays.sort(a);
		LinkedList<int[]> ret = new LinkedList<int[]>(); 
		int n = a.length;
		for(long i=0; i<1<<n; i++) {
			LinkedList<Integer> tmp = new LinkedList<Integer>();
			for(int j =0;1<<j<=i;j++) {
				if((1<<j & i) != 0)
					tmp.add(a[j]);
			}
			int[] subset = new int[tmp.size()];
			Iterator<Integer> it = tmp.iterator();
			for(int k=0;k<subset.length;k++)
				subset[k] = it.next();
			ret.add(subset);
		}
		return ret;
	}
	
	public static LinkedList<int[]> getSubsetsDistinctArrayIterate(int[] a) {
		if(a == null)
			throw new IllegalArgumentException();
		Arrays.sort(a);
		LinkedList<int[]> ret = new LinkedList<int[]>();
		ret.add(new int[]{});
		for(int i=0;i<a.length;i++) {
			LinkedList<int[]> tmp = new LinkedList<int[]>();
			for(int[] prev : ret) {
				int[] newSubset = new int[prev.length+1];
				newSubset[prev.length] = a[i];
				System.arraycopy(prev, 0, newSubset, 0, prev.length);
				tmp.add(newSubset);
			}
			for(int[] newSubset : tmp)
				ret.add(newSubset);
		}
		return ret;
	}
	
	public static LinkedList<int[]> getSubsetsDistinctArrayBFS(int[] a) {
		if(a == null)
			throw new IllegalArgumentException();
		Arrays.sort(a);
		LinkedList<int[]> ret = new LinkedList<int[]>();
		ret.add(new int[]{});
		int n = a.length;
		//queue of indices of all elements in each subset
		LinkedList<int[]> q = new LinkedList<int[]>();
		for(int i=0;i<n;i++)
			q.add(new int[]{i});
		while(!q.isEmpty()) {
			int[] indices = q.remove();
			int l = indices.length;
			int[] subset = new int[l];
			for(int i=0;i<l;i++)
				subset[i] = a[indices[i]];
			ret.add(subset);
			int nextIdx = indices[indices.length-1]+1;
			while(nextIdx < n) {
				int[] newIndices = new int[l+1];
				newIndices[l] = nextIdx;
				System.arraycopy(indices, 0, newIndices, 0, l);
				q.add(newIndices);
				nextIdx++;
			}	
		}
		return ret;
	}
	
	/**
	 * Very similar to the BFS-like approach for array of distinct values,
	 * but skipping duplicates when branching.
	 * @param a
	 * @return
	 */
	public static LinkedList<int[]> getSubsetsDuplicateBFS(int[] a) {if(a == null)
		if(a == null)
			throw new IllegalArgumentException();
		Arrays.sort(a);
		LinkedList<int[]> ret = new LinkedList<int[]>();
		int n = a.length;
		//queue of indices of all elements in each subset
		LinkedList<int[]> q = new LinkedList<int[]>();
		q.add(new int[]{});
		while(!q.isEmpty()) {
			int[] indices = q.remove();
			int l = indices.length;
			int[] subset = new int[l];
			for(int i=0;i<l;i++)
				subset[i] = a[indices[i]];
			ret.add(subset);
			int nextIdx = l==0?0:indices[l-1]+1;
			//difference comes here
			int lastVal = 0;
			boolean first = true;
			while(nextIdx < n) {
				if(first || a[nextIdx]!= lastVal) {
					//skip duplicates here
					first = false;
					lastVal = a[nextIdx];
					int[] newIndices = new int[l+1];
					newIndices[l] = nextIdx;
					System.arraycopy(indices, 0, newIndices, 0, l);
					q.add(newIndices);
				}
				nextIdx++;
			}	
		}
		return ret;
	}
	
	public static ArrayList<int[]> getSubsetsDuplicateIterate(int[] a) {
		if(a == null)
			throw new IllegalArgumentException();
		int n = a.length;
		//count of appearance for each value in a
		Hashtable<Integer, Integer> vcs = new Hashtable<Integer, Integer>();
		for(int i=0;i<n;i++)
			if(vcs.containsKey(a[i]))
				vcs.put(a[i], vcs.get(a[i])+1);
			else
				vcs.put(a[i], 1);
		ArrayList<int[]> ret = new ArrayList<int[]>();
		ret.add(new int[]{});
		//keyset will be all distinct vals in a
		//funky thing is that the order of the iteration here is in reverse order -.-
		//actually the previous line is wrong, negative numbers come after 0... 
		//so use an array and sort it
		Integer[] vals = vcs.keySet().toArray(new Integer[0]);
		Arrays.sort(vals);
		for(Integer val : vals) {
			int count = vcs.get(val);
			int len = ret.size();
			//get every subset previously existing, append and add
			for(int i=0;i<len;i++) {
				int[] prev = ret.get(i);
				//append current value 1 to count times
				for(int ac = 1;ac<=count;ac++) {
					int[] newSub = new int[prev.length+ac];
					for(int pos = 0;pos<ac;pos++)
						newSub[prev.length+pos] = val;
					System.arraycopy(prev,0,newSub,0,prev.length);
					ret.add(newSub);
				}
			}
		}
		return ret;
	}
}
