package contextFree.parser;


import inputParser.AbstractInputParser;
import inputParser.InputParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


import contextFree.grammar.IGrammar;

public class ParserFactory {
	static Logger logger = Logger.getLogger(ParserFactory.class.getName());
	
	public static IParser createParser(){
		System.out.println("E' necessario inserire una grammatica[indicare il file]:");
		try {
			AbstractInputParser parser = new InputParser(new BufferedReader(new InputStreamReader(System.in)).readLine());
			IParser l = new LALR1((IGrammar)parser.parse());
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
			}
			else {
				System.out.println("\n!!!!ATTENZIONE: La grammatica non e' di tipo LALR(1)...");
			}
			return l;

		} catch (IOException e) {
			logger.debug("Input read error");
			e.printStackTrace();
			return null;
		}catch (Exception e) {
			return null;
		}
	}
	
	public static IParser createParser(AbstractInputParser parser){
		IParser l;
		try {
			logger.debug("Grammar type init...");
			l = new LALR1((IGrammar)parser.parse());
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
			}
			else {
				System.out.println("\n!!!!ATTENZIONE: La grammatica non e' di tipo LALR(1)...");
			}
			return l;
		} catch (Exception e) {
			logger.error("Error occurred in grammar initialization");
			e.printStackTrace();
			return null;
		}
	}

	public static IParser createParser(IGrammar grammar) {
		IParser l;
		try {
			logger.debug("Grammar type init...");
			l = new LALR1(grammar);
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
		//se la grammatica e' di tipo LALR(1) ritorna l'istanza
			}else {
				System.out.println("\n!!!!!!ATTENZIONE: La grammatica non e' di tipo LALR(1)...");
			}
			return l;
		} catch (Exception e) {
			logger.error("Error occurred in grammar initialization");
			e.printStackTrace();
			return null;
		}
	}
}
