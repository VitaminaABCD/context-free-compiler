package com.compilatore.parser;

import java.util.*;

import javax.swing.Action;

import org.apache.log4j.Logger;

import com.compilatore.grammar.IGrammar;
import com.compilatore.grammar.Production;

public class LALR1 extends LR0{
	static Logger logger = Logger.getLogger(LALR1.class.getName());
	
	private Automa automa;
	private List<State> state; 
	private String[][] actionTable;
	private String[][] gotoTable;
	
	public LALR1(){
		automa=new Automa();
		grammatica=null;
		state=new LinkedList<State>();
	}
	
	public LALR1(IGrammar gram){
		automa = new Automa();
		setGrammar(gram);
		state=new ArrayList<State>();
	}
	
	@Override
	public void setGrammar(IGrammar gram) {
		grammatica=gram;
	}
	
	@Override
	public void calculateKernels(){
		Automa AutomaLR0 = new Automa(Item());
		logger.debug("Inizializzazione dell'automa con stati contenenti i kernel LR0");
		
		this.automa.setStates(AutomaLR0.newItemsFromKernels());
		
		System.out.println(this.automa.toString());
		List<State> states = AutomaLR0.getStates();
		int flag=1;
		
		for(State s : states){
			flag+=calculateLookahead(s.getKernels());
		}
		this.automa.removeDollarLookahed();
		
		System.out.println(automa.toString());
		states = automa.getStates();
		while(flag!=0){
			flag=0;
			System.out.println("ciclo");
			for(State s : states){
				flag+=calculateLookahead(s.getKernels());
			}
		}
		
		for(State s: automa.getStates()){
			s.setItems(chiusuraLR1(s.getKernels()));
		}
	}
	
	
	public int calculateLookahead(List<IndexedProduction> LR0kernels){
		List<IndexedProduction> J;
		int flag=0;
		for(IndexedProduction K : LR0kernels){
			/**Trasformo la singola produzione k in una list per poterla passare
			** a chiusura LR1 */
			List<IndexedProduction> temp = new LinkedList<IndexedProduction>();
			temp.add(K);
			J = chiusuraLR1(temp);
			for(IndexedProduction jItem : J){
				String X = jItem.getCharAfter();
				List<IndexedProduction> cl = chiusura(LR0kernels);
				List<IndexedProduction> Goto = GoTo(cl, X);
				for(String lookahead : jItem.getLookahead()){
					
					
					/////////////////////////////////
					for(IndexedProduction p : Goto){
							if( p.compare(jItem)
											&& 
								p.getCurrentCharIndex() == (jItem.getCurrentCharIndex()+1)
							){
								if(!K.getLookahead().contains(lookahead)){
									p.getLookahead().add(lookahead);
									logger.debug("Simbolo [" + lookahead + "] generato");
								}else{
									logger.debug("Simboli "+K.getLookahead()+" propagati da " + K.toString());
									p.getLookahead().addAll(K.getLookahead());
								}
								if(generateLookahead(p)) flag=1;
							}
					}
					////////////////////////////////
				}
			}
		}
		return flag;
	}
	
	public int calculateLookahead2(List<IndexedProduction> LR0kernels){
		List<IndexedProduction> J;
		int flag=0;
		for(IndexedProduction K : LR0kernels){
			/**Trasformo la singola produzione k in una list per poterla passare
			** a chiusura LR1 */
			List<IndexedProduction> temp = new LinkedList<IndexedProduction>();
			temp.add(K);
			J = chiusuraLR1(temp);
			for(IndexedProduction jItem : J){
				String X = jItem.getCharAfter();
				List<IndexedProduction> cl = chiusura(LR0kernels);
				List<IndexedProduction> Goto = GoTo(cl, X);
				for(String lookahead : jItem.getLookahead()){
					
					
					/////////////////////////////////
					for(IndexedProduction p : Goto){
							if( p.compare(jItem)
											&& 
								p.getCurrentCharIndex() == (jItem.getCurrentCharIndex()+1)
							){
								if(!K.getLookahead().contains(lookahead)){
									p.getLookahead().add(lookahead);
									logger.debug("Simbolo [" + lookahead + "] generato");
								}else{
									logger.debug("Simboli "+K.getLookahead()+" propagati da " + K.toString());
									p.getLookahead().addAll(K.getLookahead());
								}
								if(generateLookahead(p)) flag=1;
							}
					}
					////////////////////////////////
				}
			}
		}
		return flag;
	}
	private boolean generateLookahead(IndexedProduction p) {
		boolean result=false;
		for( State s : this.automa.getStates()){
			for(IndexedProduction k : s.getItems()){
				if(p.compare(k)) {
					result=k.addLookahead(p.getLookahead());
					if(result){
						logger.debug("aggiunto " + p.getLookahead() + " a " + k.toString() +" nello stato "+ s.getIndex());
						return result;
					}
				}
			}
		}
		return result;
	}

	/**
	 *Passata una lista di produzione I che formano il Kernel di uno stato, restitusce la chiusura di esso 
	 * @param 	i
	 * @return 	j
	 */
	public List<IndexedProduction> chiusuraLR1 (List<IndexedProduction> i){
		boolean flag =true;
		boolean uguale =true;
		IndexedProduction item ;
		IndexedProduction item1 ;
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
						//TODO a questo punto si deve applciare una specie di algoritmo simile a quello usato per calcolare il first così come è fatto nella teoria
						look= new HashSet<String>();
						//conrtolliamo se esiste un elemento dopo il simbolo evidenziato dal punto [A:= a.Bc, d] 
						if(!(punto+1 >= item.getRightList().size())){
							//se esiste allora aggiungiamo ai simboli di lookahead i first(cd)
							//quindi per prima cosa controlliamo se c è un terminale o un non terminale
							la = (String) right[punto+1];
							//se "la" è un Terminale aggiungiamo il simbolo alla lista dei  lookahead per la produzione che dovremo inserire
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
						uguale =prodPresente(j, corrente);
						if(!uguale){
							//aggiungo la produzione mettendo il punto come primo elemnto B::=.Z e inserendo i nuovi termini di lookahead
							j.add(new IndexedProduction(corrente,look));
							flag=true;
						}
						else{
							//se la produzione è già presente all'interno di j dobbiamo semplicemente aggiornare i simboli di lookahead
							Iterator <IndexedProduction> iter1 = j.iterator();
							//quindi scorriamo le produzioni d j fino a quando non troviamo quella che dobbiamo modificare
							while(iter1.hasNext()){
								//per ogni Item appartenente a j
								item1 = iter1.next();
								//controllo che  sia se la parte destra che la parte sinistra sono già presenti
								if (item1.getRight().equals(corrente.getRight()) & item1.getLeft().equals(corrente.getLeft()))
									{
									//quindi aggiorno i simbolii di look ahead per questa produzione e esco dal ciclo perchè ho già trovato quella che ci interessava
										item1.getLookahead().addAll(look);
										break;
									}
							}
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
	
	@Override
	public String toString(){
		return this.automa.toString();
	}

	
	/**
	 * costruisce le tabella Action GoTo a partire da un Automa LALR(1)
	 * 
	 * @param automa
	 */
	public void tableCostruction(List<State> automa){
		//salveremo l'indice identificativo dello stato di destinazione
		int j=0;
		List<IndexedProduction> chiusuraX =  new ArrayList<IndexedProduction>();
		//inizializzazione tabelle action goTo
		gotoTable= new String[automa.size()][grammatica.getV().size()];
		actionTable=new String [automa.size()][grammatica.getT().size()];
		//per ogni stato dell'automa
		for(State statoi : automa){
			//vediamo se ci sono riduzioni ed eventualmente le scriviamo
			reduce(statoi);
			//per ogni NON TERMINALE X nella grammatica
			for(String X :grammatica.getV()){
				//facciamo il GoTo per lo statoi con il  terminale x
				chiusuraX = GoTo(statoi.getItems(), X);
				//recuperiamo l'indice dello stato chiusurax
				j=uguale(automa, chiusuraX);
				//se chiusuraX non è vuoto ed è stato trovato
				if(!chiusuraX.isEmpty()
						&
						j!=-1)
					gotoTable[statoi.getIndex()][grammatica.getV().indexOf(X)]="s"+j;
			}
			//per ogni TERMINALE X nella grammatica
			for(String X :grammatica.getT()){
				//facciamo il GoTo per lo statoi con il  terminale x
				chiusuraX = GoTo(statoi.getItems(), X);
				//recuperiamo l'indice dello stato chiusurax
				j=uguale(automa, chiusuraX);
				//se chiusuraX non è vuoto ed è stato trovato
				if(!chiusuraX.isEmpty()
						&
						j!=-1)
					actionTable[statoi.getIndex()][grammatica.getT().indexOf(X)]="s"+j;
			}
		}
			
		
	}
	
	/**
	 * se il puntino è nell'ultima posizione scrive la reduce nell'action table
	 * @param stato
	 */
	public void reduce(State stato){
		//per ogni produzione dello stato
		for(IndexedProduction prodStato : stato.getKernels())
			//se il puntino si trove nell'ultima posizione ci troviamo nel caso di una reduce
			if(prodStato.getCurrentCharIndex()>=stato.size()){
				//se si tratta della reduce [S'::=S, $] è il caso dell'accettazione
				if(prodStato.getLeft().equalsIgnoreCase("S'"))
					actionTable[stato.getIndex()][grammatica.getT().indexOf("$")] ="acc";
				//se no ci troviamo nel caso di una risuzione 
				else{
					//per ogni simbolo di lookahead
					for(String la : prodStato.getLookahead())
					actionTable[stato.getIndex()][grammatica.getT().indexOf(la)] = prodStato.getLeft()+"::="+prodStato.getRight();
				}
			}
	}
}