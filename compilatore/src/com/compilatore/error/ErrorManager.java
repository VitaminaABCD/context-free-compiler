package com.compilatore.error;


public final class ErrorManager {


	public static void manage(ERROR_TYPE type){
		switch(type){
		case FILE_EXTENSION:
			System.out.println("ERROR: The extension of input file is not allowed\n");
			//TODO log4j
			break;
		case FILE_FORMAT:
			System.out.println("ERROR: Incorrect format: \n Remember that the file must have this form:\n");
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
			default:
				break;
			
		}
		e.printStackTrace();
	}
	
}