/*! \package contextFree.grammar
    \brief Contains the class for represent and manipulate a grammar.

	USAGE:
	You can obtain a Grammar instance through the static factory "GrammarFactor" {@link GrammarFactory#createGrammar(String, List<Production>,List<String>, List<String>) createGrammar};
	you must pass the quadruple Axiom, Productions, terminals, non-terminals that define a grammar.
	
	es: 
	IGrammar grammar = GrammarFactory.createGrammar(A, P, V, T);
	
	If the grammar is context-free the factory return a ContextFreeGrammar instance {@link ContextFreeGrammar};
*/

/*! \package contextFree.parser
\brief Contains class and method for create and initialize grammar parser.

Have been implemented LALR(1) bottom-up parser (LALR1) (LR(0) and LR(1) closure and goto have been implemented in respective class)
The LALR1 automaton is constructed through the LR0 automaton.
The lookahead symbols are calculated using an algorithm of "spontaneous generation and propagation of symbols" (LALR1.calculateSymbol(Automa))
When the automaton is constructed, if the grammar is LALR(1) the Action and Goto table are created and stored inside the LALR1 instance (LALR1.tableCostruction()).\n

USAGE:
You can use the ParsingFactory class to obtain an instance:\n
es:\n
1)you can pass a IGrammar grammar...\n
ParsingFactory factory = new ParsingFactory();\n
IParsing lalr1 = factory.createParsing(IGrammar grammar);\n
\n
2)...or an InputParser instance (contains the path of grammar file, es."file.4l", and the grammar instance)\n 

InputParser parser = new GrammarParser(path);\n
ParsingFactory factory = new ParsingFactory();\n
IParsing lalr1 = factory.createParsing(parser);\n
*/