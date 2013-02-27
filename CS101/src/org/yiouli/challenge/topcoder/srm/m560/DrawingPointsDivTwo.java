package org.yiouli.challenge.topcoder.srm.m560;

import java.util.Arrays;

/**
 * @division 2
 * @points 1000
 * 
 * @description 
 * Once upon a time, Little Wojtek had drawn a number of points with integer coordinates onto a sheet of paper. Then he made zero or more steps. Each step looked as follows: Let's call all the points on Wojtek's paper old points. For every four old points that formed the vertices of a 1x1 square, Wojtek would draw a point in the middle of that square. Once he had drawn all such new points, he took an eraser and erased all the old points.
 * <p>
 * He has been playing for a while when he was called downstairs to dinner. He looked at the paper with a surprised face and wondered how many steps he had made.
 * <p> 
 * You are given a String[] points, describing a rectangular area of Wojtek's paper. This area contains all of the points that were drawn by Wojtek at the end of his play. More precisely, you may assume that there are real numbers (not necessarily integers) dy and dx such that the following holds:
 * <p>
 *  - If points[i][j] = '*', then there is a point at coordinates (dx+j,dy+i).<br>
 *  - There are no other points anywhere on the paper, only those that follow from the previous statement.
 * <p>
 * Return the maximum number of steps Wojtek could have made. If there is no maximum (that is, if the number of steps can be arbitrarily large), return -1 instead.
 * 
 * @constraints
 * - points will contain between 1 and 20 elements, inclusive.<br>
 * - points[0] will contain between 1 and 20 characters, inclusive.<br>
 * - All elements of points will contain the same number of characters.<br>
 * - Each character of each element of points will be either '*' (an asterisk) or '.' (a period).<br>
 * - points will contain at least one '*' character.
 * 
 */
public class DrawingPointsDivTwo {

	static final int SIZE = 20;
	
	boolean debug;
	
	public DrawingPointsDivTwo() {
		debug = false;
	}
	
	public DrawingPointsDivTwo(boolean debug) {
		this.debug = debug;
	}
	
	String gridToString(boolean[][] grid, int n, int m, int step) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<n+step;i++) {
			for(int j=0;j<m+step;j++)
				if(grid[i][j])
					sb.append('*');
				else
					sb.append('.');
			sb.append('\n');
		}
		return sb.toString();
	}
	
	void reset(boolean[][] grid) {
		for(int i=0;i<grid.length;i++)
			for(int j=0;j<grid[i].length;j++)
				grid[i][j] = false;
	}
	
	void expand(boolean[][] grid, boolean[][] result) {
		reset(result);
		for(int i=grid.length-1;i>=0;i--)
			for(int j=grid[i].length-1;j>=0;j--)
				if(grid[i][j])
					result[i][j] = result[i][j+1] = result[i+1][j] = result[i+1][j+1] = true;
	}

	void reduce(boolean[][] grid, boolean[][] result) {
		reset(result);
		for(int i=grid.length-1;i>0;i--)
			for(int j=grid[i].length-1;j>0;j--)
				if(grid[i][j]&&grid[i][j-1]&&grid[i-1][j-1]&&grid[i-1][j])
					result[i-1][j-1] = true;
	}
	
	boolean verify(boolean[][][] dp, int step) {
		boolean[][] tmp = new boolean[SIZE*2][SIZE*2];
		reduce(dp[step+1], tmp);
		if(!Arrays.deepEquals(dp[step], tmp)) {
			if(step == 0)
				return false;
			else {
				dp[step] = tmp;
				return verify(dp, step-1);
			}
		}
		return true;
	}
	
	public int maxSteps(String[] points) {
		int n = points.length, m = points[0].length();
		//step, row, column
		boolean[][][] dp = new boolean[SIZE+1][SIZE*2][SIZE*2];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				if(points[i].charAt(j)=='*')
					dp[0][i][j] = true;
		int step = 0;
		while(step < SIZE) {
			expand(dp[step],dp[step+1]);
			if(!verify(dp, step))
				return step;
			step++;
			if(debug) {
				for(int i=0;i<=step;i++)
					System.out.println(gridToString(dp[i], n, m, i));
			}
		}
		return -1;
	}
}
