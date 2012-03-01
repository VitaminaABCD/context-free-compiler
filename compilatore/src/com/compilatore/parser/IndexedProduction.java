package com.compilatore.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.compilatore.grammar.Production;

public class IndexedProduction extends Production{
	private int currentCharIndex;
	private Set<String> lookahead;
	private List<String> l;
	
	public IndexedProduction(){
		super();
		currentCharIndex=0;
		lookahead = new HashSet<String>();
	}
	
	
	public IndexedProduction(IndexedProduction pro){
		super(pro.getLeft(),pro.getRight());
		currentCharIndex=pro.getCurrentCharIndex();
		if (lookahead==null)
			lookahead = new HashSet<String>();

	}
	
	public IndexedProduction(Production p){
		super(p.getLeft(),p.getRight());
		currentCharIndex=0;
		lookahead = new HashSet<String>();

	}
	
	public IndexedProduction(int i, Production p){
		super(p.getLeft(),p.getRight());
		currentCharIndex=i;
		lookahead = new HashSet<String>();

	}
	
	
	public IndexedProduction(int i, Production p, String la){
		super(p.getLeft(),p.getRight());
		currentCharIndex=i;
		//controlliamo se Lookahead è vuoto ed eventualmente inizializziamo tale insieme prima di inserire elementi
		if(lookahead==null)
			lookahead = new HashSet<String>();
		//aggiungo ttutti gli elementi conntenuti in la
		lookahead.add(la);
	}
	
	
	public IndexedProduction(Production p, Set<String> la){
		super(p.getLeft(),p.getRight());
		currentCharIndex=0;
		//controlliamo se Lookahead è vuoto ed eventualmente inizializziamo tale insieme prima di inserire elementi
		//TODO mi sa che questa creazione del lookahead non va tanto bene.....
		if(lookahead==null)
			lookahead = new HashSet<String>();
		//aggiungo ttutti gli elementi conntenuti in la
		lookahead.addAll(la);
	}
	
	
	public int getCurrentCharIndex() {
		return currentCharIndex;
	}
	
	public String getCharAfter(){
		try{
			return Character.toString(getRight().charAt(currentCharIndex));
		}catch (Exception e) {
			return null;
		}
	}
	
	public String getCharBefore(){
		try{
			return Character.toString(getRight().charAt(currentCharIndex-1));
		}catch (Exception e) {
			return null;
		}
		
	}

	public void setCurrentCharIndex(int currentCharIndex) {
		this.currentCharIndex = currentCharIndex;
	}

	public Set<String> getLookahead() {
		return lookahead;
	}

	public boolean compare(IndexedProduction p){
		if( p.getLeft().equals(getLeft())
					&& 
			p.getRight().equals(getRight())
			) return true;
		else return false;
	}
	
	@Override
	public String toString(){
		String right = super.getRight();
		return super.getLeft()+"->"+ right.substring(0,currentCharIndex) + "." +right.substring(currentCharIndex, right.length()) + lookahead;
//		for(String l : lookahead){
//			result += l + "  ";
//		}
//		return result +"}";
	}


	public void addLookahead(Set<String> lookahead2) {
		this.lookahead.addAll(lookahead2);
	}
	
}
