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
	 * @author Pierluigi Sottile, Paolo Pino
	 */
	private static GRAMMAR_TYPE checkType(List<Production> prod,List<String> V, List<String> T) {
		for(Production currentProduction : prod){
			//controllo che left sia un non terminale se no la grammatica non è contextFree
			if(!V.contains(currentProduction.getLeft())){
				return GRAMMAR_TYPE.NO_CONTEXT_FREE;
			}
			
			for(String symbol : currentProduction.getRightSimbols()){		//controllo che ogni simbolo appartenga all'insieme (T)U(V)
				if(symbol.equals(" ")) break;   					//se il symbolo rappresenta una stringa vuota si passa alla prossima produzione
				if(!V.contains(symbol) && !T.contains(symbol)){
					return GRAMMAR_TYPE.NO_CONTEXT_FREE;
				}
			}
		}
		return GRAMMAR_TYPE.CONTEXT_FREE;						//se le condizioni sono verificate è contextFree
	}
}
