package org.yiouli.algorithm.sequence;

public class LongestCommonSequence {

	public static int lcs(String a, String b) {
		int m = a.length(), n = b.length();
		int[][] lens = new int[m+1][n+1];
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++) {
				int len = lens[i][j];
				if(a.charAt(i) == b.charAt(j))
					len++;
				len = Math.max(lens[i+1][j], len);
				lens[i+1][j+1] = Math.max(lens[i][j+1], len);
			}
		return lens[m][n];
	}
}
