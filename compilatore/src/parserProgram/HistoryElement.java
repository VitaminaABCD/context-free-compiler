package parserProgram;


import contextFree.grammar.Production;


/**
 * This class represent an element of the parsing history
 * @author Paolo Pino
 */
public class HistoryElement {
	private String[] stack;
	private Production production;
	private String simbol;
	
	/**
	 * Default constructor.
	 */
	public HistoryElement(){
		this.stack=null;
		this.simbol=null;
		this.production=null;
	}

	/**
	 * Construct the object with specified parsing stack.
	 * @param stk the stack to set.
	 */
	public HistoryElement(String[] stk) {
		this.stack= stk;
		this.simbol=null;
		this.production=null;
	}
	
	/**
	 * Construct the object with specified parsing stack, production and step.
	 * @param p the production.
	 * @param s the step.
	 * @param stk the stack to set.
	 */
	public HistoryElement(String[] stk,Production p, String s) {
		this.stack= stk;
		this.production=p;
		this.simbol= s;
	}	
	
	/**
	 * @return the stack
	 */
	public String[] getStack() {
		return stack;
	}

	/**
	 * Set the stack
	 * @param stack the stack.
	 */
	public void setStack(String[] stack) {
		this.stack = stack;
	}

	/**
	 * @return the production in the history element
	 */
	public Production getProduction() {
		return production;
	}

	/**
	 * Set the production.
	 * @param production the production to set.
	 */
	public void setProduction(Production production) {
		this.production = production;
	}

	/**
	 * @return the history step.
	 */
	public String getSimbol() {
		return simbol;
	}

	/** Set the parsing step.
	 * @param simbol the parsing step.
	 */
	public void setSimbol(String simbol) {
		this.simbol = simbol;
	}

	/**
	 * @return true if the history element execute a shift.
	 */
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
