package inputParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import contextFree.grammar.GrammarFactory;
import contextFree.grammar.IGrammar;
import contextFree.grammar.Production;


import error.ERROR_TYPE;
import error.ErrorManager;

/**
 * Parse a four line grammar format.
 * ex (file.4l): 
 * 		E
 * 		E, T, P
 *		a, +, x, (, ), $
 * 		E::=E+T, E::=T, T::=TxP, T::=P, P::=a, P::=(E)
 * @author Pierluigi Sottile
 */
public class FourLineInputParser extends AbstractInputParser{
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
		V=new LinkedList<String>();
		T=new ArrayList<String>();
		P=new ArrayList<Production>();
	}
	
	/**
	 * Read the file .4l and creates the object grammar.
	 * @return the grammar
	 * @author Pierluigi Sottile
	 */
	@Override
	public IGrammar parse() throws Exception{
		try {
			BufferedReader f = new BufferedReader(new FileReader(file));
			// abbiamo bisogno di due variabili temporali una dove andra'
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
					//e li inserisco in un oggetto di tipo Prodaction
					P.add(new Production(singPro[0], singPro[1],V,T));	
				}
			} catch (Exception e) {
				ErrorManager.manage(ERROR_TYPE.INVALID_GRAMMAR, e);
			}
			

			
			return GrammarFactory.createGrammar(A_reader, P, V, T);
		} 	    
}