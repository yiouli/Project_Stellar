package org.yiouli.challenge.leetcode;

import java.util.LinkedList;

/**
 * Given a two dimensional array, do a spiral walk on the array starting
 * [0,0] and heading left, return the list of values in visiting order.
 *
 */
public class SpiralWalk {
	
	static int x=0,y=0;
	//0:right 1:down 2:left 3:up
	static int direction=-1;
	//left, bottom, right, top
	static int[] bounds = new int[4];

	static void setLoc(int i, int j) {
		x=i;
		y=j;
	}
	
	static void setDirection(int direct) {
		direction = direct;
	}
	
	//height, width
	static void setBounds(int n, int m) {
		bounds[0] = m-1;
		bounds[1] = n-1;
		bounds[2] = 0;
		bounds[3] = 0;
	}
	
	static void turnLeft() {
		if(direction == 1 || direction == 2)
			bounds[(direction+3)%4]--;
		else
			bounds[(direction+3)%4]++;
		direction = (direction+1)%4;
	}
	
	static void step() {
		switch(direction) {
		case 0:y++;break;
		case 1:x++;break;
		case 2:y--;break;
		case 3:x--;break;
		default: throw new RuntimeException();
		}
	}
	
	static void move() {
		if((direction%2==0 && y==bounds[direction])
			|| (direction%2!=0 && x==bounds[direction]))
			turnLeft();
		step();
	}
	
	public static LinkedList<Integer> spiralWalk(int[][] a) {
		LinkedList<Integer> ret = new LinkedList<Integer>();
		if(a == null || a.length == 0)
			return ret;
		setLoc(0,0);
		setDirection(0);
		int n = a.length, m = a[0].length;
		setBounds(n,m);
		do{
			ret.add(a[x][y]);
			move();
		} while(ret.size() != m*n);
		return ret;
	}
}
