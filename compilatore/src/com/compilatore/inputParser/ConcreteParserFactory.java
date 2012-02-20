package com.compilatore.inputParser;

import com.compilatore.error.ERROR_TYPE;
import com.compilatore.error.ErrorManager;


public class ConcreteParserFactory extends ParserFactory{

	@Override
	protected A_InputParser factoryMethod(String in) {
		String[] input = in.split("/");
		String[] type = input[input.length-1].split("\\.");
		if(type[type.length-1].equals("4l"))
			return new FourLineInputParser(in);
		if(type[type.length-1].equals("1l"))
			return new SingleLineInputParser(in);
		ErrorManager.manage(ERROR_TYPE.FILE_EXTENSION);
		
	return null;
	}
}
