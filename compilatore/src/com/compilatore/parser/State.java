package com.compilatore.parser;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class State {
	private int index;
	private List<IndexedProduction> items;

	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public State(){
		this.index=0;
		this.items = new ArrayList<IndexedProduction>();
	}
	
	public State(int i){
		this.index=i;
		this.items = new ArrayList<IndexedProduction>();
	}
	
	public State(int i, List<IndexedProduction> c){
		this.index=i;
		this.items = c;
	}
	
	public List<IndexedProduction> getItems() {
		return items;
	}

	public void setItems(List<IndexedProduction> Items) {
		this.items = Items;
	}
	
	/**
	 * ritorna la dimensione dello stato
	 * @return
	 */
	public int size(){
		return size();
	}
	/**
	 * ritorna una List<IndexedProduction> contenente il Kernel di uno stato
	 * @return j
	 */
	public List<IndexedProduction> getKernels(){
		 IndexedProduction prod = new IndexedProduction();
		List<IndexedProduction> j =new LinkedList<IndexedProduction>();
		//se ci troviamo nello stato 0
		if(index==0){
			//solo la produzione S'::=S appartiene al kernel
			 Iterator<IndexedProduction> it = items.iterator();
			 //per ogni item dello stato
			 while(it.hasNext()){
				 prod = it.next();
				 j.add(prod);
				 break;
			 }
		}
		//se non siamo nello stato 0
		 else{
			 Iterator<IndexedProduction> it = items.iterator();
			 //per ogni item dello stato
			 while(it.hasNext()){
				 prod = it.next();
				 //vediamo in che posizione si trova il puntino, perchè solo le produzionioni che hanno il 
				 //puntino in una posizione diversa dalla prima faranno parte del kernel
				 if(prod.getCurrentCharIndex()!=0){
					//quindi la aggiungiamo a J
					 IndexedProduction p = new IndexedProduction(prod);
				 	 p.getLookahead().addAll(prod.getLookahead());
					 j.add(p);
				 }
			 }
		 }
		return j;
	}
	
	@Override
	public String toString(){
		String result= "State number: "+index+"\n";
		for(IndexedProduction i : items){
			result += i.toString() + "\n";
		}
		return result;		
	}
	
}
