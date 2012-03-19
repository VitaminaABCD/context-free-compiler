package contextFree.parser;


import java.util.HashSet;
import java.util.Set;

import contextFree.grammar.Production;


public class IndexedProduction extends Production{
	private int currentCharIndex;
	private Set<String> lookahead;
	
	public IndexedProduction(){
		super();
		currentCharIndex=0;
		lookahead = new HashSet<String>();
	}
	
	
	public IndexedProduction(IndexedProduction pro){
		super(pro.getLeft(),pro.getRight());
		currentCharIndex=pro.getCurrentCharIndex();
		lookahead=pro.getLookahead();
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
		//controlliamo se Lookahead � vuoto ed eventualmente inizializziamo tale insieme prima di inserire elementi
		if(lookahead==null)
			lookahead = new HashSet<String>();
		//aggiungo ttutti gli elementi conntenuti in la
		lookahead.add(la);
	}
	
	
	public IndexedProduction(Production p, Set<String> la){
		super(p.getLeft(),p.getRight());
		currentCharIndex=0;
		//controlliamo se Lookahead � vuoto ed eventualmente inizializziamo tale insieme prima di inserire elementis
		if(lookahead==null)
			lookahead = new HashSet<String>();
		//aggiungo ttutti gli elementi conntenuti in la
		lookahead.addAll(la);
	}
	
	
	public int getCurrentCharIndex() {
		return currentCharIndex;
	}
	
	/**
	 * Return the next character that that will be read
	 * @return the character after dot in the production
	 */
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

	/**
	 * 
	 * @return a reference to the lookahead list
	 */
	public Set<String> getLookahead() {
		return lookahead;
	}

	/**
	 * Compare to production without the dot
	 * @param p production to compare
	 * @return true if they are equal, false otherwise 
	 */
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


	public boolean addLookahead(Set<String> lookahead2) {
		return this.lookahead.addAll(lookahead2);
	}
	
}