package com.compilatore.parser;

import java.util.ArrayList;
import java.util.List;

import com.compilatore.grammar.IGrammar;

public class LALR1 extends LR0{
	private List<IndexedProduction> kernel; 
	
	public LALR1(){
		grammatica=null;
		kernel=new ArrayList<IndexedProduction>();
	}
	
	public LALR1(IGrammar gram){
		setGrammar(gram);
		kernel=new ArrayList<IndexedProduction>();
	}
	
	@Override
	public void setGrammar(IGrammar gram) {
		grammatica=gram;
	}
	
	@Override
	public void calculateKernels(){
		Automa AutomaLR0 = new Automa(Item());
		AutomaLR0.getKernels();
		int x=0;
	}
	
}
