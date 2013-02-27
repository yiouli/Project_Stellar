package org.yiouli.algorithm.graph;

import java.util.LinkedList;

class Vertex {
	int value;
	LinkedList<Vertex> neighbors;
	
	Vertex(int value) {
		this.value = value;
		this.neighbors = new LinkedList<Vertex>();
	}
}

class Graph {
	LinkedList<Vertex> vertices;
	
	public Graph() {
		vertices = new LinkedList<Vertex>();
	}
}
