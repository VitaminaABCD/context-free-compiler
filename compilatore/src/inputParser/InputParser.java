package inputParser;


import contextFree.grammar.IGrammar;

/**
 * An object rappresentation of input file parsing result.
 * @author Paolo Pino
 */
public class InputParser extends AbstractInputParser {

	private String input;
	private IGrammar grammar;
	
	/**
	 * Default constructor.
	 */
	public InputParser(){
		this.input=null;
		this.grammar=null; 
	}
	
	/**
	 * Constructor.
	 * @param in the input file path.
	 */
	public InputParser(String in){
		this.input = in;
		this.grammar=null;
	}
	
	/**
	 * @return the input file path.
	 */
	public String getInput(){
		return this.input;
	}
	
	/**
	 * Set input file path.
	 * @param in the input file path.
	 */
	public void setInput(String in){
		this.input=in;
	}
	
	/**
	 * Return grammar.
	 * @return the grammar field.
	 */
	public IGrammar getGrammar(){
		return this.grammar;
	}
	
	/**
	 * Set a grammar.
	 * @param gr the grammar to set.
	 */
	public void setGrammar(IGrammar gr){
		this.grammar=gr;
	}
	
	@Override
	public Object parse() throws Exception{
		InputParserFactory fc = new ConcreteParserFactory();
		return fc.factoryMethod(input).parse();
	}
}
