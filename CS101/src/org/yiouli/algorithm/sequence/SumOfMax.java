package org.yiouli.algorithm.sequence;

/**
 * Given an array of size n and window size k, find the sum of max values in all size k
 * window starting from 0 to n-k.
 * A window of size k on an array starting at i will be the sub-array a[i]...a[i+k-1].
 * 
 */
public class SumOfMax {

	/**
	 * straight forward approach takes O(k*(n-k)) time
	 */
	public static long sumOfMaxPlain(int[] a, int k) {
		if(k <=0 || a==null || a.length < k)
			throw new IllegalArgumentException();
		int n = a.length;
		long ret = 0;
		for(int i=0;i<=n-k;i++) {
			int max = a[i];
			for(int j=1;j<k;j++) {
				if(a[i+j]>max)
					max = a[i+j];
			}
			ret += max;
		}
		return ret;
	}
	
	//a in decreasing order, return index of first element in a that smaller than val
	//return a.length if all no less than
	static int bs(int[] a, int val) {
		int n = a.length;
		if(val <= a[n-1])
			return n;
		if(val > a[0])
			return 0;
		int left = 0, right = n-1;
		while(left<right){
			int mid = (left+right)/2;
			if(val > a[mid+1] && val <= a[mid])
				return mid+1;
			else if(val <= a[mid+1])
				left = mid+1;
			else	//val > a[mid]
				right = mid;
		}
		throw new RuntimeException();
	}
	
	//Dynamic programming using the fact: 
	// maximum for elements after ith (0 based and inclusive)
	// of window starting at m, also can state as maximum in a[m+i]...a[m+k-1],
	// defined ad maxAfter[m][i]
	// maxAfter[m][i] = max(maxAfter[m-1][i+1],a[m+k-1]), i in [0,k-1]
	// maxLast[x][k-1] = a[x+k-1] for all x
	// also maxAfter is non-increasing so if do binary search on maxAfter
	// using value a[m+k-1], every maxAfter[m][i] will become a[m+k-1]
	public static long sumOfMaxDP(int[] a, int k) {
		if(k <=0 || a==null || a.length < k)
			throw new IllegalArgumentException();
		int n = a.length;
		int[] maxAfter = new int[k];
		maxAfter[k-1] = a[k-1];
		for(int i=k-2;i>=0;i--) {
			maxAfter[i] = Math.max(maxAfter[i+1], a[i]);
		}
		long ret = 0;
		for(int m=1;m<=n-k;m++) {
			
		}
		return ret;
	}
}
