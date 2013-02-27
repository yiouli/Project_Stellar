package org.yiouli.challenge.codesprint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class SmallestPermutation {

	static int n;
	static int[] a;
	static boolean[][] adj;
	
	static void readInput(InputStreamReader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        String line = br.readLine();
        n = Integer.parseInt(line);
        a = new int[n];
        adj = new boolean[n][n];
        String[] tmp = br.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(tmp[i]);
        }
        for(int i=0;i<n;i++) {
        	line = br.readLine();
        	for(int j=0;j<n;j++)
        		adj[i][j] = line.charAt(j) == 'Y';
        }
	}
	
	static void dfs(int idx, LinkedList<Integer> ids, boolean[] visited) {
		visited[idx] = true;
		ids.add(idx);
		for(int i=0;i<n;i++)
			if(!visited[i] && (adj[idx][i]||adj[i][idx]))
				dfs(i, ids, visited);
	}
	
	//do dfs and remember all positions and values in the tree, place those in sorted order
	static int[] minPermutation() {
		int[] ret = new int[n];
		boolean[] visited = new boolean[n];
		for(int i=0;i<n;i++)
			if(!visited[i]) {
				LinkedList<Integer> ids = new LinkedList<Integer>();
				dfs(i, ids, visited);
				int[] vals = new int[ids.size()];
				int pos = 0;
				for(Integer idx : ids) {
					vals[pos] = a[idx];
					pos++;
				}
				Arrays.sort(vals);
				Collections.sort(ids);
				pos = 0;
				for(Integer idx : ids) {
					ret[idx] = vals[pos];
					pos++;
				}
			}
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length > 0)
			readInput(new FileReader(args[0]));
		else
			readInput(new InputStreamReader(System.in));
		int[] perm = minPermutation();
		boolean first = true;
		for(int i=0;i<n;i++) {
			if(first)
				first = false;
			else
				System.out.print(" ");
			System.out.print(perm[i]);
		}
	}
}
