package com.Parse;

import java.util.List;

import com.compilatore.grammar.Production;

/**
 * This class rappresent an element of the history
 * @author Paolo Pino
 *
 */
public class HistoryElement {
	private String[] stack;
	private Production production;
	private String simbol;
	
	public HistoryElement(){
		this.stack=null;
		this.simbol=null;
		this.production=null;
	}

	public HistoryElement(String[] stk) {
		this.stack= stk;
		this.simbol=null;
		production=null;
	}
	
	public HistoryElement(String[] stk,Production p, String s) {
		this.stack= stk;
		production=p;
		this.simbol= s;
	}	
	
	public String[] getStack() {
		return stack;
	}

	public void setStack(String[] stack) {
		this.stack = stack;
	}

	/**
	 * 
	 * @return the production in the history element
	 */
	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
	}

	public String getSimbol() {
		return simbol;
	}

	public void setSimbol(String simbol) {
		this.simbol = simbol;
	}

	public boolean isShift(){
		if(production==null) return true;
		return false;
	}
	
	
	@Override
	public String toString(){
		String result= "[ ";
			for(String simbolo : this.stack){
				result+=simbolo+" ";
			}
			result+="]";
		if(production!=null) result+=" | reduce for "+production.toString() + " | " + simbol;
		else result+= " | shift | " + simbol;
		return result;
	}	
}
