package org.yiouli.algorithm.tree.binarytree;

import java.util.Comparator;

public class LowestCommonAncestor {

	public static Node findRoot(Node a) {
		if(a == null)
			throw new IllegalArgumentException();
		Node ret = a;
		while(ret.parent != null) {
			ret = ret.parent;
		}
		return ret;
	}
	
	public static Node LCAforBST(Node a, Node b, Node root) {
		if(root == null)
			return null;
		if(a == null || b==null || a.value == root.value || b.value == root.value)
			throw new IllegalArgumentException();
		if(a.value < root.value && b.value < root.value)
			return LCAforBST(a, b, root.left);
		else if(a.value > root.value && a.value > root.value)
			return LCAforBST(a, b, root.left);
		else
			return root;
	}
	
	public static Node LCAforBST(Node a, Node b) {
		if(a == null || b==null)
			throw new IllegalArgumentException();
		Node root = findRoot(a);
		return LCAforBST(a, b, root);
	}
	
	protected static boolean DFSBranch(Node node, Node root, boolean left, Comparator<Node> comp) {
		if(root == null)
			return false;
		if(comp.compare(node, root) == 0)
			return true;
		if(left)
			return Search.DFS(node, root.left, comp);
		else
			return Search.DFS(node, root.right, comp);
	}
	
	public static Node LCAforBinaryTree(Node a, Node b) {
		if(a == null || b==null)
			throw new IllegalArgumentException();
		Comparator<Node> comp = a.getComparer();
		//if a is ancestor of b, including a equals b
		if(Search.DFS(b, a, comp))
			return a;
		Node parent = a.parent;
		while(parent != null) {
			boolean searchLeft = parent.right == a;
			if(DFSBranch(b, parent, searchLeft, comp))
				break;
			a = parent;
			parent = a.parent;
		}
		return parent;
	}
}
