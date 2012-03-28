package contextFree.grammar;

//import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An object factory that create a correct instance of grammar.
 * @author Paolo Pino
 */
public class GrammarFactory {
	public static IGrammar createContextFreeGrammar(String ass, List<Production> prod){
		return new ContextFreeGrammar(ass,prod);
	}
	
	/**
	 * Check the type of grammar (ex. Context-free) and returns the correct instance.
	 * @param Axioms
	 * @param List of Production
	 * @param List of Not Terminal Symbol
	 * @param list of Terminal Symbol
	 * @return The grammar instance if is context-free type, null otherwise.
	 * @author Paolo Pino
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
	 * Controls that make up the grammar productions that are valid.
	 * @param List of production
	 * @param list of Not-terminal symbol
	 * @param list of Terminal symbol
	 * @return GRAMMAR_TYPE.NO_CONTEXT_FREE if the grammar isn't context-free, GRAMMAR_TYPE.CONTEXT_FREE otherwise;
	 * @author Pierluigi Sottile
	 */
	private static GRAMMAR_TYPE checkType(List<Production> prod,List<String> V, List<String> T) {
		
		Iterator <Production> i = prod.iterator();
		String right=null;
		Production p= null;
		String [] righttemp;
		
			while(i.hasNext()){
				p = i.next();
				right=p.getRight();
				//controllo che left sia un non terminale se no lancio un errore
				if(!V.contains(p.getLeft())){
					return GRAMMAR_TYPE.NO_CONTEXT_FREE;
				}
				//for (int j = 0; j < right.length(); j++) {
					// controllo che sia presente eps simbolo di stringa
					// vuota che deve occupare una sola posizione...
					if (!right.equals("eps")){
						righttemp=right.split(" ");
						// prendo il carattere j-esimo della espressione
						// per assegnarlo a una singola posizioe dellaList<String>
						// prima pero controllo che esso appartenga alla grammatica
						//String carattereCorrente = Character.toString(right.charAt(j));
						for(int j=0;j<righttemp.length;j++){
								if(!V.contains(righttemp[j]) && !T.contains(righttemp[j])){
							//se non è ne un terminale ne un Non Terminale vuol dire che il carattere 
							//non appartiene alla grammatica inquanto già abbiamo controlato che non sia 
							//il carattere di stringa vuota epslon
							//return GRAMMAR_TYPE.NO_CONTEXT_FREE;
						}
						p.getRightList().add(righttemp[j]);}
					}
					else{
						//inseriamo un carattere di stringa vuota per dire che è il carattere nullo
						p.getRightList().add(" ");
						//break;
					}
				//}
			}
			
			return GRAMMAR_TYPE.CONTEXT_FREE;
	}
}
