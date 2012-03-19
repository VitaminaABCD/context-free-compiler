package contextFree.grammar;

//import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GrammarFactory {
	public static IGrammar createContextFreeGrammar(String ass, List<Production> prod){
		return new ContextFreeGrammar(ass,prod);
	}
	
	/**
	 * Check the type of grammar (eg Contex-free) and returns the correct instance
	 * @param Assioma
	 * @param List of Prodaction
	 * @param List of Not Termianl Simbol
	 * @param list of Termianl Simbol
	 * @return 
	 * @author Pierluigi Sottile
	 */
	public static IGrammar createGrammar(String ass, List<Production> prod,List<String> V, List<String> T){
		
		switch(checkType(prod,V,T)){
		case CONTEXT_FREE:
			return new ContextFreeGrammar(ass, prod, V, T);
			default:
				return null;
		}
	}
	
	
	/**
	 * controls that make up the grammar productions that are valid
	 * @param List of prodaction
	 * @param list of Not-terminal simbol
	 * @param list of Terminal Simbol
	 * @return
	 * @author Pierluigi Sottile
	 */
	private static GRAMMAR_TYPE checkType(List<Production> prod,List<String> V, List<String> T) {
		
		Iterator <Production> i = prod.iterator();
		String right=null;
		Production p= null;
		
			while(i.hasNext()){
				p = i.next();
				right=p.getRight();
				//controllo che left sia un non terminale se no lancio un errore
				if(!V.contains(p.getLeft())){
					return GRAMMAR_TYPE.NO_CONTEXT_FREE;
				}
				for (int j = 0; j < right.length(); j++) {
					// controllo che sia presente eps simbolo di stringa
					// vuota che deve occupare una sola posizione...
					if (!right.equals("eps")){
						// prendo il carattere j-esimo della espressione
						// per assegnarlo a una singola posizioe dellaList<String>
						// prima pero controllo che esso appartenga alla grammatica
						String carattereCorrente = Character.toString(right.charAt(j));
						if(!V.contains(carattereCorrente) && !T.contains(carattereCorrente)){
							//se non è ne un terminale ne un Non Terminale vuol dire che il carattere 
							//non appartiene alla grammatica inquanto già abbiamo controlato che non sia 
							//il carattere di stringa vuota epslon
							return GRAMMAR_TYPE.NO_CONTEXT_FREE;
						}
						p.getRightList().add(carattereCorrente);
					}
					else{
						//inseriamo un carattere di stringa vuota per dire che è il carattere nullo
						p.getRightList().add(" ");
						break;
					}
				}
			}
			
			return GRAMMAR_TYPE.CONTEXT_FREE;
	}
}
