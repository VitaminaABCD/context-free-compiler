package com.compilatore.inputParser;

public abstract class ParserFactory{

	public void parse(String in){
		A_InputParser parser = factoryMethod(in);
	}
	
	protected abstract A_InputParser factoryMethod(String in);
}
