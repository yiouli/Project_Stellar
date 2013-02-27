package org.yiouli.challenge.topcoder.srm.m561;

import java.util.*;

class Balloon implements Comparable<Balloon> {
	char size;
	int count;
	int color;
	boolean arranged;
	
	Balloon(int color, char size, int count) {
		this.size = size;
		this.count = count;
		this.color = color;
		arranged = false;
	}
	
	@Override
	public int compareTo(Balloon b) {
		return count - b.count;
	}
}

/**
 * @division 2
 * @points 500
 * 
 * @description
 * You are organizing a subregional ACM ICPC contest. The problemset at the contest will consist of M problems. According to an ACM ICPC tradition, when a team solves a problem, it gets awarded a balloon. To account for this, you've bought balloons of N different colors (conveniently numbered from 0 to N-1). The number of balloons of color i that you've bought is given by balloonCount[i]. Balloons come in two sizes: medium and large. All balloons of the same color have the same size. If the i-th character of balloonSize is 'M', then all balloons of color i have medium size, and if this character is 'L', then all balloons of color i have large size.  
 * <p>
 * Today you've been at the meeting with the scientific committee of the contest. There, you learned that there are additional restrictions of which you were not aware. Here are those restrictions:<br>
 *  - All balloons that get awarded for a particular problem must have the same color and size.<br>
 *  - For any two problems, the colors of balloons awarded for solving them must be different. In other words, the color of balloons awarded for each problem must be unique.<br>
 * <p>
 * These are definitely bad news, since you ordered balloons pretty much arbitrarily and it's possible that you won't be able to satisfy the restrictions with the balloons you currently have. However, the good news is that scientific committee members were able to evaluate the difficulty of each problem. More exactly, they told you that the maximum number of teams that can potentially solve the i-th problem is maxAccepted[i]. The scientific committee members are very clever and experienced, so their prediction is guaranteed to come true.  
 * <p>
 * Your budget is limited and balloons are expensive, so buying more of them is not an option. Fortunately, there is a very cheap balloon repaint service at your city, so you are going to use it. The service offers repainting a given balloon into any other color. This can be one of the N colors you have, as well as any color that you don't have yet. However, it is not possible to change the size of a balloon.  
 * <p>
 * You are given the int[]s balloonCount, maxAccepted and the String balloonSize. Return the minimum number of balloons that have to be repainted in order to guarantee that you will be able to award balloons to the teams properly. If it is impossible to achieve the goal using any number of balloon repaintings, return -1.
 *
 * @constraints
 * - balloonCount will contain between 1 and 50 elements, inclusive.<br>
 * - Each element of balloonCount will be between 1 and 100, inclusive.<br>
 * - balloonSize will contain the same number of characters as the number of elements in balloonCount.<br>
 * - Each character of balloonSize will be 'M' or 'L'.<br>
 * - maxAccepted will contain between 1 and 15 elements, inclusive.<br>
 * - Each element of maxAccepted will be between 1 and 100, inclusive.<br>
 * 
 */
public class ICPCBalloons {

	int[] balloonCount;
	String balloonSize;
	boolean[] picked;
	int[] maxAccepted;
	
	/**
	 * Given the selection of either large or medium balloon for each problem, find out the min repainting needed to fit the set of problems
	 * for large balloons or medium balloons.
	 * largeSelections will be an integer that have k digits in binary, each digit stand for one problem, with 1 means that the problem will use
	 * large balloon while 0 means medium ballooon.
	 * @param largeSelections
	 * @param lorm is 1 for large, 0 for medium.
	 * @param l
	 * @param m
	 * @return
	 */
	public int minRepaintingsForSize(int selections, int k, int lorm, ArrayList<Integer> l, ArrayList<Integer> m) {
		ArrayList<Integer> balloons = lorm==1?l:m;
		ArrayList<Integer> problems = new ArrayList<Integer>();
		int sum = 0;
		for(int i=0;i<k;i++) {
			if((selections & 1<<i)>>i == lorm) {
				problems.add(maxAccepted[i]);
				sum+=maxAccepted[i];
			}
		}
		for(int i=0;i<balloons.size();i++)
			sum -= balloons.get(i);
		if(sum > 0)
			return -1;
		Collections.sort(problems);
		int ret = 0, bi = balloons.size()-1;
		for(int pi=problems.size()-1;pi>=0;pi--) {
			ret+=Math.max(0,problems.get(pi)- (bi<0?0:balloons.get(bi)));
			bi--;
		}
		return ret;
	}
	
	public int minRepaintings(int[] balloonCount, String balloonSize, int[] maxAccepted) {
		this.maxAccepted = maxAccepted;
		int n = balloonCount.length, k = maxAccepted.length;
		ArrayList<Integer> l = new ArrayList<Integer>(), m = new ArrayList<Integer>();
		for(int i=0; i<n; i++)
			(balloonSize.charAt(i)=='L'?l:m).add(balloonCount[i]);
		Collections.sort(l);
		Collections.sort(m);
		int ret = -1;
		//for each problem, it can either choose from L or M balloons
		for(int i=0;i<=1<<k;i++) {
			int lret = minRepaintingsForSize(i, k, 1, l, m), mret = minRepaintingsForSize(i, k, 0, l, m);
			if(lret != -1 && mret != -1 && (ret == -1 || lret + mret < ret))
				ret = lret + mret;
		}
		return ret;
	}
	
	public int min(int a, int b) {
		if(a == -1)
			return b;
		if(b == -1)
			return a;
		return Math.min(a, b);
	}
	
	public int add(int a, int b) {
		if(a==-1 || b==-1)
			return -1;
		return a+b;
	}
	
	int minRepaintings(int i, int mCount, int lCount) {
		if(i == maxAccepted.length)
			return 0;
		int ret = -1;
		if(maxAccepted[i] <= mCount) {
			ret = min(ret, add(maxAccepted[i], minRepaintings(i+1, mCount-maxAccepted[i], lCount)));
			for(int c=0;c<balloonCount.length;c++) {
				if(!picked[c] && balloonSize.charAt(c)== 'M') {
					picked[c] = true;
					ret = min(ret, add(Math.max(0, maxAccepted[i]-balloonCount[c]), minRepaintings(i+1, mCount-maxAccepted[i], lCount)));
					picked[c] = false;
				}
			}
		}
		if(maxAccepted[i] <= lCount) {
			ret = min(ret, add(maxAccepted[i], minRepaintings(i+1, mCount, lCount-maxAccepted[i])));
			for(int c=0;c<balloonCount.length;c++) {
				if(!picked[c] && balloonSize.charAt(c)== 'L') {
					picked[c] = true;
					ret = min(ret, add(Math.max(0, maxAccepted[i]-balloonCount[c]),minRepaintings(i+1, mCount, lCount-maxAccepted[i])));
					picked[c] = false;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Exponential time complexity.
	 * @deprecated
	 * @param balloonCount
	 * @param balloonSize
	 * @param maxAccepted
	 * @return
	 */
	public int minRepaintingsRecursive(int[] balloonCount, String balloonSize, int[] maxAccepted) {
		picked = new boolean[balloonCount.length];
		this.balloonCount = balloonCount;
		this.balloonSize = balloonSize;
		this.maxAccepted = maxAccepted;
		int mCount = 0, lCount = 0;
		for(int i=0;i<balloonCount.length;i++) {
			if(balloonSize.charAt(i)=='M')
				mCount+=balloonCount[i];
			else
				lCount+=balloonCount[i];
		}
		return minRepaintings(0, mCount, lCount);
	}
	
	/**
	 * Missed the condition that balloons for different problems should have distinct colors
	 * @deprecated
	 * @param balloonCount
	 * @param balloonSize
	 * @param maxAccepted
	 * @return
	 */
	public int minRepaintingsNonDistinctColors(int[] balloonCount, String balloonSize, int[] maxAccepted) {
		Balloon[] bs = new Balloon[balloonCount.length];
		for(int i=0;i<bs.length;i++)
			bs[i] = new Balloon(i, balloonSize.charAt(i), balloonCount[i]);
		Arrays.sort(maxAccepted);
		int ret = 0;
		for(int i=0;i<maxAccepted.length;i++) {
			Arrays.sort(bs);
			int j=0;
			while(j<bs.length && bs[j].count < maxAccepted[i]) {
				j++;
			}
			if(j!=bs.length) {
				bs[j].count-=maxAccepted[i];
				continue;
			}
			//combined
			int pos = bs.length-1;
			int remain = maxAccepted[i];
			Balloon first = bs[pos];
			while(pos >= 0) {
				if(bs[pos].size == first.size) {
					if(bs[pos].count >= remain) {
						bs[pos].count-=remain;
						break;
					}
					remain-= bs[pos].count;
				}
				pos--;
			}
			if(pos == -1)
				return -1;
			else
				ret	+= maxAccepted[i]-first.count;
		}
		return ret;
	}
}