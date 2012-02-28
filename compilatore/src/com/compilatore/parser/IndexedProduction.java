package com.compilatore.parser;

import java.util.HashSet;
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
	
	
	public IndexedProduction(int i, Production p, String la){
		super(p.getLeft(),p.getRight());
		currentCharIndex=i;
		//TODO mi sa che questa creazione del lookahead non va tanto bene.....
		lookahead = new HashSet<String>();
		lookahead.add(la);
	}
	
	public int getCurrentCharIndex() {
		return currentCharIndex;
	}

	public void setCurrentCharIndex(int currentCharIndex) {
		this.currentCharIndex = currentCharIndex;
	}

	public Set<String> getLookahead() {
		return lookahead;
	}

	public void setLookahead(Set<String> lookahead) {
		this.lookahead = lookahead;
	}
	
}
