package org.yiouli.challenge.leetcode;

/**
 * Given an n*m grid and the robot sit at [0,0], how many ways can the robot
 * get to [n-1,m-1]?
 *
 */
public class RobotPath {

	/**
	 * The number of ways to get to each cell forms a pascal triangle
	 * by looking diagonally:
	 * 
	 * 1 1  1  1  1...
	 * 1 2  3  4  5
	 * 1 3  6 10 15
	 * 1 4 10 20 35
	 * ...
	 * 
	 * So given a cell [i,j], the number of ways to get to it is C(i, i+j).
	 * Then it's obviously the return will be C(n-1, n+m-2).
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static long getPathNumMath(int n, int m) {
		if(m<=0||n<=0)
			throw new IllegalArgumentException();
		if(n==1 || m==1)
			return 1;
		long ret = 1;
		for(int i=m;i<n+m-1;i++)
			ret *= i;
		for(int i=1;i<n;i++)
			ret /= i;
		return ret;

        /*
         * in case of returning int, way to avoid overflow
        if(m==1 || n==1)
            return 1;
        double ret = 1.0;
        for(int i=1;i<=n-1;i++)
            ret*= (m-1+i)/(double)i;
        return (int)Math.round(ret);
        */
	}
	
	public static long getPathNumRecursive(int n, int m) {
		if(m<=0||n<=0)
			throw new IllegalArgumentException();
		if(m==1||n==1)
			return 1;
		return getPathNumRecursive(n,m-1)+getPathNumRecursive(n-1,m);
	}
	
	public static long getPathNumDP(int n, int m) {
		if(m<=0||n<=0)
			throw new IllegalArgumentException();
		if(m==1||n==1)
			return 1;
		int[] row = new int[m];
		for(int i=0;i<m;i++)
			row[i] = 1;
		for(int i=0;i<n;i++) {
			for(int j=1;j<m;j++)
				row[j] += row[j-1];
		}
		return row[m-1];
	}
}
