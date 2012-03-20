package contextFree.grammar;

import java.util.List;
import java.util.Set;

/**
 * Grammar Interface.
 * @author Paolo Pino
 */
public interface IGrammar {

	/**
	 * Get non-terminal symbols list.
	 * @return a list of string with non-terminal symbol
	 */
	public List<String> getV();

	/**
	 * Set the list of non-terminal symbols.
	 * @param v the list of non-terminal
	 */
	public void setV(List<String> v);

	/**
	 * Get terminal symbols list.
	 * @return a list of string with terminal symbol
	 */
	public List<String> getT();

	/**
	* Set the list of terminal symbols.
	* @param e the list of terminal
	*/
	public void setT(List<String> e);

	/**
	 * Get production list.
	 * @return a list of production objects.
	 */
	public List<Production> getP();

	/**
	 * Set the produciton list.
	 * @param p the list of productions that must be setted.
	 */
	public void setP(List<Production> p);
	
	/**
	 * Set the axioms for the grammar.
	 * @param s the axioms.
	 */
	public void setS(String s);
	
	/**
	 * Get the axioms.
	 * @return the axioms
	 */
	public String getS();
	
	/**
	 * Get the list of first for the grammar.
	 * @return the first list.
	 */
	public Set<String>[] getFirst();

	/**
	 * Get the list of follow for the grammar.
	 * @return the follow list.
	 */	
	public Set<String>[] getFollow();
	
	/**
	 * Get the list of follow for the grammarfor a specific symbol.
	 * @return the follow list.
	 */		
	public Set<String> getFirst(String A);

	/**
	 * @return the grammar string formatted in one line.
	 */
	public String toOneLineString();
}
