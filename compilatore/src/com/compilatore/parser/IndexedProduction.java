package com.compilatore.parser;

import java.util.Set;

import com.compilatore.grammar.Production;

public class IndexedProduction extends Production{
	private int currentCharIndex;
	private Set<String> lookahead;
	
	public IndexedProduction(){
		super();
		currentCharIndex=0;
	}
	
	public IndexedProduction(Production p){
		super(p.getLeft(),p.getRight());
		currentCharIndex=0;
	}
	
	public IndexedProduction(int i, Production p){
		super(p.getLeft(),p.getRight());
		currentCharIndex=i;
	}
	public int getCurrentCharIndex() {
		return currentCharIndex;
	}

	public void setCurrentCharIndex(int currentCharIndex) {
		this.currentCharIndex = currentCharIndex;
	}
	
}
