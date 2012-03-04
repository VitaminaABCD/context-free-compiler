package com.compilatore.error;

import org.apache.log4j.Logger;


public final class ErrorManager {
	static Logger logger = Logger.getLogger(ErrorManager.class.getName());

	public static void manage(ERROR_TYPE type){
		switch(type){
		case FILE_EXTENSION:
			logger.error("ERROR: The extension of input file is not allowed\n");
			//TODO log4j
			break;
		case FILE_FORMAT:
			logger.error("ERROR: Incorrect format: \n Remember that the file must have this form:\n");
			System.out.println(
					"Example:\nS::= { S: TE | +TE; T: FT | xFT; E : eps; F: a | (E)}");
		}
	}
	
	public static void manage(ERROR_TYPE type, Exception e){
		switch(type){
			case INPUT_FILE:
				System.out.println("Problem with input file:\n" + e);
				//TODO log4j
				break;
			case INVALID_GRAMMAR:
				System.out.println("Contex-free incompleto si ricorda che il contex free è composto da 4 righe contenti rispettivamente:/n Assioma/n Non Terminali /n Non Terminali/n Produzioni nella forma A::=bBc, B::= (C)/n gli elementi della produzione non devono essere separati da uno spazio");
				//TODO log4j
				break;
			case LOOKAHEAD_GENERATION_ERROR:
				logger.error("Errore nella generazione spontanea dei simboli",e);
				break;
			case LOOKAHEAD_PROPAGATION_ERROR:
				logger.error("Errore nella propagazione dei simboli",e);
			default:
				break;
			
		}
		e.printStackTrace();
	}
	
}