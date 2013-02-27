package org.yiouli.course.coursera.algorithmp1.week1;

public class Percolation {
	
	protected UnionFind unionFind;
	protected int openCellCount;
	protected boolean[] isOpen;
	protected final int n;
	protected final int topIdx;
	protected final int bottomIdx;
	
	public Percolation(int N) {
		n = N;
		int gridSize = (N+2)*(N+2);
		topIdx = gridSize;
		bottomIdx = gridSize+1;
		unionFind = new UnionFind(gridSize+2);
		openCellCount = 0;
		isOpen = new boolean[gridSize+2];
		isOpen[topIdx] = isOpen[bottomIdx] = true;
		for(int i=0;i<N+1;i++)
		{
			isOpen[i] = isOpen[gridSize-1-i] = true;
			unionFind.union(topIdx, i);
			unionFind.union(bottomIdx, gridSize-1-i);
		}
	}
	
	protected int getIndex(int i, int j) {
		return j+1+(n+2)*(i+1);
	}
   
	public void open(int i, int j) {
		isOpen[getIndex(i,j)] = true;
		if(isOpen[getIndex(i,j+1)])
			unionFind.union(getIndex(i,j), getIndex(i,j+1));
		if(isOpen[getIndex(i,j-1)])
			unionFind.union(getIndex(i,j), getIndex(i,j-1));
		if(isOpen[getIndex(i+1,j)])
			unionFind.union(getIndex(i,j), getIndex(i+1,j));
		if(isOpen[getIndex(i-1,j)])
			unionFind.union(getIndex(i,j), getIndex(i-1,j));
	}
   
	public boolean isOpen(int i, int j) {
		return isOpen[getIndex(i,j)];
	}
   
	public boolean isFull(int i, int j) {
		return unionFind.connected(topIdx, getIndex(i,j));
	}
   
	public boolean percolates() {
	   return unionFind.connected(topIdx, bottomIdx);
	}
	
	public void print()
	{
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				int idx = getIndex(i,j);
				char c = isOpen[idx] ? '.':'*';
				System.out.print(c);
			}
			System.out.println();
		}
	}
}

class UnionFind {
	
	int[] size;
	int[] parent;
	
	UnionFind(int N) {
		size = new int[N];
		parent = new int[N];
		for(int i=0;i<N;i++) {
			size[i] = 1;
			parent[i] = i;
		}
	}
	
	void union(int a, int b)
	{
		int roota = find(a);
		int rootb = find(b);
		if(roota != rootb) {
			if(size[roota] > size[rootb]) {
				parent[rootb] = roota;
				size[roota] += size[rootb];
			}
			else {
				parent[roota] = rootb;
				size[rootb] += size[roota];
			}
		}
	}
	
	int find(int a)
	{
		while(parent[a] != a)
		{
			int par = parent[a];
			parent[a] = parent[par];
			a = par;
		}
		return a;
	}
	
	boolean connected(int a, int b)
	{
		return find(a) == find(b);
	}
}
