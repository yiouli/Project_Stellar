package org.yiouli.challenge.topcoder.srm.m562;

public class PastingPaintingDivTwo {
	int overlaidPts(boolean[][] grid, boolean[][] overlay) {
		int n = grid.length, m = grid[0].length;
		int ret = 0;
		for(int i=1;i<n;i++)
			for(int j=1;j<m;j++)
				if(overlay[i][j] && grid[i-1][j-1])
					ret++;
		return ret;
	}

	void overlay(boolean[][] grid, boolean[][] overlay) {
		int n = grid.length, m = grid[0].length;
		for(int i=1;i<n;i++)
			for(int j=1;j<m;j++) {
				boolean prev = i<n-1 && j<m-1 && overlay[i+1][j+1];
				overlay[i][j] = prev || grid[i][j];
			}
	}

	public long countColors(String[] clipboard, int T) {
		int n = clipboard.length, m = clipboard[0].length();
		boolean[][] grid = new boolean[n][m];
		int count = 0;
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				if(clipboard[i].charAt(j) =='B') {
					count++;
					grid[i][j] = true;
				}
		boolean[][] overlay = new boolean[n][m];
		long ret = 0;
		int oc = Math.min(T, Math.min(n,m));
		for(int c=0;c<oc;c++) {
			ret += count- overlaidPts(grid, overlay);;
			overlay(grid, overlay);
		}
		int inc = count- overlaidPts(grid, overlay);
		for(int c=oc;c<T;c++)
			ret += inc;
		return ret;
	}
	
	public static void main(String[] args) {
		PastingPaintingDivTwo obj = new PastingPaintingDivTwo();
		String[] clipboard = {"."};
		System.out.println(obj.countColors(clipboard, 1000000000));
	}
}
