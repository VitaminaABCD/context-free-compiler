package com.compilatore.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.compilatore.grammar.IGrammar;
import com.compilatore.grammar.Production;

public  abstract class LR1 {
	
	protected IGrammar grammatica;

	public abstract void setGrammar(IGrammar gram);

	/**
	 *Passata una lista di produzione I che formano il Kernel di uno stato, restitusce la chiusura di esso 
	 * @param 	i
	 * @return 	j
	 */
	public List<IndexedProduction> chiusura (List<IndexedProduction> i){
		boolean flag =true;
		boolean uguale =true;
		IndexedProduction item ;
		int punto;
		Production corrente;
		String x;
		String la;
		Object[] ob;
		String[] right;
		Set<String> look;

		//Copio il kernel item I in una List<IndexedProduction> j cosi' avro' sia i Kernel che i relativi Item che formano la chiusura
		List<IndexedProduction> j = new ArrayList<IndexedProduction>();
		j.addAll(i);
		//fino a quando  vengono aggiunti nuovi item a J
		while (flag) {
			flag=false;
			Iterator <IndexedProduction> iter = j.iterator();
			while(iter.hasNext()){
				//per ogni Item appartenente a j
				item = iter.next();
				ob = item.getRightList().toArray();
				right = Arrays.copyOf(ob,ob.length,String[].class);
				//Se il puntino si trova nell'ultima posizione, ossia l'indice di posizione è maggiore o uguale 
				//della lunghezza del Rightlist
				punto= item.getCurrentCharIndex();
				if(punto >= item.getRightList().size()){
					//esci dal while xkè ci troviamo nel caso chiusura e quindi non possiamo 
					//trovare alcuna produzione che ha nella parte sinistra l'elemento che segue il punto
					//quindi inseriamo nella tabella action la Reduce per la produzione giusta...
					//ossi R = P::=aBc.
					//se la casella è già occupata abbiamo un conflitto e cerchiamo di risolverlo avvalendocci dei simboli di lookahead
					break;
				}
				//prendo il simbolo che segue il puntino nella produzione [A:= a.Bc, d]
				x =(String) right[punto];
				Iterator<Production> prod = grammatica.getP().iterator();
				//per ogni produzione della grammatica 
				while (prod.hasNext()){
					corrente=prod.next();
					//se il simbolo alla destra del punto e' uguale alla Parte sinistra della produzione B::= z
					if(x.equals(corrente.getLeft())){
						/////////////////////////
						//TODO a questo punto si deve applciare una specie di algoritmo simile a quello usato per calcolare il first così come è fatto epr nella teoria
						look= new HashSet<String>();
						//conrtolliamo se esiste un elemento dopo il simbolo evidenziato dal punto [A:= a.Bc, d] 
						if(!(punto+1 >= item.getRightList().size())){
							//se esiste allora aggiungiamo ai simboli di lookahead i first(cd)
							//quindi per prima cosa controlliamo se c è un terminale o un non terminale
							la =(String) right[punto];
							//se la è un Terminale aggiungiamo il simbolo alla lista dei  lookahead per la produzione che dovremo inserire
							if (grammatica.getT().contains(la))
								//aggiungiamo il simbolo di lookahead per la produzione che dobbiamo inserire nella chiusura
								look.add(la);
							//se è un NoN Terminale aggiungiamo i first legati a quel simbolo
							else{
								look.addAll(grammatica.getFirst(la));
							}
						}
						//se non esiste dobbiamo inserire i simbolo di lookahead d
						else{
							look.addAll(item.getLookahead());
						}
						///////////////////////////////////	
						//se la produzione nn e' gi presente in J
						uguale =controllo(j, corrente);
						if(!uguale){
							//aggiungo la produzione mettendo il punto come primo elemnto B::=.Z e inserendo i nuovi termini di lookahead
							j.add(new IndexedProduction(corrente,look));
							flag=true;
						}
					}
				}
				//se ho aggiunto nuove produzioni devo uscire dal while(iter.hasNext()) e ricreare l'iteratore sulla nuova Lista creata
				if(flag)
					break;
			}
		}
		//ritorna una lista chiusura con il kernel che la genera
		return j;
	}

	
	/**
	 * controlla se una produzione è già presente in una lista di produzioni ritorna vero se è presente falso se non lo è.
	 * @param j
	 * @param corrente
	 * @return
	 */
	public boolean controllo(List<IndexedProduction>j,Production corrente){
		//uso questa variabile per vedere se è già presente in j la setto di default a false, e poi la cambio se ne trova 2  uguali
		boolean uguale=false;
		Production item1;
		
		Iterator <IndexedProduction> iter1 = j.iterator();
		while(iter1.hasNext()){
			//per ogni Item appartenente a j
			item1 = iter1.next();
			//controllo sia se la parte destra che la parte sinistra sono già presenti
			if (item1.getRight().equals(corrente.getRight()) & item1.getLeft().equals(corrente.getLeft()))
				{
				//setto la variabile booleana a TRUE per segnalare che sono uguali
				uguale =true;
				//esco dal ciclo xke' gia' ho trovato la corrispondenza
				break;
				
				}
			}
		return uguale;
	}
}

