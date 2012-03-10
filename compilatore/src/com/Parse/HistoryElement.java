package com.Parse;

import com.compilatore.grammar.Production;

public class HistoryElement {
	private String[] stack;
	private Production production;
	
	public HistoryElement(){
		this.stack=null;
		this.production=null;
	}

	public HistoryElement(String[] stk) {
		this.stack= stk;
		production=null;
	}
	
	public HistoryElement(String[] stk,Production p) {
		this.stack= stk;
		production=p;
	}	
	
	@Override
	public String toString(){
		String result= "[";
			for(String simbolo : stack){
				result+=simbolo+",";
			}
			result+="]";
		if(production!=null) result+=" | reduce for "+production.toString();
		else result+= " | shift";
		return result;
	}	
}
