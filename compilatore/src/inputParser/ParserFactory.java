package inputParser;

public abstract class ParserFactory{

//	public void parse(String in){
//		A_InputParser parser = factoryMethod(in);
//	}

	protected abstract InputParser factoryMethod(String in);
}
