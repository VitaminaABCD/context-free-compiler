package com.compilatore.parser;

import java.util.LinkedList;
import java.util.List;

public class Automa {
	private List<State> states;

	public Automa(){
		states = new LinkedList<State>();
	}
	
	public Automa(List<State> states){
		this.states= states;
	}
	
	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}
	
	/* Get kernels element for each states into automa
	 * gli stati sono separati da una produzione vuota
	 * @return the kernel element of the automa
	 */
	public List<IndexedProduction> getKernels(){
		List<IndexedProduction> kernels = new LinkedList<IndexedProduction>();
		//per ogni stato nell'automa
		for(State s : states){
			kernels.addAll(s.getKernels());
			kernels.add(new IndexedProduction());
		}
		return kernels;
	}
}
