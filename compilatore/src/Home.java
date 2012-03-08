
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
			System.out.println(l.toString());
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
		
		
		Parser parserProgram = (Parser)parser.parse();
		parserProgram.setInput("d$");                     /////////ATTENZIONE!!!!  scrivi qui la stringa di input (es. sul libro id*id+id$)..."d=$" e"d$"solleva eccezione da controllare
		System.out.println(parserProgram.parse());
		System.out.println(parserProgram.getStack().toString());
		System.out.println("\tProgetto di compilatori e interpreti");
		System.out.println("\n\tRealizzato da Paolo Pino e Pierluigi Sottile ");
		System.out.println("\nun programma che accetta in ingresso una grammatica context-free e produca in uscita\n" +
						   "le tabelle action e goto LALR(1), eventuali stati ambigui devono mostrare la situazione \n"+
						   "di ambiguita scriva i risultati in un file di testo Result.txt.");
		System.out.println("un compilatore generico che preso in ingresso il file Result.txt traduca la generica \n" +
						   "frase del linguaggio generato dalla grammatica contenuta nel file Result.txt in un albero\n" +
						   "sintattico, nel caso in cui la frase appartenga alla grammatica scrivai risultati in \n" +
						   "un file di testo");
		 do
		    {
		      //print out a menu
		      System.out.println("\n------- Menu----------");
		      System.out.println("1. Analizzare una gramamtica Contex-Free");
		      System.out.println("2. COntrollare se una frase appartiene alla grammatica e stampare l'albero sintattico");
		      System.out.println("3. EXIT");
		      System.out.println("Digitare la propria scelta:");
		      System.out.flush();
		      int scelta= System.in.read();
		    switch (scelta)
		      {
		        //the choices go here - print the details
		        case 1:
		      	  System.out.println("\nDATE: "); break;
		        case 2:
		          System.out.println("\nMONTH: " ); break;
		        case 3:
		        	System.exit(0);
		      }
		    }while(true);
	}
}