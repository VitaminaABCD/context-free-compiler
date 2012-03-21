package inputParser;

/**
 * Abstract InputParserFactory
 * @author Paolo Pino
 *
 */
public abstract class InputParserFactory{
	
	/**
	 * Return different object instance based on the extension.
	 * @param the path of file to parse.
	 * @return FourLineInputParser(input) instance if the file extension is ".4l",\n SingleLineInputParser(input) instance if the file extension is ".1l",\n LRInputParser(in) instance if the file extension is ".txt", null otherwise.
	 * @author Paolo Pino
	 */
	protected abstract AbstractInputParser factoryMethod(String in);
}
