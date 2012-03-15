package com.Parse;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class Ast {
	
	private List<HistoryElement> history;
	private DefaultMutableTreeNode root;
	
	public Ast(){
		this.history=null;
		this.root=null;
	}
	
	public Ast(List<HistoryElement> h){
		this.history=h;
		this.root = new DefaultMutableTreeNode("ROOT");
	}	
	
	public boolean initFromHistory(){
		int size = this.history.size();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.history.get(size-1));
		char temp[] = this.history.get(size-1).getProduction().getRight().toCharArray();
		for(char c : temp){
			root.add(new DefaultMutableTreeNode(c));
		}
		if(size < 2) return true;
		for(int i=0; i<root.getChildCount();i++){
			root.getChildAt(i);
		}
		for(int i=this.history.size()-2;i>=0;i--){
			//root.
			//TODO: da fare...
		}
		return false;
	}
	
}




//
//DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
//DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Child 1");
//root.add(child1);
//DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Child 2");
//root.add(child2);