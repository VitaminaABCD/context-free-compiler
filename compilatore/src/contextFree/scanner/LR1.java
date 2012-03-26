package contextFree.scanner;


import contextFree.grammar.IGrammar;

public  abstract class LR1 {
	
	protected IGrammar grammatica;

	public abstract void setGrammar(IGrammar gram);
}