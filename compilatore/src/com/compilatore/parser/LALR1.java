package com.compilatore.parser;

import java.util.*;

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
		
//		for(IndexedProduction ki : AutomaLR0.getKernels()){
//			if(ki.getLeft()!=""){
//				
//			}
//		}
		
		List<State> states = AutomaLR0.getStates();
		for(State s : states){
			calculateLookahead(s.getKernels(), new String());  //bisogna passare il simbolo grammaticale...
		}
		
	}
	
	
	public List<String> calculateLookahead(List<IndexedProduction> kernels, String simbol){
		List<String> result = new LinkedList<String>();
		for(IndexedProduction LR0kernel: kernels){
			
		}
		
		return result;
	}
}
