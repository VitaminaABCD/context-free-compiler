package contextFree.parser;

import contextFree.grammar.IGrammar;

public interface IParser {
	public void setGrammar(IGrammar gram);
	public int init() throws Exception;
	public IGrammar getGrammar();
	boolean isAmbiguos();
	Automa getAutoma();
}
