package inputParser;

import error.ERROR_TYPE;
import error.ErrorManager;

/**
 * Concrete class that implement the factoryMethod of InputParserFactory.
 * @author Paolo Pino
 *
 */
public class ConcreteParserFactory extends InputParserFactory{

	@Override
	protected AbstractInputParser factoryMethod(String in) {		
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
