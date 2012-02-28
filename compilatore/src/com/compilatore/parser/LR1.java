package com.compilatore.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.compilatore.grammar.IGrammar;
import com.compilatore.grammar.Production;

public  abstract class LR1 {
	
	protected IGrammar grammatica;

	public abstract void setGrammar(IGrammar gram);
}