
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.Parse.Parser;
import com.compilatore.parser.IParsing;
import com.compilatore.parser.ParsingFactory;
import com.compilatore.inputParser.GrammarParser;
import com.compilatore.inputParser.InputParser;
import com.compilatore.inputParser.LRInputParser;


public class Home{

	static Logger logger = Logger.getLogger(Home.class.getName());
	
	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("log4j.config");
		
		logger.debug("Start Application");
		InputParser parser = new GrammarParser("esempioLibro.4l");
		
		ParsingFactory p = new ParsingFactory();
//		IParsing l = p.createParsing();
		IParsing l = p.createParsing(parser);
		
		if(l!=null){
			PrintStream output = new PrintStream(new FileOutputStream("Result.txt"));
			String table = l.toString();
			String [] temp = table.split("\\n");
			for(String o : temp)
				output.println(o);
			output.println("\nGrammatica:");
			output.println(l.getGrammar().toOneLineString());
			output.close();
		}
		
		parser = new LRInputParser("Result.txt");
		parser.parse();
		
		Parser parserProgram = new Parser();
		
	}
}