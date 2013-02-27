package org.yiouli.algorithm.tree.binarytree;

import org.yiouli.common.ICommand;

public class Traversal {
	
	public void preOrder(Node root, ICommand<Node> visit, ICommand<Node> pre, ICommand<Node> post) {
		if(root == null)
			return;
		if(pre != null)
			pre.execute(root);
		if(visit != null)
			visit.execute(root);
		preOrder(root.left, visit, pre, post);
		preOrder(root.right, visit, pre, post);
		if(post != null)
			post.execute(root);
	}
	
	public void inOrder(Node root, ICommand<Node> visit, ICommand<Node> pre, ICommand<Node> post) {
		if(root == null)
			return;
		if(pre != null)
			pre.execute(root);
		inOrder(root.left, visit, pre, post);
		if(visit != null)
			visit.execute(root);
		inOrder(root.right, visit, pre, post);
		if(post != null)
			post.execute(root);
	}
	
	public void postOrder(Node root, ICommand<Node> visit, ICommand<Node> pre, ICommand<Node> post) {
		if(root == null)
			return;
		if(pre != null)
			pre.execute(root);
		postOrder(root.left, visit, pre, post);
		postOrder(root.right, visit, pre, post);
		if(visit != null)
			visit.execute(root);
		if(post != null)
			post.execute(root);
	}
	
	public void preOrder(Node root, ICommand<Node> visit) {
		if(visit == null)
			throw new IllegalArgumentException();
		preOrder(root, visit, null, null);
	}
		
	public void inOrder(Node root, ICommand<Node> visit) {
		if(visit == null)
			throw new IllegalArgumentException();
		inOrder(root, visit, null, null);
	}
	
	public void postOrder(Node root, ICommand<Node> visit) {
		if(visit == null)
			throw new IllegalArgumentException();
		postOrder(root, visit, null, null);
	}
}
