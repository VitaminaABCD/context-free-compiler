package com.compilatore.parser;

import com.compilatore.grammar.IGrammar;

public interface IParsing {
	public void setGrammar(IGrammar gram);
	public int init() throws Exception;
}
