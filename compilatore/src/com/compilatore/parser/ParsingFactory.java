package com.compilatore.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.compilatore.grammar.IGrammar;
import com.compilatore.inputParser.InputParser;

public class ParsingFactory {
	static Logger logger = Logger.getLogger(ParsingFactory.class.getName());
	
	public IParsing createParsing(){
		System.out.println("E' necessario inserire una grammatica[indicare il file]:");
		try {
			InputParser parser = new InputParser(new BufferedReader(new InputStreamReader(System.in)).readLine());
			IParsing l = new LALR1(parser.parse());
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
				return l;		//se la grammatica è di tipo LALR(1) ritorna l'istanza
			}
			else {
				System.out.println("ATTENZIONE: La grammatica non è di tipo LALR(1).\n Il programma cerrà terminato...");
				return null;
			}

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
			l = new LALR1(parser.parse());
			if(l.init()==1) {
				logger.debug("Grammar created and initialized correctly");
				return l;		//se la grammatica è di tipo LALR(1) ritorna l'istanza
			}
			else {
				System.out.println("ATTENZIONE: La grammatica non è di tipo LALR(1).\n Il programma verrà terminato...");
				return null;
			}
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
				return l;		//se la grammatica è di tipo LALR(1) ritorna l'istanza
			}
			else {
				System.out.println("ATTENZIONE: La grammatica non è di tipo LALR(1).\n Il programma Verrà terminato...");
				return null;
			}
		} catch (Exception e) {
			logger.error("Error occurred in grammar initialization");
			e.printStackTrace();
			return null;
		}
	}
}
