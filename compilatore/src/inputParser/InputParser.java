package inputParser;


import contextFree.grammar.IGrammar;

public class InputParser extends AbstractInputParser {

	private String input;
	private IGrammar grammar;
	
	public InputParser(){
		input="";
		grammar=null; 
	}
	
	public InputParser(String in){
		input = in;
	}
	
	public String getInput(){
		return input;
	}
	
	public void setInput(String in){
		input=in;
	}
	
	public IGrammar getGrammar(){
		return grammar;
	}
	
	public void setGrammar(IGrammar gr){
		grammar=gr;
	}
	
	@Override
	public Object parse() throws Exception{
		InputParserFactory fc = new ConcreteParserFactory();
		return fc.factoryMethod(input).parse();
	}
}
