package org.yiouli.challenge.codesprint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

class Edge implements Comparable<Edge> {
	public int u;
	public int v;
	public int len;
	
	Edge(int i, int j, int c) {
		u = i;
		v = j;
		len = c;
	}
	
	@Override
	public int compareTo(Edge e) {
		return len-e.len;
	}
}

class TestCase {
	public int n;	//node number
	public int m;	//edge number
	public int k;	//tree number
	public Edge[] edges;
	
	TestCase(int n, int m, int k) {
		this.n = n;
		this.m = m;
		this.k = k;
		edges = new Edge[m];
	}
}

public class PowerGrid {

	static LinkedList<TestCase> ts = new LinkedList<TestCase>();
	
	//search whether can get to v from u
	static boolean dfs(int u, int v, boolean[][] g, boolean[] visited) {
		if(u == v)
			return true;
		visited[u] = true;
		for(int i=0;i<g[u].length;i++)
			if(!visited[i] && g[u][i] && dfs(i,v,g, visited))
				return true;
		return false;
	}
	
	static String minGrid(TestCase t) {
		int ret = 0;
		int treeCount = t.n;
		Arrays.sort(t.edges);
		//group index, same gi means in same tree
		int[] gis = new int[t.n];
		for(int ni=0;ni<t.n;ni++)
			gis[ni] = ni;
		//boolean[][] g = new boolean[t.n][t.n];
		for(int ei=0;ei<t.m;ei++) {
			if(treeCount <= t.k)
				break;
			//if create cycle continue here
			Edge e = t.edges[ei];
			if(gis[e.u] != gis[e.v]) {
				int gi = gis[e.v];
				for(int ni=0;ni<t.n;ni++)
					if(gis[ni] == gi)
						gis[ni] = gis[e.u];
				ret += e.len;
				treeCount--;
			}
			/*
			 * Too slow
			if(!dfs(e.u, e.v, g, new boolean[t.n])) {
				g[e.u][e.v] = g[e.v][e.u] = true;
				ret += e.len;
				treeCount--;
			}
			*/
		}
		if(treeCount <= t.k)
			return String.valueOf(ret);
		return "Impossible!";
	}
	
	static void readInput(InputStreamReader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        String line = br.readLine();
        int tc = Integer.parseInt(line);
        for(int ti=0;ti<tc;ti++) {
        	String[] tmp = br.readLine().split(" ");
        	int n = Integer.parseInt(tmp[0]);
        	int m = Integer.parseInt(tmp[1]);
        	int k = Integer.parseInt(tmp[2]);
        	TestCase t = new TestCase(n, m, k);
        	for(int l=0;l<m;l++) {
        		tmp = br.readLine().split(" ");
            	int i = Integer.parseInt(tmp[0]);
            	int j = Integer.parseInt(tmp[1]);
            	int c = Integer.parseInt(tmp[2]);
            	Edge e = new Edge(i-1, j-1, c);
            	t.edges[l] = e;
        	}
        	ts.add(t);
        }
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length > 0)
			readInput(new FileReader(args[0]));
		else
			readInput(new InputStreamReader(System.in));
		for(TestCase t : ts)
			System.out.println(minGrid(t));
	}
}
