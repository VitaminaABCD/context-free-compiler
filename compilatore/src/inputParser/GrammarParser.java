package inputParser;


import contextFree.grammar.IGrammar;

public class GrammarParser extends InputParser {

	private String input;
	private IGrammar grammar;
	
	public GrammarParser(){
		input="";
		grammar=null; 
	}
	
	public GrammarParser(String in){
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
	public IGrammar parse() throws Exception{
		ParserFactory fc = new ConcreteParserFactory();
		return (IGrammar)fc.factoryMethod(input).parse();
	}
	
	
}
