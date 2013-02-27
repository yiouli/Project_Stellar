package org.yiouli.challenge.topcoder.srm.m558;

import java.util.LinkedList;

/**
 * @division 2
 * @points 600
 * 
 * @description
 * Little Fox Jiro has a rectangular board. On the board there is a row of N unit cells. The cells are numbered 0 through N-1 from the left to the right. Initially, the cells are not colored. Jiro must color each of the cells red, green, or blue.
 * <p>
 * You are given a String desiredColor with N characters. For each i, character i of desiredColor represents the color Jiro must use for cell i. If a character is one of 'R' (as red), 'G' (as green), and 'B' (as blue), it means that Jiro must use that particular color. If a character is '*', Jiro may use any of the three colors for the particular cell.
 * <p>
 * You are also given the ints stampCost and pushCost that describe the cost of the coloring process. The coloring process consists of two phases. In the first phase, Jiro must pick a single stamp he will then use to color all the cells. The length L of the stamp can be any integer between 1 and N, inclusive. A stamp of length L costs L*stampCost.
 * <p>
 * In the second phase, Jiro must repeatedly use the stamp to color the cells. Each use of the stamp works as follows:
 * <p>
 * - Jiro picks one of the three colors and pushes the stamp into ink of the chosen color C.<br>
 * - Jiro picks a segment of L contiguous cells such that each of them is either uncolored or already has the color C. The segment must be completely inside the board. That is, the leftmost cell of the segment must be one of the cells 0 through N-L.<br>
 * - Jiro pushes the stamp onto the chosen segment of cells. All the cells now have color C.<br>
 * <p>
 * Each use of the stamp costs pushCost.
 * Return the smallest possible total cost of coloring all the cells using the above process.
 * 
 * @constraints
 * - desiredColor will contain between 1 and 50 characters, inclusive.<br>
 * - Each character of desiredColor will be either 'R' or 'G' or 'B' or '*'.<br>
 * - stampCost will be between 1 and 100,000, inclusive.<br>
 * - pushCost will be between 1 and 100,000, inclusive.<br>
 * 
 */

public class Stamp {

	//color count
	int cc = 3;
	
	/**
	 * get integer representation of color c.
	 * @param c the color in character.
	 * @return the integer representation of c. 0 for R, 1 for G, 2 for B, -1 for others. 
	 */
	int getColor(char c) {
		if(c == 'R')
			return 0;
		if(c == 'G')
			return 1;
		if(c == 'B')
			return 2;
		return -1;
	}
	
	int min(int a, int b) {
		int ret = Integer.MAX_VALUE;
		if(a != -1)
			ret = a;
		if(b != -1)
			ret = Math.min(ret, b);
		return ret==Integer.MAX_VALUE?-1:ret;
	}
	
	int min(int[] arr) {
		return min(arr, 0, arr.length);
	}
	
	int min(int[] arr, int start, int end) {
		int ret = Integer.MAX_VALUE;
		for(int i=start;i<end;i++)
			if(arr[i]!=-1)
				ret = Math.min(ret, arr[i]);
		return ret==Integer.MAX_VALUE?-1:ret;
	}
	
	boolean isPaintable(String desiredColor, int l, int k, int color) {
		for(int i=k-l;i<k;i++) {
			int ic = getColor(desiredColor.charAt(i));
			if(ic != color && ic != -1)
				return false;
		}
		return true;
	}
	
	/**
	 * for given l of stamp length, for each combination of board length k to paint 
	 * and the last stamp color, stamp count sc will be:
	 * sc[l][color][k] = min{sc[l][color1][k-l] for all color |_| sc[l][color][j], k-l<j<k}
	 * for l<=k<=n and desiredColor[k-l]...[k-1] all can be painted color
	 * @param desiredColor
	 * @param stampCost
	 * @param pushCost
	 * @return
	 */
	public int getMinimumCostDP(String desiredColor, int stampCost, int pushCost) {
		int n = desiredColor.length();
		int minCost = stampCost + n * pushCost;
		for(int l=2;l<=n;l++) {
			//sc[k][color] for certain l
			//0 for R, 1 for G, 2 for B
			int[][] sc = new int[n+1][cc];
			//set impossible to -1
			for(int j=1;j<l;j++)
				for(int i=0;i<cc;i++)
					sc[j][i] = -1;
			for(int k=l;k<=n;k++) {
				for(int color=0;color<cc;color++) {
					if(!isPaintable(desiredColor, l, k, color))
						sc[k][color]=-1;
					else {
						// if paintable get sc[k][color] from solved subproblems
						sc[k][color] = min(sc[k-l]);
						for(int i=k-l;i<k;i++)
							sc[k][color] = min(sc[k][color], sc[i][color]);
						if(sc[k][color] != -1)
							sc[k][color]++;
					}
				}
			}
			int minSc = min(sc[n]);
			if(minSc != -1)
				minCost = Math.min(minCost, stampCost*l+pushCost*minSc);
		}
		return minCost;
	}
	
	int addCount(int minCount, int toAdd) {
		if(minCount == -1)
			return -1;
		else
			return minCount + toAdd;
	}
	
	/**
	 * Get the minimum total push cost to paint the first x elements on the board.
	 * @param dc is the desired colors for all the cells.
	 * @param x is the number of elements to paint from the leftmost of the board.
	 * @param l is the length of stamp.
	 * @param color is the color of last stamp that has been pushed.
	 * @return the minimum total push cost to paint correctly, -1 if not possible.
	 */
	int minPushCount(String dc, int x, int l, char color) {
		if(x == 0)
			return 0;
		//first push not done yet
		if(color == '*') {
			if(x < l)
				return -1;
			boolean r=false,g=false,b=false;
			for(int i=0;i<l;i++) {
				char c=dc.charAt(x-1-i);
				if(c != '*')
					color = c;
				if(c == 'R')
					r=true;
				if(c == 'G')
					g=true;
				if(c == 'B')
					b=true;
			}
			if(r^g^b && !(r&&g&&b))
				return addCount(minPushCount(dc, x-l, l, color),1);
			if(!(r||g||b)) {
				int min = Integer.MAX_VALUE;
				int count = minPushCount(dc, x-l, l, 'R');
				if(count != -1)
					min = count;
				count = minPushCount(dc, x-l, l, 'G');
				if(count != -1)
					min = Math.min(min, count);
				count = minPushCount(dc, x-l, l, 'B');
				if(count != -1)
					min = Math.min(min, count);
				return min == Integer.MAX_VALUE ? -1:min+1;
			}
		}
		else {
			if(x < l) {
				for(int i=x-1;i>=0;i--) {
					char c = dc.charAt(i);
					if(c != color && c!='*')
						return -1;
				}
				return 1;
			}
			else {
				//the first cell in the push can be any between 
				//first of previous color(inclusive) and last not previous color(exclusive).
				int lastNotC = x-1, firstC = -1;
				for(;lastNotC>x-1-l;lastNotC--) {
					char c = dc.charAt(lastNotC);
					if(c != color && c!= '*')
						break;
					if(c == color)
						firstC = lastNotC;
				}
				//start new stamp for the first x
				if(firstC == -1)
					return minPushCount(dc, x, l, '*');
				int min = Integer.MAX_VALUE;
				for(int i=firstC;i>lastNotC;i--) {
					int count = minPushCount(dc, i, l, color);
					if(count != -1)
					min = Math.min(min, count);
				}
				return min == Integer.MAX_VALUE ? -1:min+1;
			}
		}
		return -1;
	}
	
	
	/**
	 * 
	 * @param desiredColor is the desired colors for all the cells.
	 * @param stampCost is the cost of stamp per unit length.
	 * @param pushCost is the cost to push the stamp once.
	 * @return the minimum cost to paint the board.
	 */
	public int getMinimumCostRecur(String desiredColor, int stampCost, int pushCost) {
		//total cost = min{ stampCost*l+minPushCount(n,l,color)*pushCost for 0<l<=n }
		//minPushCount(i,l,color) = min{ minPushCount(j,l,color1)}+1,
		//while j is the index of last unpainted cell after pushing a new stamp
		int n = desiredColor.length();
		int minCost = n * pushCost + stampCost;
		for(int l=2;l<=n;l++) {
			int cost = l*stampCost;
			int pCount = minPushCount(desiredColor, n, l, '*');
			if(pCount == -1)
				continue;
			else
				cost += pCount * pushCost;
			if(cost < minCost)
				minCost = cost;
		}
		return minCost;
	}
	
	/**
	 * Partially correct algorithm.
	 * @deprecated
	 * @param desiredColor
	 * @param stampCost
	 * @param pushCost
	 * @return
	 */
	public int getMinimumCostDFA(String desiredColor, int stampCost, int pushCost) {
		int n = desiredColor.length();
		int ret = Integer.MAX_VALUE;
		//l is the length of stamp
		outer:
		for(int l=n;l>0;l--) {
			LinkedList<Integer> si = new LinkedList<Integer>();
			int start = 0, pot = 0;
			//initial state, color state, pending state
			int state = 0;
			char cur = '*';
			int i;
			for(i=0;i<n;i++) {
				char c = desiredColor.charAt(i);
				if(state == 0) {
					cur = c;
					if(c == '*')
						state = 2;
					else
						state = 1;
				}
				else if(state == 1) {
					if(c == '*' && i>=start + l) {
						pot = i;
						state = 2;
					}
					else if(c != '*' && c != cur) {
						if(i < start+l)
							continue outer;
						else {
							cur = c;
							start = i;
							si.add(start);
						}
					}
				}
				else if(state == 2 && c != '*') {
					if(c != cur) {
						cur = c;
						start = pot;
						si.add(start);
					}
					else
						pot = i + 1;
					state = 1;
				}
			}
			if(i >= start + l) {
				si.add(i);
				int count = 0;
				int prev = 0;
				while(!si.isEmpty()) {
					int idx = si.removeFirst();
					count += (idx - prev)/l;
					if((idx-prev)%l != 0)
						count++;
					prev = idx;
				}
				ret = Math.min(ret, l * stampCost + pushCost*count);
			}
		}
		//shouldn't reach here
		return ret;
	}
}
