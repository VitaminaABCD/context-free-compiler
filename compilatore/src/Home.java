import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.compilatore.grammar.IGrammar;
import com.compilatore.inputParser.InputParser;
import com.compilatore.parser.IParsing;
import com.compilatore.parser.LALR1;
import com.compilatore.parser.LR0;
import com.compilatore.parser.ParsingFactory;
import com.compilatore.parser.State;


public class Home{

	static Logger logger = Logger.getLogger(Home.class.getName());
	
	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("log4j.config");
		
		logger.debug("Start Application");
		InputParser parser = new InputParser("file.4l");
		
		ParsingFactory p = new ParsingFactory();
//		IParsing l = p.createParsing();
		IParsing l = p.createParsing(parser);
		
		if(l!=null)
			System.out.println("\n"+l.toString());
		
	}
}
