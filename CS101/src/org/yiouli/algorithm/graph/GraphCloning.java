package org.yiouli.algorithm.graph;

import java.util.Hashtable;

public class GraphCloning {
	
	protected static Vertex cloneVertex(Vertex v, Hashtable<Vertex, Vertex> map) {
		if(map.containsKey(v))
			return map.get(v);
		Vertex ret = new Vertex(v.value);
		map.put(v, ret);
		for(Vertex w : v.neighbors)
			ret.neighbors.add(cloneVertex(w, map));
		return ret;
	}
	
	public static Graph cloneGraph(Graph g) {
		Hashtable<Vertex, Vertex> map = new Hashtable<Vertex, Vertex>();
		Graph ret = new Graph();
		for(Vertex v : g.vertices)
			ret.vertices.add(cloneVertex(v, map));
		return ret;
	}
}
