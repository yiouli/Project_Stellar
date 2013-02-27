package org.yiouli.challenge.leetcode;

/**
 * Given two sorted array a and b, find the median for them combined in O(log N),
 * while N is the total length of the two arrays.
 * Median is the middle element (odd length) or mean of two middle elements (even length).
 *
 */
public class MedianOfTwoSortedArray {

	/**
	 * Try find Median of combined a and b in a between a[left] to a[right]. If can't find
	 * in the interval, try to find it in b.
	 * @param a
	 * @param b
	 * @param left
	 * @param right
	 * @return
	 */
	static float findMedian(int[] a, int[] b, int left, int right) {
		int l = a.length, m = b.length;
		boolean isEven = (l + m) %2 ==0;
		if(left > right)
			return findMedian(b, a, Math.max(0, (m-l)/2), Math.min(m-1, (l+m)/2));
		int mid = (left+right)/2;
		int remain = (l+m)/2-mid;
		if((remain == 0 || b[remain-1]<=a[mid]) && (remain == m || b[remain]>=a[mid])) {
			if(isEven) {
				//two candidates for the smaller median from each array
				float sm = Math.max(mid==0?Integer.MIN_VALUE:a[mid-1],
									remain==0?Integer.MIN_VALUE:b[remain-1]);
				return (a[mid]+sm)/2;
			}
			else
				return a[mid];
		}
		else if(remain != m && b[remain]<a[mid])
			return findMedian(a, b, left, mid-1);
		else
			return findMedian(a, b, mid+1, right);
	}
	
	public static float findMedian(int[] a, int[] b) {
		int l = a.length, m = b.length;
		return findMedian(a, b, Math.max(0, (l-m)/2), Math.min(l-1, (l+m)/2));
	}
	
	static float getMedian(int[] a, int l, int r) {
		if((r-l)%2==0)
			return a[(r+l)/2];
		return ((float)a[(r+l)/2+1]+a[(r+l)/2])/2;
	}
	
	/**
	 * a will be the smaller size array. Invairant is that median(s) will always in remaining
	 * portion of the arrays, variant is that the remaining portion will always be smaller.
	 * @param a
	 * @param b
	 * @param al
	 * @param ar
	 * @param bl
	 * @param br
	 * @return
	 */
	public static float findMedian(int[] a, int[] b, int al, int ar, int bl, int br) {
		if(al > ar)
			return getMedian(b, bl, br);
		int amid = (al+ar+1)/2, bmid = (bl+br+1)/2;
		//base case when a and b are both of size 1
		if(bl == br)
			return ((float)a[amid]+b[bmid])/2;
		//base case when a is of size 1
		if(ar == al) {
			if((br-bl)%2==0)
				return ((float)b[bmid] + Math.min(Math.max(b[bmid-1], a[amid]), b[bmid+1]))/2;
			else
				return Math.min(b[bmid], Math.max(a[amid], b[bmid-1]));
		}
		if(a[amid] >= b[bmid]) {
			//both even size, b's two medians in between a's two medians
			if((ar-al)%2!=0 && (br-bl)%2!=0 && b[bmid-1]>=a[amid-1])
				return ((float)b[bmid-1]+b[bmid])/2;
			//both odd size, so median of a can still be upper median
			if((ar-al)%2==0 && (br-bl)%2==0)
				return findMedian(a, b, al, amid, bl+ar-amid, br);
			//other cases a[amid] can't be median, -1 keep the variant 'a will getting smaller'.
			return findMedian(a, b, al, amid-1, bl+ar-amid+1, br);
		}
		else if((ar-al)%2!=0 && (br-bl)%2!=0 && a[amid-1]>=b[bmid-1])
			return ((float)a[amid-1]+a[amid])/2;
		else
			return findMedian(a, b, amid, ar, bl, br-amid+al);
	}
	
	public static float findMedianDivideArray(int[] a, int[] b) {
		if(a.length > b.length)
			return findMedian(b, a, 0, b.length-1, 0, a.length-1);
		return findMedian(a, b, 0, a.length-1, 0, b.length-1);
	}
}
