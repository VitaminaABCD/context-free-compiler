import com.compilatore.grammar.IGrammar;
import com.compilatore.inputParser.InputParser;
import com.compilatore.parser.LALR1;
import com.compilatore.parser.LR0;


public class Home{

	public static void main(String[] args){
		IGrammar grammar;
		
		InputParser parser = new InputParser("file.4l");
		LR0 l = new LALR1(parser.parse());
		
		l.calculateKernels();
		
	}
}
