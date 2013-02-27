package org.yiouli.challenge.storm8;

class Solution {

	public int rice_chessboard (int[][] A) {
		if(A.length == 0 || A[0].length == 0)
			return 0;
		int n = A.length, m = A[0].length;
		int[] r = new int[m+1];
		for(int i=1;i<n+1;i++)
			for(int j=1;j<m+1;j++) {
				r[j] = Math.max(r[j-1], r[j]) + A[i-1][j-1];
			}
		return r[m];
	}
}
