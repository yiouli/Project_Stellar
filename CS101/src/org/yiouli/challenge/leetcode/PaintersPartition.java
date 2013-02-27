package org.yiouli.challenge.leetcode;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Given an array of non-negative integers and a positive integer k,
 * find the way to part the array into k or less partitions, so that 
 * the maximum sum over each of the partition will be minimized.
 * 
 * @author leoSU
 *
 */
public class PaintersPartition {

	/**
	 * give the sum of elements in a from index start to end (both inclusive).
	 * @param a is the array to sum.
	 * @param start is the starting index of sum (inclusive).
	 * @param end is the ending index of sum (inclusive).
	 * @return the sum of elements in the portion.
	 */
	protected static int sum(int[] a, int start, int end) {
		int ret = 0;
		for(int i = start; i<=end; i++)
			ret+=a[i];
		return ret;
	}
	
	/**
	 * Find the minimum max sum of k partitions over first m elements of a.
	 * @param a is the array to be parted.
	 * @param m is the number of elements need to considered.
	 * @param k is the maximum number of partition.
	 * @param p is an empty list that will have all the parting indices after the function returns.
	 * @return the minimum max sum of each partition.
	 * @throws IllegalArgumentException when a is null, k less than 1,
	 * m larger than size of a or less than 0.
	 */
	protected static long minMaxSum(int[] a, int m, int k, LinkedList<Integer> p) {
		if(a == null || m < 0 || k < 1 || a.length < m || p == null || p.size() != 0)
			throw new IllegalArgumentException();
		if(k == 1)
			return sum(a, 0, m-1);
		if(m == 0)
			return 0;
		if(m == 1)
			return a[0];
		LinkedList<Integer> subp = null;
		long minMaxSum = Long.MAX_VALUE;
		int pi = 0;
		for(int i=0;i<=m;i++) {
			LinkedList<Integer> tmp = new LinkedList<Integer>();
			long maxSum = Math.max(minMaxSum(a, i, k-1, tmp), sum(a, i, m-1));
			if(maxSum < minMaxSum) {
				minMaxSum = maxSum;
				subp = tmp;
				pi = i;
			}
		}
		for(Integer i : subp)
			p.add(i);
		p.add(pi);
		return minMaxSum;
	}
	
	/**
	 * Given the array and k, find a way to part the array into no more than
	 * k partitions while minimize the max sum of each partition and find the 
	 * minimum sum.
	 * @param a is the array to be parted.
	 * @param k is the maximum number of partitions allowed.
	 * @param p is an empty list that will have all the parting indices after the function returns.
	 * @return the minimum max sum over each partition.
	 * @throws IllegalArgumentException if a is null or k is non-positive.
	 */
	public static long minMaxSumPartitionRecursive(int[] a, int k, LinkedList<Integer> p) {
		if(a == null || k < 1 || p == null || p.size() != 0)
			throw new IllegalArgumentException();
		return minMaxSum(a, a.length, k ,p);
	}
	
	public static long minMaxSumPartitionDP(int[] a, int k, LinkedList<Integer> p) {
		if(a == null || k < 1 || p == null || p.size() != 0)
			throw new IllegalArgumentException();
		int n = a.length;
		//s[i] = a[0]+...a[i-1], s[0] = 0
		long[] s = new long[n+1];
		long total = 0;
		for(int i=0;i<n;i++) {
			total += a[i];
			s[i+1] = total;
		}
		//DP table (n, k) while only store two rows at a time
		//ms[m][l] will be min max sum over partitions for first m elements in a
		//and number of partitions equals l
		//ms[m][l] = min{ max{ ms[j][l-1], s[m]-s[j] } for j <= m }
		//for l = 1, ms[m][l] = s[m];
		long[] msprev = Arrays.copyOf(s, n+1);
		long[] ms = new long[n+1];
		int[][] pis = new int[n+1][k+1];
		for(int l=2;l<=k;l++) {
			for(int m=0;m<=n;m++) {
				long minMaxSum = Long.MAX_VALUE;
				for(int j=0;j<=m;j++) {
					long maxSum = Math.max(msprev[j], s[m]-s[j]);
					if(maxSum < minMaxSum) {
						minMaxSum = maxSum;
						pis[m][l] = j;
					}
				}
				ms[m] = minMaxSum;
			}
			long[] tmp = msprev;
			msprev = ms;
			ms = tmp;
		}
		int pi = n;
		for(int i = k;i>0;i--) {
			pi = pis[pi][i];
			if(pi == 0)
				break;
			p.addFirst(pi);
		}
		return msprev[n];
	}
	
	/**
	 * Find the minimum k that by parting a into k partitions, no sum over partition
	 * is larger than maxSum. Time complexity of the procedure is O(n).
	 * @param a is the array to be parted.
	 * @param maxSum is the upper limit(inclusive) for sum over partition.
	 * @return minium k that satisfy the condition, Integer.MAX_VALUE when impossible.
	 */
	static int getk(int[] a, long maxSum) {
		int ret = 1;
		long sum = 0;
		for(int i=0;i<a.length;i++) {
			if(a[i] > maxSum)
				return Integer.MAX_VALUE;
			if(sum + a[i] > maxSum) {
				ret++;
				sum=a[i];
			}
			else
				sum+=a[i];
		}
		return ret;
	}

	/**
	 * Time complexity is O(N log N + N log(max{a[i] for i in [0,N)}), N is the length of a.
	 * @param a
	 * @param k
	 * @return
	 */
	public static long minMaxSumPartitionBS(int[] a, int k) {
		if(a == null || a.length == 0 || k <1)
			throw new IllegalArgumentException();
		int n = a.length;
		//asum[i] = a[0]+...+a[i]
		long[] asum = new long[n];
		asum[0] = a[0];
		for(int i=1;i<n;i++)
			asum[i] = asum[i-1]+a[i];
		//since when sum goes up k will be smaller, the problem can be seen as finding
		//minimum maxSum that makes num of partition<=k
		//first search for interval in asum where getk(asum[i])<=k and getk(asum[i-1])>k
		int left=0,right=n-1;
		int mid = (left+right)/2;
		while(left < right) {
			mid = (left+right)/2;
			int highk = getk(a, asum[mid]), lowk = getk(a, asum[mid+1]);
			if(highk>k && lowk<=k)
				break;
			else if(highk<=k)
				right = mid;
			else
				left = mid;
		}
		//we don't worry about left=right=n-1 here since getk(asum[n-1]) is 1 and it will 
		//surely satisfy the condition <=k.
		//take a[0] as maxSum results in num of partition<=k, but maxSum should at least 
		//be the value of largest element(so minMaxSum>=a[0]), so a[0] is the miMaxSum.
		if(left == right)
			return a[0];
		//now binary search in interval (asum[mid], asum[mid+1]] to find minimum value taht
		//getk(val) = k. 
		//left != right for sure here. since getk(left)!=getk(right).
		long leftVal = asum[mid], rightVal = asum[mid+1];
		while(leftVal < rightVal) {
			long midVal = (leftVal+rightVal)/2;
			int highk = getk(a, midVal), lowk = getk(a, midVal+1);
			if(highk>k && lowk<=k)
				return midVal+1;
			else if(highk<=k)
				rightVal = midVal;
			else	//lowk>k
				leftVal = midVal+1;
		}
		//shouldn't reach here if coded correctly
		throw new RuntimeException();
	}
}
