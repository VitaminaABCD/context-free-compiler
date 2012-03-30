package contextFree.scanner;


import inputParser.AbstractInputParser;
import inputParser.InputParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


import contextFree.grammar.ContextFreeGrammar;
import contextFree.grammar.IGrammar;

/**
 * 
 * @author Paolo Pino
 *
 */
public class ScannerFactory {
	static Logger logger = Logger.getLogger(ScannerFactory.class.getName());
	
	public static IScanner createParser() throws Exception{
		System.out.println("E' necessario inserire una grammatica[indicare il file]:");
		try {
			AbstractInputParser parser = new InputParser(new BufferedReader(new InputStreamReader(System.in)).readLine());
			IGrammar grammar = (IGrammar)parser.parse();
			if(grammar instanceof ContextFreeGrammar){
				IScanner l = new LALR1(grammar);
				if(l.init()==1) {
					logger.debug("Grammar created and initialized correctly");
				}
				else {
					logger.info("\n!!!!ATTENZIONE: La grammatica non e' di tipo LALR(1)...");
				}
				return l;
			}else{			
				logger.error("No implementation for non context free grammar type.");
				return null;
			}
			
		} catch (IOException e) {
			logger.debug("Input read error");
			e.printStackTrace();
			throw new IOException("Input read error",e);
		}
	}
	
	public static IScanner createParser(AbstractInputParser parser) throws Exception{
		IScanner l;
		logger.debug("Grammar type init...");
		IGrammar grammar = (IGrammar)parser.parse();
		if(grammar instanceof ContextFreeGrammar){
			l = new LALR1(grammar);
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
			//se la grammatica e' di tipo LALR(1) ritorna l'istanza
			}else {
				logger.info("\n!!!!!!ATTENZIONE: La grammatica non e' di tipo LALR(1)...");
			}
			return l;	
		}else{			
			logger.error("No implementation for non context free grammar type.");
			return null;
		}
	}

	public static IScanner createParser(IGrammar grammar) throws Exception {
		IScanner l;
		logger.debug("Grammar type init...");
		if(grammar instanceof ContextFreeGrammar){
				l = new LALR1(grammar);
				if(l.init()==1) {
					logger.debug("Grammar created and initialized correctly");
			//se la grammatica e' di tipo LALR(1) ritorna l'istanza
				}else {
					logger.info("\n!!!!!!ATTENZIONE: La grammatica non e' di tipo LALR(1)...");
				}
				return l;	
		}else{			
			logger.error("No implementation for non context free grammar type.");
			return null;
		}
	}
}
