package com.compilatore.inputParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.compilatore.error.ERROR_TYPE;
import com.compilatore.error.ErrorManager;
import com.compilatore.grammar.GrammarFactory;
import com.compilatore.grammar.IGrammar;
import com.compilatore.grammar.Production;

public class FourLineInputParser extends A_InputParser{
	String file;
	String A_reader, V_reader, T_reader, P_reader;
	String[] V_temp, T_temp, P_temp;
	String[] singPro;
	// List<Character> esprTemp;
	Production pro;
	int i, j;
	List<String> V=null ,T = null;
	List<Production> P=null;
	
	public FourLineInputParser(){
		file = null;
	}
	
	public FourLineInputParser(String input){
		file = input;
		V=new ArrayList<String>();
		T=new ArrayList<String>();
		P=new ArrayList<Production>();
	}
	
	public IGrammar parse(){

		
		try {
			BufferedReader f = new BufferedReader(new FileReader(file));
			// abbiamo bisognodi due variabili temporali una dove andr�
			// caricata la riga letta e un'altra di tipo String[] dove
			// saranno caricati i singoli elementi
			
				A_reader = f.readLine(); // simbolo assioma
				V_reader = f.readLine(); // simboli nn terminali
				T_reader = f.readLine(); // simboli terminali
				P_reader = f.readLine(); // insieme produzioni
				f.close();
				
			}catch(IOException e){
				ErrorManager.manage(ERROR_TYPE.INPUT_FILE,e);
				return null;
			}
		
			V_temp = V_reader.replaceAll(" ","").split(",");
			T_temp = T_reader.replaceAll(" ","").split(",");
			P_temp = P_reader.replaceAll(" ","").split(",");
			
			try{
				//inserisco innanzi tutto l'assioma tra i non Termianli
				V.add(A_reader);
				// inserisco i Non Terminali nel List<string> V
				for (i = 0; i < V_temp.length; i++) {
					// controllo che non sia uguale al  simbolo dell'assioma e lo inserisco
					if (!A_reader.equals(V_temp[i]))
						V.add(V_temp[i]);
				}	

					// inserisco i terminali nel List<string> T
					for (i = 0; i < T_temp.length; i++) {
						
						T.add(T_temp[i]);
					}
			
				// inserisco le produzioni nel List<produzione>
				for (i = 0; i < P_temp.length; i++) {
					// separo soggetto e espressione per ogni produzione
					singPro = P_temp[i].split("::=");
					P.add(new Production(singPro[0], singPro[1],true));	
				}
			} catch (Exception e) {
				ErrorManager.manage(ERROR_TYPE.INVALID_GRAMMAR, e);
			}
			
			return GrammarFactory.createGrammar(A_reader, P, V, T);
		}


}