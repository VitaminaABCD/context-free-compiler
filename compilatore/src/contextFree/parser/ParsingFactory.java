package contextFree.parser;


import inputParser.GrammarParser;
import inputParser.InputParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


import contextFree.grammar.IGrammar;

public class ParsingFactory {
	static Logger logger = Logger.getLogger(ParsingFactory.class.getName());
	
	public IParsing createParsing(){
		System.out.println("E' necessario inserire una grammatica[indicare il file]:");
		try {
			InputParser parser = new GrammarParser(new BufferedReader(new InputStreamReader(System.in)).readLine());
			IParsing l = new LALR1((IGrammar)parser.parse());
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
			}
			else {
				System.out.println("\n!!!!ATTENZIONE: La grammatica non � di tipo LALR(1)...");
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
	
	public IParsing createParsing(InputParser parser){
		IParsing l;
		try {
			logger.debug("Grammar type init...");
			l = new LALR1((IGrammar)parser.parse());
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
			}
			else {
				System.out.println("\n!!!!ATTENZIONE: La grammatica non � di tipo LALR(1)...");
			}
			return l;
		} catch (Exception e) {
			logger.error("Error occurred in grammar initialization");
			e.printStackTrace();
			return null;
		}
	}

	public IParsing createParsing(IGrammar grammar) {
		IParsing l;
		try {
			logger.debug("Grammar type init...");
			l = new LALR1(grammar);
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
		//se la grammatica � di tipo LALR(1) ritorna l'istanza
			}else {
				System.out.println("\n!!!!!!ATTENZIONE: La grammatica non � di tipo LALR(1)...");
			}
			return l;
		} catch (Exception e) {
			logger.error("Error occurred in grammar initialization");
			e.printStackTrace();
			return null;
		}
	}
}
