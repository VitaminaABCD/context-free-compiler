package inputParser;


public abstract class AbstractInputParser {

	/**
	 * Execute the parse operation on the object.
	 * @return the parsing result
	 * @throws Exception
	 * @author Paolo Pino
	 */
	public abstract Object parse() throws Exception;
}
