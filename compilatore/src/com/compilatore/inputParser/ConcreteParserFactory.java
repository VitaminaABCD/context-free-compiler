package com.compilatore.inputParser;

import com.compilatore.error.ERROR_TYPE;
import com.compilatore.error.ErrorManager;


public class ConcreteParserFactory extends ParserFactory{

	@Override
	protected InputParser factoryMethod(String in) {
		String[] input = in.split("/");
		String[] type = input[input.length-1].split("\\.");
		int index = type.length-1;
		if(type[index].equals("4l"))
			return new FourLineInputParser(in);
		if(type[index].equals("1l"))
			return new SingleLineInputParser(in);
		if(type[index].equals("txt"))
			return new LRInputParser(in);
		ErrorManager.manage(ERROR_TYPE.FILE_EXTENSION);
	return null;
	}
}
