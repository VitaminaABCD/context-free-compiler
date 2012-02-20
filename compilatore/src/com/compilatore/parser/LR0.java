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
		IndexedProduction item ;
		Production corrente;
		String x;
		Object[] ob;
		String[] right;

		//Copio il kernel item I in una List<IndexedProduction> j cos� avr� sia iKernel che i relativi Item che formano la chiusura
		List<IndexedProduction> j = new ArrayList<IndexedProduction>();
		j=i;
		//fino a quando  vengono aggiunti nuovi item a J
		while (flag) {
			flag=false;
			Iterator <IndexedProduction> iter = j.iterator();
			while(iter.hasNext()){
				//per ogni Item appartenente a j
				item = iter.next();   //TODO al secondo ciclo lancia eccezione (???)
				ob = item.getRightList().toArray();
				right = Arrays.copyOf(ob,ob.length,String[].class);
				//prendo il simbolo che segue il puntino nella produzione A:= a.Bc
				x =(String) right[item.getCurrentCharIndex()];
				Iterator<Production> prod = grammatica.getP().iterator();
				//per ogni produzione della grammatica 
				while (prod.hasNext()){
					corrente=prod.next();
					//se il simbolo alla destra del punto � uguale alla Parte sinistra della produzione B::= z
					if(x.equals(corrente.getLeft())){
						//se la produzione nn � gi� presente in J
						if(!j.contains(corrente)){
							//aggiungo la produzione mettendo il punto come primo elemnto B::=.Z
							j.add(new IndexedProduction(corrente));
							flag=true;
						}
					}
				}
			}
		}
		//ritorna una lista chiusura
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
		String [] right;
		List<IndexedProduction> j = new ArrayList<IndexedProduction>(null);
		Iterator <IndexedProduction> iter = i.iterator();
		//Per ogni produzione contenuta in I
		while (iter.hasNext()){
			item = iter.next();
			right = (String[]) item.getRightList().toArray();
			//controlliamo se il carattere che segue il punto � lo stesso di A::= a.Xc
			//TODO da qui si capisce quali sono gli stati di chiusura e quali no.......
			if(right[item.getCurrentCharIndex()]== X)
				//quindi aggiungiamo la produzione a J spostando il punto al carattere succesivo
				j.add(new IndexedProduction(item.getCurrentCharIndex()+1, item));
		}
		//ritorna il nuovo kernel con la relativa chiusura
		return chiusura(j);
	}
	
	/**
	 * data una grammatica G ci calcoliamo la grammatica aumentata associata aggiungendo la produzione S'::=.S, dove S � l'assioma.
	 * calcoliamo la chiusura di essa i GoTo associati cos� da avera l'automa a stati finiti
	 * 
	 */
	public void Item(){
		boolean flag = true;
		State stato;
		String[] x;
		List<IndexedProduction>chiusraX = new ArrayList<IndexedProduction>();
		List<State> automa = new ArrayList<State>();
		List<IndexedProduction> aumentata= new ArrayList<IndexedProduction>();
		Production p = new Production();
		//creiamo una produzione S'::= S che fa diventare la nostra grammatica aumentata
		p.setLeft("S'");
		p.setRight(grammatica.getS());
		//TODO Dovrebbe funzionare.... 
		p.getRightList().add(grammatica.getS()); 
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
					x = (String[]) grammatica.getV().toArray();
					for(int i=0;i<grammatica.getV().size();i++){
						//faccio il GoTo dello stato 
						chiusraX =GoTo(Items, x[i]);
						//TODO se si crea il nuovo stato vado a inserire lo SHIFT o REDUCE nella tabella degli ACTION Inquanto generato da un Terminale
						//se ChiusuraX non � vuoto AND non � contenuta nell'automa
						if(!chiusraX.isEmpty() & !automa.contains(chiusraX))
							//allora lo aggiungo
							automa.add(new State(automa.size(),chiusraX));
						flag=true;
					}
					//ora si deve fare la stessa cosa per i non terminali
					x = (String[]) grammatica.getT().toArray();
					for(int i=0;i<grammatica.getT().size();i++){
						//faccio il GoTo dello stato 
						chiusraX =GoTo(Items, x[i]);
						//TODO se si crea il nuovo stato vado a inserire lo SHIFT nella tabella degli GOT Inquanto generato da un NON Terminale
						//se ChiusuraX non � vuoto AND non � contenuta nell'automa
						if(!chiusraX.isEmpty() & !automa.contains(chiusraX))
							//allora lo aggiungo
							automa.add(new State(automa.size(),chiusraX));
						flag=true;
					}
				}
		}
		
		int y= 0;
		y++;
	}
}