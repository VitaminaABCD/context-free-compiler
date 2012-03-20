package inputParser;

public abstract class InputParserFactory{

//	public void parse(String in){
//		A_InputParser parser = factoryMethod(in);
//	}

	protected abstract AbstractInputParser factoryMethod(String in);
}
