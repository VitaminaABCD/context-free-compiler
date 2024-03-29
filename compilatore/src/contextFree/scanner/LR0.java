package contextFree.scanner;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import contextFree.grammar.IGrammar;
import contextFree.grammar.Production;


public abstract class LR0 implements IScanner{

	protected IGrammar grammatica;
	
	public abstract void setGrammar(IGrammar gram);
	public abstract int init() throws Exception;	

	/**
	 *I passed a list of production that form the kernel of a state, return closing it 
	 * @param 	the kernels of the state
	 * @return 	list of products forming the state
	 * @author Pierluigi Sottile
	 * 
	 */
	public List<IndexedProduction> chiusura (List<IndexedProduction> i){
		boolean flag =true;
		boolean uguale =true;
		IndexedProduction item ;
		Production corrente;
		String x;
		Object[] ob;
		String[] right;

		//Copio il kernel item I in una List<IndexedProduction> j cosi' avro' sia i Kernel che i relativi Item che formano la chiusura
		List<IndexedProduction> j = new LinkedList<IndexedProduction>();
		j.addAll(i);
		//fino a quando  vengono aggiunti nuovi item a J
		while (flag) {
			flag=false;
			Iterator <IndexedProduction> iter = j.iterator();
			while(iter.hasNext()){
				//per ogni Item appartenente a j
				item = iter.next();
				System.out.println(item);
				ob = item.getRightSimbols().toArray();
//				System.out.println(item.getRightSimbols());
				right = Arrays.copyOf(ob,ob.length,String[].class);
				//Se il puntino si trova nell'ultima posizione, ossia l'indice di posizione � maggiore o uguale 
				//della lunghezza del Rightlist
				if(item.getCurrentCharIndex() >= item.getRightSimbols().size()){
					//esci dal while xk� ci troviamo nel caso chiusura e quindi non possiamo 
					//trovare alcuna produzione che ha nella parte sinistra l'elemento che segue il punto
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
						uguale =prodPresente(j, corrente);
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
	 * Passed a state and the symbol that follows the point, returns the kernel of the new state with the relative closure 
	 * @param State which calculated to be the new kernel
	 * @param X symbol that moves the point
	 * @return closure of new state
	 * @author Pierluigi Sottile
	 */
	public List<IndexedProduction> GoTo(List<IndexedProduction> i, String X){
		IndexedProduction item;
		Object[] ob;
		String [] right;
		List<IndexedProduction> j = new LinkedList<IndexedProduction>();
		Iterator <IndexedProduction> iter = i.iterator();
		//Per ogni produzione contenuta in I
		while (iter.hasNext()){
			item = iter.next();
			ob= item.getRightSimbols().toArray();
			right = Arrays.copyOf(ob ,ob.length ,String[].class);
			//se il puntino non si trova nell'ultima posizione
			if(!(item.getCurrentCharIndex() >= item.getRightSimbols().size()))
				//controlliamo se il carattere che segue il punto e' lo stesso di A::= a.Xc
				if(right[item.getCurrentCharIndex()].equals(X))
					//quindi aggiungiamo la produzione a J spostando il punto al carattere succesivo
					j.add(new IndexedProduction(item.getCurrentCharIndex()+1, item));
		}
		//ritorna il nuovo kernel con la relativa chiusura
		return chiusura(j);
	}
	
	/**
	 * given a grammar G we calculate the associated grammar augmented by adding the production 
	 * S ':: =. S, where S is the assiom. We calculate the closure 
	 * of it and so associated GoTo 'to have the finite state autom.
	 *  @return the autom LR(0)
	 *  @author Pierluigi Sottile
	 */
	public List<State> Item(){
		boolean flag = true;
		State stato;
		Object[] ob;
		String[] x;
		List<IndexedProduction>chiusraX = new LinkedList<IndexedProduction>();
		List<State> automa = new LinkedList<State>();
		List<IndexedProduction> aumentata= new LinkedList<IndexedProduction>();
		//creiamo una produzione S'::= S che fa diventare la nostra grammatica aumentata
		Production p = new Production("S'", grammatica.getS());
		//dopo di che inseriamo il tutto in un List<IndexedProdaction> mettendo il punto al primo posto nella produzione S'::=.S
		aumentata.add( new IndexedProduction(0, p,"$"));
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
				//per ogni  NON Terminale della grammatica
				ob= grammatica.getV().toArray();
				x = Arrays.copyOf(ob,ob.length,String[].class);
					for(int i=0;i<grammatica.getV().size();i++){
						//faccio il GoTo dello stato 
						chiusraX =GoTo(Items, x[i]);
						//TODO se si crea il nuovo stato vado a inserire lo SHIFT nella tabella degli GOTO Inquanto generato da un Terminale
						//se ChiusuraX non e' vuoto AND non e' contenuta nell'automa
						int uguale=uguale(automa,chiusraX);
						if(!chiusraX.isEmpty())
							if(uguale==-1){
								//allora lo aggiungo
								automa.add(new State(automa.size(),chiusraX));
								//e setto nella lista degli shift dello stato di origine, lo spostamento per il simbolo x[i] al nuovo stato
								stato.getShift().put(x[i],automa.size()-1);
								flag=true;
							}else{
								//se chiusurax gi� esiste aggiungo alla lista degli shift dello stato di origine, lo spostamento per il simbolo x[i] allo stato uguale
								stato.getShift().put(x[i], uguale);
							}
					}
					//ora si deve fare la stessa cosa per i non terminali
					ob = grammatica.getT().toArray();
					x = Arrays.copyOf(ob,ob.length,String[].class);
					for(int i=0;i<grammatica.getT().size();i++){
						//faccio il GoTo dello stato 
						chiusraX =GoTo(Items, x[i]);
						//TODO se si crea il nuovo stato vado a inserire lo SHIFT  o REDUCE nella tabella dei ACTION Inquanto generato da un NON Terminale
						//se ChiusuraX non e' vuoto AND non e' contenuta nell'automa
						int uguale=uguale(automa,chiusraX);
						if(!chiusraX.isEmpty())
							if(uguale==-1){
								//allora lo aggiungo
								automa.add(new State(automa.size(),chiusraX));
								//e setto nella lista degli shift dello stato di origine, lo spostamento per il simbolo x[i] al nuovo stato  
								stato.getShift().put(x[i],automa.size()-1);
								flag=true;
							}else{
								//se chiusurax gi� esiste aggiungo alla lista degli shift dello stato di origine, lo spostamento per il simbolo x[i] allo stato uguale
								stato.getShift().put(x[i], uguale);
							}
					}
					//se ho aggiunto nuovi stati all'automa devo uscire dal while(iter.hasNext()) e ricreare l'iteratore sulla nuova struttura
					if(flag)
						break;
				}
		}
		return automa;
	}
	
	/**
	 * checks if a state is present in an automaton
	 * @param autom
	 * @param state to search
	 * @return -1 If not present, otherwise returns the number state equal to the past
	 * @author Pierluigi Sottile
	 */
	public int uguale(List<State> automa, List<IndexedProduction> stato){
		State it;
		IndexedProduction prod ;
		int leng=0;
		int uguale = -1;
		int proUguali = 0; 	//questa variabile ci servir� per controllare che tutte le produzioni siano uguali
	
		Iterator<State> iter = automa.iterator();
		//per ogni stato dell'automa
		while (iter.hasNext()){
			it= iter.next();
			//prendiamo la lista delle produzioni dello stato
			Iterator<IndexedProduction> stato1 = it.getItems().iterator();
			//questa variabile contera' quante produzioni sono uguali a quello dello stato che stiamo controllando 
			proUguali = 0;
			leng=0;
			//per ogni produzione contenuta nell'iteratore
			while(stato1.hasNext()){
				prod = stato1.next();
				leng++;
				//controllo se la produzione dello stato1 � presente nello stato che stiamo controllando, in caso incrementa il contatore
				for(IndexedProduction prodStato : stato)
					//TODO:
				if (prodStato.getLeft().equalsIgnoreCase(prod.getLeft())
						&
					prodStato.getRight().equalsIgnoreCase(prod.getRight())
						&
						prodStato.getCurrentCharIndex()==prod.getCurrentCharIndex())
					proUguali++;
			}
			//se il numero di produzioni uguali � uguale alla dimensione dello stato vuol dire che
			if (proUguali == stato.size()
						&
				leng==proUguali){ 
				//sono tutti uguali e quindi 
				uguale = it.getIndex();
				break;
			}
			
		} 
		return uguale;	
	}
	
	/**
	 * checks if a production is already in a list of productions. 
	 * @param the state in which control
	 * @param prodaction to search
	 * @return  true if there is false if it is not
	 * @author Pierluigi Sottile
	 */
	public boolean prodPresente(List<IndexedProduction>j,Production corrente){
		//uso questa variabile per vedere se � gi� presente in j la setto di default a false, e poi la cambio se ne trova 2  uguali
		boolean uguale=false;
		IndexedProduction item1;
		
		Iterator <IndexedProduction> iter1 = j.iterator();
		while(iter1.hasNext()){
			//per ogni Item appartenente a j
			item1 = iter1.next();
			//controllo sia se la parte destra che la parte sinistra sono gi� presenti
			if (item1.getRight().equals(corrente.getRight()) 
								&
				item1.getLeft().equals(corrente.getLeft())
				&
				item1.getCurrentCharIndex()==0)
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