package contextFree.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Define a context-free grammar type
 * @author Paolo Pino
 *
 */
public class ContextFreeGrammar implements IGrammar {

	private List<String> V; // insieme di simboli nn terminali
	private List<String> T; // insieme di simboli terminali
	private List<Production> P; // Insieme di produzioni
	private String S; // Assioma
	private boolean[] nullo; // Insieme dei null
	private Set<String>[] first; // Insieme dei first
	private Set<String>[] follow; // Insieme dei follow

	public ContextFreeGrammar() {
		this.V = new ArrayList<String>();
		this.T = new ArrayList<String>();
		this.P = new ArrayList<Production>();
		this.S = "";
	}

	public ContextFreeGrammar(String ass, List<Production> prod) {
		this.V = new ArrayList<String>();
		this.T = new ArrayList<String>();
		this.P = prod;
		this.S = ass;
	}

	public ContextFreeGrammar(String ass, List<Production> prod,
			List<String> V, List<String> T) {
		this.V = V;
		this.T = T;
		this.P = prod;
		this.S = ass;
		nullo();
		first();
		follow();
	}

	@Override
	public List<String> getV() {
		return V;
	}

	@Override
	public void setV(List<String> v) {
		V = v;
	}

	@Override
	public List<String> getT() {
		return T;
	}

	@Override
	public void setT(List<String> e) {
		T = e;
	}

	@Override
	public List<Production> getP() {
		return P;
	}

	@Override
	public void setP(List<Production> p) {
		P = p;
	}

	@Override
	public String getS() {
		return S;
	}

	@Override
	public void setS(String s) {
		S = s;
	}

	public Set<String>[] getFirst() {
		first();
		return first;
	}
	
	
	/**
	 * I spent a character returns the first list associated to it
	 * @param 	Simbol not-Terminal A
	 * @return  First(A)
	 */
	public Set<String> getFirst(String A){
		return first[V.indexOf(A)];
	}
	
	/**
	 * I spent a character returns the Follow list associated to it
	 * @param 	Simbol not-terminal A
	 * @return  Follow(A)
	 */
	public Set<String>[] getFollow() {
		follow();
		return follow;
	}

	/**
	 * population structure Bolean [] null defined in class grammar,
	 * it has the same size of V. one element is said to null if every component 
	 * of the expression of the production will be 'null.
	 * @author Pierluigi Sottile
	 */
	public void nullo() {

		Production prod;
		String simbolo; // singolo elemento dell'espressione
		boolean flag = true; // serve per controllare se sono stati fatti
								// cambiamenti
		boolean tuttiNull = true; // controlla se tutti gli elementi di una
									// espressione sono nullable
		nullo = new boolean[V.size()];
		//fino a quando sono state fatte modifiche alla struttura
		while (flag) {
			flag = false;
			Iterator<Production> posiz = P.iterator();
			// itero per ogni produzioni per vedere se e' nulla
			while (posiz.hasNext()) {
				prod = (Production) posiz.next();
				// per ogni produzione isolo la parte dell'associazione dalla
				// parte dell'espressione
				String ass = prod.getLeft();
				List<String> espr = prod.getRightList();
				tuttiNull = true;
				Iterator<String> termine = espr.iterator();
				// Controlliamo se tutti i termini dell'espressione sono nulli
				while (termine.hasNext()) {
					simbolo = termine.next();
					// se anche un solo simbolo dell'espressione e' un elemento
					// terminale tutta l'espressione nn potra' essere nulla
					// quindi analizzo il prossimo non terminale della tabella
					// nullo
					if (T.contains(simbolo)) {
						tuttiNull = false;
						break;
					}
					// Se il simbolo ï¿½ un non terminale devo vedere se e' nullo
					else if (V.contains(simbolo)) {
						if (!nullo[V.indexOf(simbolo)]) {
							tuttiNull = false;
							break;
						}
					}
				}
				// se tutti i termini dell'espressione sono nulli e il nostro
				// Non Terminale e' impostato a false allora
				// resettiamo a true nella tabella nullable e segnaliamo tramite
				// il flag che abbiamo fatto un cambiamento e rieseguiamo tutto
				// da capo
				if (tuttiNull && !nullo[V.indexOf(ass)]) {
					nullo[V.indexOf(ass)] = true;
					flag = true;
				}
			}
		}
	}

	/**
	 * Populate structure in Set<String>[]first for each non-terminal V, 
	 * using a structure of type Set to avoid duplication. 
	 * @author Pierluigi Sottile
	 */
	 @SuppressWarnings("unchecked")
	public void first() {
		boolean flag = true; // serve per controllare se sono stati fatti
							// cambiamenti, inquanto essi influenzerebbero gli altri
							// first
		Production prod;
		String ass;
		String carattere;
		int dimensione;
		first = new Set[V.size()];
		for (int i = 0; i < first.length; i++) {
			first[i] = new HashSet<String>();
		}
		while (flag) {
			flag = false;
			Iterator<Production> posiz = P.iterator();
			// itero per ogni produzione
			while (posiz.hasNext()) {
				prod = (Production) posiz.next();
				ass = prod.getLeft();
				List<String> espr = prod.getRightList();
				// Dimensione iniziale del first, ci permette di capire se
				// abbiamo aggiunto caratteri
				dimensione = first[V.indexOf(ass)].size();
				// First dell'espressione
				Set<String> firstEspr = new HashSet<String>();
				Iterator<String> termine = espr.iterator();
				// itero i first per ogni termine dell'espressione
				while (termine.hasNext()) {
					carattere = termine.next();
					// Se il simbolo e' un terminale,lo aggiungo a firstEspr
					// a questo punto esco dal ciclo inquanto gli altri termini non
					// ci interessano
					if (T.contains(carattere)) {
						firstEspr.add(carattere);
						break;
					}
					// Se il simbolo e' un nt...
					else if (V.contains(carattere)) {
						// ..allora tutti i suoi first sono inclusi nel
						// firstEspr
						firstEspr.addAll(first[V.indexOf(carattere)]);
						// Se il non terminale non ï¿½ nullable ho finito di
						// calcolare firstEspr ed esco dal ciclo
						if (!nullo[V.indexOf(carattere)]) {
							break;
						}
					}
				}

				// inserisco i First(Espr) nei First(ass), naturalmente essendo
				// un Set verranno aggiunti solo i simboli che non sono gia'
				// presenti
				first[V.indexOf(ass)].addAll(firstEspr);
				// se il numero di elementi in first(ass) e' cambiata devo
				// rieseguire tutto perchè potrebbe influire su tutti gli altri
				if (first[V.indexOf(ass)].size() != dimensione) {
					flag = true;
				}
			}
		}
	}

	/**
	 * Population structure in Set <String> [] first for each non-terminal V, 
	 * using a structure of type Set to avoid duplication.
	 * @author Pierluigi Sottile
	 */
	 @SuppressWarnings("unchecked")
	public void follow() {

		boolean flag = true;
		Production prod;
		String ass;
		String termine;
		String simbolo;
		int dimensione;
		boolean tuttinull;
		follow = new Set[V.size()];

		for (int i = 0; i < first.length; i++) {
			follow[i] = new HashSet<String>();
		}
		// Il follow dell'assioma contiene $ per definizione quindi lo possiamo
		// inserire
		follow[0].add("$");
		while (flag) {
			flag = false;
			Iterator<Production> posizione = P.iterator();
			// itero per ogni produzione
			while (posizione.hasNext()) {
				prod = (Production) posizione.next();
				ass = prod.getLeft();
				LinkedList<String> espr = new LinkedList<String>(
						prod.getRightList());
				// Ripeto il ciclo per ogni elemento dell'espressione, per
				// facilitare le operazioni su quest'ultimo uso una linked list
				while (!espr.isEmpty()) {
					termine = espr.removeFirst();
					if (V.contains(termine)) {
						// se e' un NON Terminale allora calcolo i follow per
						// esso
						Set<String> followTermine = new HashSet<String>();
						tuttinull = true;
						Iterator<String> elemen = espr.iterator();
						// a questo punto valuto i follow di tutti gli elementi
						// che lo seguono
						while (elemen.hasNext()) {
							simbolo = elemen.next();
							// Se Simbolo e' un terminale lo aggiungo ai
							// follow(termine) e setto la variabile tuttinull a
							// folse
							if (T.contains(simbolo)) {
								followTermine.add(simbolo);
								tuttinull = false;
								break;
							}
							// Se il Simbolo e' un nt, aggiungo i first(simbolo)
							// ai follow(termine), controllo se Simbolo ï¿½ nullo
							// e in caso positivo esco perchï¿½ ho finito
							else if (V.contains(simbolo)) {
								followTermine.addAll(first[V.indexOf(simbolo)]);
								if (!nullo[V.indexOf(simbolo)]) {
									tuttinull = false;
									break;
								}
							}
						}
						// se tuttinull ï¿½ vero vuol dire che il tutti gli
						// elementi di follow(Ass) sono contenuti nel
						// follow(termine)
						if (tuttinull) {
							followTermine.addAll(follow[V.indexOf(ass)]);
						}

						dimensione = follow[V.indexOf(termine)].size();
						// inserisco il follow(termine) nella Set<string>[]
						// follow senza inserire ripetizioni
						follow[V.indexOf(termine)].addAll(followTermine);
						// Se il follow ï¿½ cambiato, devo ricalcolare tutti i
						// follow tutti i follow
						if (follow[V.indexOf(termine)].size() != dimensione) {
							flag = true;
						}
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "ASSIOMA: \t" + S + "\nTERMINALI: \t" + T + "\nNON TERMINALI: \t" + V
				+ "\nPRODUZIONI: \t" + P;
	}

	@Override
	public String toOneLineString() {
		String result=S.toString()+"::= {";
		for(String v  : V){
			result+=v+": ";			
			for(Production p : P){
				if(p.getLeft().equals(v)) result+=p.getRight()+" | ";
			}
			result=result.substring(0, result.length()-3);
			result+="; ";
		}
		
		return result=result.substring(0,result.length()-2)+"}";
	}
}
