package parserProgram;

import javax.swing.tree.DefaultMutableTreeNode;

public class NodeStBookmarks {
	private DefaultMutableTreeNode node;
	private int childNumber;
	
	public NodeStBookmarks(){
		this.node=null;
		this.childNumber=0;
	}
	
	public NodeStBookmarks(DefaultMutableTreeNode nd, int number){
		this.node=nd;
		this.childNumber=number;
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	public void setNode(DefaultMutableTreeNode node) {
		this.node = node;
	}

	public int getChildNumber() {
		return childNumber;
	}

	public void setChildNumber(int childNumber) {
		this.childNumber = childNumber;
	}
	
	public void decrementChildNumber(){
		this.childNumber--;
	}

	public void incrementChildNumber() {
		this.childNumber++;		
	}
	
}
