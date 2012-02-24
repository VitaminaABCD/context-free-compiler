package com.compilatore.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.compilatore.grammar.IGrammar;
import com.compilatore.grammar.Production;

public abstract class LR0 {

	protected IGrammar grammatica;
	
	public abstract void setGrammar(IGrammar gram);
	
	/**
	 *Passata una lista di produzione I contenente i Kernel di uno stato, restitusce la chiusura di esso 
	 * @param 	i
	 * @return 	j
	 */
	public List<IndexedProduction> chiusura (List<IndexedProduction> i){
		boolean flag =true;
		boolean uguale =true;
		IndexedProduction item ;
		IndexedProduction item1 ;
		Production corrente;
		String x;
		Object[] ob;
		String[] right;

		//Copio il kernel item I in una List<IndexedProduction> j cosï¿½ avrï¿½ sia iKernel che i relativi Item che formano la chiusura
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
				//TODO Se il puntino si trova nell'ultima posizione, ossia l'indice di posizione è maggiore o uguale 
				//della lunghezza del Rightlist
				if(item.getCurrentCharIndex() >= item.getRightList().size()){
					//esci dal while xkè ci troviamo nel caso chiusura e quindi non possiamo 
					//trovare alcuna produzione che ha nella parte sinistra l'elemento che segue il punto
					//quindi inseriamo nella tabella action la Reduce per la produzione giusta...
					break;
				}
				//prendo il simbolo che segue il puntino nella produzione A:= a.Bc
				x =(String) right[item.getCurrentCharIndex()];
				Iterator<Production> prod = grammatica.getP().iterator();
				//per ogni produzione della grammatica 
				while (prod.hasNext()){
					corrente=prod.next();
					//se il simbolo alla destra del punto e' uguale alla Parte sinistra della produzione B::= z
					if(x.equals(corrente.getLeft())){
						//se la produzione nn e' gi presente in J
						uguale =controllo(j, corrente);
						if(!uguale){
							//aggiungo la produzione mettendo il punto come primo elemnto B::=.Z
							j.add(new IndexedProduction(corrente));
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
	 * Passato uno stato e il simbolo che segue il punto, restituisce il kernel del nuovo stato con la relativa chiusura 
	 * @param i 
	 * @param X
	 * @return chiusura(j)
	 */
	public List<IndexedProduction> GoTo(List<IndexedProduction> i, String X){
		IndexedProduction item;
		Object[] ob;
		String [] right;
		List<IndexedProduction> j = new ArrayList<IndexedProduction>();
		Iterator <IndexedProduction> iter = i.iterator();
		//Per ogni produzione contenuta in I
		while (iter.hasNext()){
			item = iter.next();
			ob= item.getRightList().toArray();
			right = Arrays.copyOf(ob ,ob.length ,String[].class);
			//controlliamo se il carattere che segue il punto e' lo stesso di A::= a.Xc
			//TODO da qui si capisce quali sono gli stati di chiusura e quali no.......
			if(right[item.getCurrentCharIndex()].equals(X))
				//quindi aggiungiamo la produzione a J spostando il punto al carattere succesivo
				j.add(new IndexedProduction(item.getCurrentCharIndex()+1, item));
		}
		//ritorna il nuovo kernel con la relativa chiusura
		return chiusura(j);
	}
	
	/**
	 * data una grammatica G ci calcoliamo la grammatica aumentata associata aggiungendo la produzione S'::=.S, dove S ï¿½ l'assioma.
	 * calcoliamo la chiusura di essa i GoTo associati cosï¿½ da avera l'automa a stati finiti
	 * 
	 */
	public void Item(){
		boolean flag = true;
		State stato;
		Object[] ob;
		String[] x;
		List<IndexedProduction>chiusraX = new ArrayList<IndexedProduction>();
		List<State> automa = new ArrayList<State>();
		List<IndexedProduction> aumentata= new ArrayList<IndexedProduction>();
		//creiamo una produzione S'::= S che fa diventare la nostra grammatica aumentata
		Production p = new Production("S'", grammatica.getS());
		//dopo di che inseriamo il tutto in un List<IndexedProdaction> mettendo il punto al primo posto nella produzione S'::=.S
		aumentata.add( new IndexedProduction(0, p));
		//per poterlo inserire nell'automa al primo posto come Stat[0]
		automa.add(new State(0, chiusura(aumentata)));
		//fino a quando vengono aggiunti nuovi insiemi di item ad AUTOMA
		while(flag){
			flag=false;
			Iterator <State> iter = automa.iterator();
			//per ogni insieme di item(o stato) nell'Automa
			while(iter.hasNext()){
				stato =iter.next();
				//recupero la Lsit<indexedProduction>contenente tutte le produzioni
				List<IndexedProduction>Items = stato.getItems();
				//per ogni Terminale della grammatica
				ob= grammatica.getV().toArray();
				x = Arrays.copyOf(ob,ob.length,String[].class);
					for(int i=0;i<grammatica.getV().size();i++){
						//faccio il GoTo dello stato 
						chiusraX =GoTo(Items, x[i]);
						//TODO se si crea il nuovo stato vado a inserire lo SHIFT nella tabella degli ACTION Inquanto generato da un Terminale
						
						//se ChiusuraX non e' vuoto AND non e' contenuta nell'automa
						if(!chiusraX.isEmpty() & uguale(automa, chiusraX)==-1){
							//allora lo aggiungo
							automa.add(new State(automa.size(),chiusraX));
							flag=true;}
					}
					//ora si deve fare la stessa cosa per i non terminali
					ob = grammatica.getT().toArray();
					x = Arrays.copyOf(ob,ob.length,String[].class);
					for(int i=0;i<grammatica.getT().size();i++){
						//faccio il GoTo dello stato 
						chiusraX =GoTo(Items, x[i]);
						//TODO se si crea il nuovo stato vado a inserire lo SHIFT  o REDUCE nella tabella dei GOTo Inquanto generato da un NON Terminale
						//se ChiusuraX non e' vuoto AND non e' contenuta nell'automa
						if(!chiusraX.isEmpty() & uguale(automa, chiusraX)==-1){
							//allora lo aggiungo
							automa.add(new State(automa.size(),chiusraX));
							flag=true;}
					}
					//se ho aggiunto nuovi stati all'automa devo uscire dal while(iter.hasNext()) e ricreare l'iteratore sulla nuova struttura
					if(flag)
						break;
				}
		}
	}
	
	/**
	 * controlla se uno stato è presente in un automa restituisce -1 
	 * se non è presente se no restituisce il numero di stato uguale a quello passato
	 * @param automa
	 * @param stato
	 * @return
	 */
	public int uguale(List<State> automa, List<IndexedProduction> stato){
		State it;
		Production prod ;
		int uguale = -1;
		int proUguali = 0; 	//questa variabile ci servirà per controllare che tutte le produzioni siano uguali
	
		Iterator<State> iter = automa.iterator();
		//per ogni stato dell'automa
		while (iter.hasNext()){
			it= iter.next();
			//prendiamo la lista delle produzioni dello stato
			Iterator<IndexedProduction> stato1 = it.getItems().iterator();
			//questa variabile contera' quante produzioni sono uguali a quello dello stato che stiamo controllando 
			proUguali = 0;
			//per ogni produzione contenuta nell'iteratore
			while(stato1.hasNext()){
				prod = stato1.next();
				//se la produzione dello stato è presente nello stato che stiamo controllando incrementa il contatore 
				if (controllo(stato, prod))
					proUguali++;
			}
			if (proUguali == stato.size()){
				//se il numero di produzioni uguali è uguale alla dimensione dello stato vuol dire che 
				//sono tutti uguali e quindi 
				uguale = it.getIndex();
				break;
			}
			
		}
		return uguale;	
	}
	
	/**
	 * controlla se una produzione è già presente in una lista di produzioni ritorna vero se è presente falso se non lo è
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