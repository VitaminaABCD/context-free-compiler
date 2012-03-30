import inputParser.AbstractInputParser;
import inputParser.InputParser;
import inputParser.LRInputParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import parserProgram.St;
import parserProgram.HistoryElement;
import parserProgram.ParserProgram;


import contextFree.scanner.IScanner;
import contextFree.scanner.ScannerFactory;

/**
 * 
 * @author Paolo Pino
 *
 */
public class Home{


	static Logger logger = Logger.getLogger(Home.class.getName());
	
	public static void main(String[] args) throws Exception{
		if(args.length==0){
			PropertyConfigurator.configure("log4j.config");
			
			logger.debug("Start Application");
		   	
			System.out.println("\tProgetto di compilatori e interpreti");
			System.out.println("\n\tRealizzato da Paolo Pino e Pierluigi Sottile ");
			System.out.println("\nE' stato realizzato un programma che accetta in ingresso una grammatica context-free e produca in uscita\n" +
							   "le tabelle action e goto LALR(1), eventuali stati ambigui devono mostrare la situazione \n"+
							   "di ambiguita scriva i risultati in un file di testo Result.txt.");
			System.out.println("un compilatore generico che preso in ingresso il file Result.txt traduca la generica \n" +
							   "frase del linguaggio generato dalla grammatica contenuta nel file Result.txt in un albero\n" +
							   "sintattico, nel caso in cui la frase appartenga alla grammatica scrivai risultati in \n" +
							   "un file di testo");
			try{ 
				do
				    {
				      //print out a menu
				      System.out.println("\n------- Menu----------");
				      System.out.println("1. Analizzare una gramamtica Contex-Free");
				      System.out.println("2. COntrollare se una frase appartiene alla grammatica e stampare l'albero sintattico");
				      System.out.println("3. EXIT");
				      System.out.println("Digitare la propria scelta:");
					BufferedReader leggi = new BufferedReader(new InputStreamReader(System.in));
//				      for(int i=0; i<4;i++){
//							System.out.println("Inserire un numero");
//							leggi=new BufferedReader(new InputStreamReader(System.in));
							//TODO:
//							if(leggi.readLine().compareTo("9")>0 && leggi.readLine().compareTo("0")<0) 
//								break;
//				      }
				    int scelta= Integer.parseInt(leggi.readLine());
				    AbstractInputParser parser = null;
				    switch (scelta)
				      {
				        //the choices go here - print the details
				        case 1:
				      	  	System.out.println("\nDigitare il nome del file contenente il Contex-Free: "); 
				      	  	leggi = new BufferedReader(new InputStreamReader(System.in));
				      	  	parser = new InputParser("file.4l");    //TODO: da rimuovere
//				      	  	parser = new GrammarParser(leggi.readLine());
				      	  	analizer(parser);
				      	  	break;
				        case 2:
				        	System.out.println("\nAnalisi sintattica " ); 
				  			parser = new LRInputParser("Result.txt");
				  			astMethod(parser);
				          break;
				        case 3:
				        	System.exit(0);
				      }
				    }while(true);
				}catch (NumberFormatException e) {
					logger.error("You have inserted a number",e);
				}catch (Exception e) {
					logger.error("Critical error...the programm whil termanate",e);
					System.exit(0);
				}
		}		
	}
	

	private static void astMethod(AbstractInputParser parser) throws Exception {
		ParserProgram parserProgram = (ParserProgram)parser.parse();
		System.out.println("\nDigitare la stringa in input: "); 
//		BufferedReader leggi = new BufferedReader(new InputStreamReader(System.in));
		parserProgram.setInput("d=d$");                     //TODO: da rimuovere	
//		parserProgram.setInput(leggi.readLine());                     /////////ATTENZIONE!!!!  scrivi qui la stringa di input (es. sul libro id*id+id$)
		System.out.println(parserProgram.parse());
		System.out.println(parserProgram.getStack().toString());
		System.out.println("\nCRONOLOGIA:\n");
		for(HistoryElement e : parserProgram.getHistory()) System.out.println(e.toString()+"\n");
		
		St st = new St(parserProgram.getHistory());
		st.initFromHistory();
		System.out.println(st.toString());
	}


	private static void analizer(AbstractInputParser parser)
			throws FileNotFoundException, Exception {
		IScanner lalr1 = ScannerFactory.createParser(parser);
		
		if(lalr1!=null){
			System.out.println(lalr1.toString());
			if(!lalr1.isAmbiguos()){
				PrintStream output = new PrintStream(new FileOutputStream("Result.txt"));
				String table = lalr1.toString();
				String [] temp = table.split("\\n");
				for(String o : temp)
					output.println(o);
				output.println("\nGrammatica:");
				output.println(lalr1.getGrammar().toOneLineString());
				output.close();
			}
		}		
	}
}