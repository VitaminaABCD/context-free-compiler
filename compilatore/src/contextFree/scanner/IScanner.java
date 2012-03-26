package contextFree.scanner;

import contextFree.grammar.IGrammar;

public interface IScanner {
	public void setGrammar(IGrammar gram);
	public int init() throws Exception;
	public IGrammar getGrammar();
	boolean isAmbiguos();
	Automa getAutoma();
}
