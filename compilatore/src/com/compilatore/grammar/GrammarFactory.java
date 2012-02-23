package com.compilatore.grammar;

//import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GrammarFactory {
	public static IGrammar createGrammar(String ass, List<Production> prod){
		
		return new ContextFreeGrammar(ass,prod);
	}
	
	/**
	 * Verifica il tipo di grammatica (es contex-free) e ritorna l'istanza corretta
	 * @param ass
	 * @param prod
	 * @param V
	 * @param T
	 * @return 
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
	 * 
	 * @param prod
	 * @param V
	 * @param T
	 * @return
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
							//se non � ne un terminale ne un Non Terminale vuol dire che il carattere 
							//non appartiene alla grammatica inquanto gi�� abbiamo controlato che non sia 
							//il carattere di stringa vuota epslon
							return GRAMMAR_TYPE.NO_CONTEXT_FREE;
						}
						p.getRightList().add(carattereCorrente);
					}
					else{
						//inseriamo un carattere di stringa vuota per dire che � il carattere nullo
						p.getRightList().add(" ");
						break;
					}
				}
			}
			
			return GRAMMAR_TYPE.CONTEXT_FREE;
	}
}
