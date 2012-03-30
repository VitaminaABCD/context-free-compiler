/*! \package contextFree.grammar
    \brief Contains the class for represent and manipulate a grammar.

        USAGE:\n
        You can obtain a Grammar instance through the static factory "GrammarFactor" (see createGrammar(String, List<Production>,List<String>, List<String>));
        you must pass the quadruple Axiom, Productions, terminals, non-terminals that define a grammar.
        \n
        \nexample: 
        \nIGrammar grammar = GrammarFactory.createGrammar(A, P, V, T);
        \n
        \nIf the grammar is context-free the factory return a ContextFreeGrammar instance (see ContextFreeGrammar);
*/

/*! \package contextFree.scanner
\brief Contains class and method for create and initialize grammar parser.

Have been implemented LALR(1) bottom-up parser {LALR1} (LR(0) and LR(1) closure and goto have been implemented in respective class)
The LALR1 automaton is constructed through the LR0 automaton.
The lookahead symbols are calculated using an algorithm of "spontaneous generation and propagation of symbols" {LALR1#calculateSymbol(Automa)}
When the automaton is constructed, if the grammar is LALR(1) the Action and Goto table are created and stored inside the LALR1 instance {LALR1#tableCostruction()}.
\n
\nUSAGE:
\nYou can use the ScannerFactory class to obtain an instance:
\nex:
\n1)you can pass a IGrammar grammar...
\nScannerFactory factory = new ScannerFactory();
\nIScanner lalr1 = factory.createScanner(IGrammar grammar);
\n
\n2)...or an InputParser instance (contains the path of grammar file, es."file.4l", and the grammar instance) 
\n
\nInputParser parser = new GrammarParser(path);         
\nScannerFactory factory = new ScannerFactory();
\nIScanner lalr1 = factory.createScanner(parser);
*/


/*! \package inputParser
\brief Contains the class for file parsing. 

Only 2 extension are allowed:\n
1) *.4l - a file with 4 line that define a grammar:\n
                first line -> axioms\n
                second line -> non-terminals\n
                third line -> terminals\n
                fourth line -> production\n
        example: \n
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
this type of file may contain the action and goto table and the grammar (written like *.1l grammar).\n
 
USAGE:\n
You can obtain the parsing result through the ParserFactory that return 
a proper instance initialized with the result of parsing.\n
\nexample:
\n- get a grammar instance from a grammar file:
\n      IGrammar grammar = (IGrammar) new InputParser("grammar.4l").parse();
\n
\n- get an instance of parser program from a "Result.txt" file (contains action and goto table and the grammar in *.1l format):
\n      ParserProgram parserProgram = (ParserProgram) new InputParser("Result.txt").parse();
*/

/*! \package parserProgram
\brief Contains the parser program's class that to create the ST of an input phrase.

USAGE:\n
\nexample:\n
\nParser parserProgram = (Parser)parser.parse();
\nparserProgram.setInput(input_string);                  
\n              switch(parserProgram.parse()){
\n                      case ACCEPT:    
\n                              St st = new St(parserProgram.getHistory());  //create the ST
\n                              st.initFromHistory();
\n                              return st.getRoot();
\n                      case ERROR:
\n                      return null;
*/