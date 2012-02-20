package com.compilatore.grammar;

import java.util.ArrayList;
import java.util.List;

public class Production {

	private String left;
	private String right;
	private List <String> rightList;


	public Production(){
		left="";
		right="";
	}
	
	public Production(String lt, String rt){
		left=lt;
		right=rt;
		rightList = new ArrayList<String>();
	}
	
	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}
	
	public List<String> getRightList() {
		if(rightList==null)
			this.rightList =new ArrayList<String>();
		else{
			if(rightList.size()!=right.length()){
				char[] temp = right.toCharArray();
				for(int i=0;i<temp.length;i++)
					this.rightList.add(Character.toString(temp[i]));
			}
		}
		return rightList;
	}

	public void setRightList(List<String> rightList) {
		this.rightList = rightList;
	}
	/**
	 * restiuisce una stringa formattata nella forma ass::=espr
	 */
	@Override
	public String toString() {
		if (right.equals(" "))
			return left + "::=" + "eps";
		else{
				return left + "::=" + right ;
		}
	}
}
