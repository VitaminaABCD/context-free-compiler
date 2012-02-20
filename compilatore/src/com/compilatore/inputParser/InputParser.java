package com.compilatore.inputParser;


import com.compilatore.grammar.IGrammar;

public class InputParser {

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
	
	public IGrammar parse(){
		ParserFactory fc = new ConcreteParserFactory();
		return fc.factoryMethod(input).parse();
	}
}
