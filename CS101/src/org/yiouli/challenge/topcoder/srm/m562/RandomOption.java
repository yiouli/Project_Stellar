package org.yiouli.challenge.topcoder.srm.m562;

public class RandomOption {
	
	boolean check(int[] newLanes, int[] badLane1, int[] badLane2) {
		for(int i=0;i<badLane1.length;i++) {
			if(1==Math.abs(newLanes[badLane1[i]]-newLanes[badLane2[i]]))
				return false;
		}
		return true;
	}

	//false when arr is sorted again
	boolean nextPerm(int[] arr) {
		return false;
	}
	
	public double getProbability(int keyCount, int[] badLane1, int[] badLane2) {
		int[] newLanes = new int[keyCount];
		for(int i=0;i<keyCount;i++)
			newLanes[i] = i;
		int ok = 0, total = 0;
		while(nextPerm(newLanes)) {
			if(check(newLanes, badLane1, badLane2))
				ok++;
			total++;
		}
		return ((double)ok)/total;
	}
}
