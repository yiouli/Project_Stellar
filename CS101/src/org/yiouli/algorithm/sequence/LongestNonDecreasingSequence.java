package org.yiouli.algorithm.sequence;

public class LongestNonDecreasingSequence {
	public static int lnds(int[] s) {
		int n = s.length;
		//minEnd[i] is the end point value of the non-decreasing subsequence with length i+1
		//observation: after processing the sequence, minEnd will be in non-decreasing order
		int[] minEnd = new int[n];
		for(int i=0;i<n;i++)
			minEnd[i] = Integer.MAX_VALUE;
		minEnd[0] = s[0];
		int ret = 1;
		//each iteration using s[i] to update minEnds, 
		//set minEnd[j] = s[i] for j that j <= i and minEnd[j-1] <= s[i] < minEnd[j]
		//set minEnd[0] = s[i] if s[i] is smaller than minEnd[0]
		for(int i=1;i<n;i++) {
			//search for j where j is the largest that minEnd[j]<= s[i]
			//this case will search minEnd[0]-mindEnd[i-1] since the longest
			//subsequence in s[0]...s[i-1] won't be longer than i
			//idx can be i-1
			int idx = binaryIntervalSearch(minEnd, 0, i, s[i]);
			minEnd[idx+1]=s[i];
			if(idx + 2 > ret)
				ret= idx + 2;
		}
		return ret;
	}

	//same as lnds but store index for min end to recover the sequence
	public static int[] LNDSResult(int[] s) {
		int n = s.length;
		int[] mei = new int[n];
		for(int i=1;i<n;i++)
			mei[i] = -1;
		int[] trace = new int[n];
		for(int i=0;i<n;i++)
			trace[i] = -1;
		int len = 1;
		for(int i=1;i<n;i++) {
			int idx = findIndex(s, mei, 0, i, s[i]);
			mei[idx+1]=i;
			//-1 if nothing smaller than s[i]
			if(idx != -1)
				trace[i] = mei[idx];
			if(idx + 2 > len)
				len = idx + 2;
		}
		int[] ret = new int[len];
		int prev = mei[len-1], pos = len - 1;
		do {
			ret[pos] = s[prev];
			prev = trace[prev];
			pos--;
		} while(prev != -1);
		return ret;
	}
	
	protected static int findIndex(int[] s, int[] mei, int start, int end, int value) {
		if(end<=start || start<0 || end>=mei.length)
			throw new IllegalArgumentException();
		if(mei[end]!=-1 && value>=s[mei[end]])
			return end;
		if(mei[start]==-1 || value<s[mei[start]])
			return start-1;
		int pos = (start+end)/2;
		while(pos!=start){
			if(mei[pos]==-1 || value<s[mei[pos]])
				end=pos;
			else if(mei[pos+1]!=-1 && value>=s[mei[pos+1]])
				start=pos;
			else
				return pos;
			pos=(start+end)/2;
		}
		return pos;
		
	}
	
		//arr is a non-decreasing array
		//find i such start<=i<=end arr[i] <= value < arr[i+1]
		//return start-1 if value<arr[start]
		//return end if value>=arr[end]
	public static int binaryIntervalSearch(int[] arr, int start, int end, int value) {
		if(end<=start || start<0 || end>=arr.length)
			throw new IllegalArgumentException();
		if(value>=arr[end])
			return end;
		if(value<arr[start])
			return start-1;
		int pos = (start+end)/2;
		while(pos!=start){
			if(value >= arr[pos] && value<arr[pos+1])
				return pos;
			if(value<arr[pos])
				end=pos;
			else if(value>=arr[pos+1])
				start=pos;
			pos=(start+end)/2;
		}
		return pos;
	}
}
