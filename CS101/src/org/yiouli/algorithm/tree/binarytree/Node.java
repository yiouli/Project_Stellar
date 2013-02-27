package org.yiouli.algorithm.tree.binarytree;

import java.util.Comparator;

class NodeValueComp implements Comparator<Node> {
	
	@Override
	public int compare(Node a, Node b) {
		return a.value - b.value;
	}
}

public class Node {
	public Node parent;
	public Node left;
	public Node right;
	public int value;
	
	public Node(int value) {
		this.value = value;
	}
	
	public Node(int value, Node parent, Node left, Node right) {
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.value = value;
	}
	
	public Comparator<Node> getComparer() {
		return new NodeValueComp();
	}
}
