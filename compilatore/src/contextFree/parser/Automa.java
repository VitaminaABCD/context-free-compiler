package contextFree.parser;

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
	
	/** Get kernels element for each states into automa
	 * @return new State list with shift value and kernel item only
	 */
	public List<State> newItemsFromKernels(){
		List<State> result= new LinkedList<State>();
		//per ogni stato nell'automa
		for(State s : states){
			result.add(new State(s.getIndex(), s.getKernels(),s.getShift()));

		}
		return result;
	}

	/**	Return a state with a specific state index
	 * 	@param i the index of desired state
	 *  @return the State reference if the state exist else null;
	 */	
	public State getState(int i){
		for(State s : states){
			if(s.getIndex()==i) return s;
		}
		return null;
	}
	
	@Override
	public String toString(){
		String result = "";
		for(State s : this.states) result+=s.toString();
		return result;
	}

	/**
	 * remove dollar simbol lookahed
	 * @author Paolo Pino
	 */
	public void removeDollarLookahed() {
		for(State s : states){
			if(s.getIndex()!=0)
				for(IndexedProduction p : s.getItems()){
					p.getLookahead().remove("$");
				}
		}
	}
	
	public int size(){
		return this.states.size();
	}
}
