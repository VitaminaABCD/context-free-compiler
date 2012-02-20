package com.compilatore.parser;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

public class State {
	private int index;
	private List<IndexedProduction> items;

	
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
	 * ritorna una List<IndexedProduction> contenente il Kernel di uno stato
	 * @return j
	 */
	public List<IndexedProduction> getKernels(){
		 IndexedProduction prod = new IndexedProduction();
		List<IndexedProduction> j =new ArrayList<IndexedProduction>();
		//se ci troviamo nello stato 0
		if(index==0){
			//tutte gli item appartenenti allo stato fanno parte del kernel
			j=items;
		}
		//se non siamo nello stato 0
		 else{
			 Iterator<IndexedProduction> it = items.iterator();
			 //per ogni item dello stato
			 while(it.hasNext()){
				 prod = it.next();
				 //vediamo in che posizione si trova il puntino, perchè solo le produzionioni che hanno il 
				 //puntino un una posizione diversa dalla prima faranno parte del kernel
				 if(prod.getCurrentCharIndex()!=0)
					//quindi la aggiungiamo a J
					 j.add(new IndexedProduction(prod));
			 }
		 }
		return j;
	}
}
