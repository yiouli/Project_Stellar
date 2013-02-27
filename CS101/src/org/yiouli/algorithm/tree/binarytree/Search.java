package org.yiouli.algorithm.tree.binarytree;

import java.util.Comparator;
import java.util.LinkedList;

public class Search {

	public static boolean DFS(Node node, Node root) {
		if(node == null)
			throw new IllegalArgumentException();
		if(root == null)
			return false;
		if(root == node)
			return true;
		return DFS(node, root.left) || DFS(node, root.right);
	}
	
	public static boolean DFS(Node node, Node root, Comparator<Node> comparer) {
		if(node == null)
			throw new IllegalArgumentException();
		if(root == null)
			return false;
		if(comparer.compare(root, node) == 0)
			return true;
		return DFS(node, root.left) || DFS(node, root.right);
	}
	
	public static boolean BFS(Node node, Node root) {
		if(node == null)
			throw new IllegalArgumentException();
		LinkedList<Node> q = new LinkedList<Node>();
		//HashSet<Node> visited = new HashSet<Node>();
		q.add(root);
		while(!q.isEmpty()) {
			Node n = q.removeFirst();
			if(n == null /*|| visited.contains(n)*/)
				continue;
			if(n == node)
				return true;
			q.add(n.left);
			q.add(n.right);
		}
		return false;
	}
}
