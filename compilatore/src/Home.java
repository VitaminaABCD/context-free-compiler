import inputParser.AbstractInputParser;
import inputParser.InputParser;
import inputParser.LRInputParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thoughtworks.xstream.XStream;

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
		PropertyConfigurator.configure("log4j.config");
		if(args.length==0){
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
				      	  	parser = new InputParser(leggi.readLine());    //TODO: da rimuovere
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
		}else{
			logger.debug("Start Application");
			int i=0;
	        String arg;
	        boolean analizeFlag= false;
	        boolean inFlag=false;
	        boolean stFlag=false;

	        String input = "";
			while (i < args.length && args[i].startsWith("-")) {
				arg = args[i++];
	            if (arg.equals("-in")) {
	                if (i < args.length)
	                    input = args[i++];
	                inFlag = true;
	            }else if (arg.equals("--analize")) {
	            	analizeFlag=true;
	            }else if (arg.equals("--st")) {
	            	stFlag=true;
	            }
			}
			if(inFlag){
			    AbstractInputParser parser = null;
				if(stFlag){
		      	  	parser = new InputParser(input);   
		      	  	analizer(parser);
				}
				if(analizeFlag){
		        	System.out.println("\nAnalisi sintattica " ); 
		  			parser = new LRInputParser("Result.txt");
		  			astMethod(parser);
				}
				if(!stFlag&&!analizeFlag){
					System.out.println("no option specified...program terminated.");
				}
			}
		}
	}
	

	private static void astMethod(AbstractInputParser parser) throws Exception {
		ParserProgram parserProgram = (ParserProgram)parser.parse();
		boolean flag=true;
		while(flag){
			System.out.println("\nDigitare la stringa in input: "); 
			BufferedReader leggi = new BufferedReader(new InputStreamReader(System.in));
			parserProgram.setInput(leggi.readLine());                     /////////ATTENZIONE!!!!  scrivi qui la stringa di input (es. sul libro id*id+id$)
			switch(parserProgram.parse()){
				case ACCEPT:
					flag=false;
					logger.info("ACCEPT");	
					System.out.println(parserProgram.getStack().toString());
					System.out.println("\nCRONOLOGIA:\n");
					for(HistoryElement e : parserProgram.getHistory()) System.out.println(e.toString()+"\n");
					St st = new St(parserProgram.getHistory());
					st.initFromHistory();	
					writeToXml(st.getRoot());
					break;
				case ERROR:
					flag=false;
					logger.info("ERROR");
					break;
				case INVALID_IN:
					System.out.println("Uno o pi� caratteri tra quelli inseriti non sono ammessi dalla grammatica corrente");
					flag=true;
					break;
			}
		}
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
	/**
	 * Store an ST into xml file named "ST.xml"
	 * @param root the root of AST
	 * @throws FileNotFoundException
	 * @author Paolo Pino
	 */
	private static void writeToXml(DefaultMutableTreeNode root) throws FileNotFoundException { 
		XStream xstream = new XStream();
		String xml = xstream.toXML(root);
		PrintStream output = new PrintStream(new FileOutputStream("ST.xml"));
		String [] temp = xml.split("\\n");
		for(String o : temp)
			output.println(o);
		output.close();
	}
}