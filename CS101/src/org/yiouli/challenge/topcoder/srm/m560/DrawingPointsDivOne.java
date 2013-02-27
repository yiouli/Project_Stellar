package org.yiouli.challenge.topcoder.srm.m560;

import java.util.Arrays;

/**
 * Basically the same as DrawingPointsDivTwo, only difference is that the input format is more confusing.
 * 
 * @division 1
 * @points 500
 * 
 * @description 
 * Once upon a time, Little Wojtek had drawn a number of points with integer coordinates onto a sheet of paper. Then he made zero or more steps. Each step looked as follows: Let's call all the points on Wojtek's paper old points. For every four old points that formed the vertices of a 1x1 square, Wojtek would draw a point in the middle of that square. Once he had drawn all such new points, he took an eraser and erased all the old points.
 * <p>
 * He has been playing for a while when he was called downstairs to dinner. He looked at the paper with a surprised face and wondered how many steps he had made.
 * <p> 
 * You are given two int[]s x, y of some equal length n. They describe all of the points that were drawn by Wojtek in the last step of his play. More precisely, you may assume that there are real numbers (not necessarily integers) dy and dx such that the following holds:
 * <p>
 *  - For each i between 0 and n-1, there is a point at coordinates (dx+x[i], dy+y[i]).<br>
 *  - There are no other points anywhere on the paper, only those that follow from the previous statement.
 * <p>
 * Return the maximum number of steps Wojtek could have made. If there is no maximum (that is, if the number of steps can be arbitrarily large), return -1 instead.
 * 
 * @constraints
 * - x will contain between 1 and 50 elements, inclusive.<br>
 * - x and y will contain the same number of elements.
 * - Each element of x will be between -70 and 70, inclusive.<br>
 * - Each element of y will be between -70 and 70, inclusive.<br>
 * - No two points described by x and y will be the same.
 * 
 */
public class DrawingPointsDivOne {
	
	static final int BOUND = 70;
	static final int SIZE = BOUND*2+1;

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
	
	public int maxSteps(int[] x, int[] y) {
		//step, row, column
		boolean[][][] dp = new boolean[SIZE+1][SIZE*2][SIZE*2];
		for(int i=0;i<x.length;i++)
			dp[0][x[i]+BOUND][y[i]+BOUND] = true;
		int step = 0;
		while(step < SIZE) {
			expand(dp[step],dp[step+1]);
			if(!verify(dp, step))
				return step;
			step++;
		}
		return -1;
	}
}
