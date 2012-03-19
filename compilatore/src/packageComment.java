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


/*! \package inputParser
\brief Contains the class for file parsing. 
Only 2 extension are allowed:\n
1) *.4l - a file with 4 line that define a grammar:\n
	 	first line -> axioms\n
	 	second line -> non-terminlas\n
	 	third line -> terminals\n
	 	fourth line -> production\n
 	es: \n
 	//////////grammar.4l//////////\n
 	E\n
	E, T, P\n
	a, +, x, (, ), $\n
	E::=E+T, E::=T, T::=TxP, T::=P, P::=a, P::=(E)\n
 	/////////////////////////////\n
 	
 2) *.1l - a file with 1 line that define a grammar.\n
 the  structure may look like this...\n
 	S::= { S: TE | +TE; T: FT | xFT; E : eps; F: a | (E)}\n
 ...where the first element is the axioms and all the element between\n 
 the bracket are the production.\n
 This format is pretty convenient associated with Action and Goto table for
 context-free grammar type.\n
 
You can also parse ".txt" file through LRInputParser
this tyoe of file may contain the action and goto table and the grammar (written like *.1l grammar).\n
 
USAGE:\n
You can obtain the parsing result through the ParserFactory that return 
a proper instance initialized with the result of parsing.\n
\nes.
\nInputParser parser = new GrammarParser(path); 	//return a parser for a grammar file (es. *.txt)
\nParsingFactory p = new ParsingFactory();		//the factory
\nIParsing lalr1 = p.createParsing(parser);		//return an lalr1 instance initialized  
*/