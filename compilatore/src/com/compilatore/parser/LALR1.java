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
	 * @param 	i kernel di cui va calcolata la chiusura
	 * @return 	j chiusura del kernel passato
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
						//applchiamo un algoritmo simile a quello usato per calcolare il first così come è fatto nella teoria
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
	 * costruisce le tabella Action GoTo a partire da un Automa LALR(1) e ci dice se è di tipo LALR1 o meno
	 * 
	 * @param automa da analizzare
	 */
	public void tableCostruction(List<State> automa){
		//serve per vedere se ci sono stati di ambiguità
		boolean esito=false;
		//salveremo l'indice identificativo dello stato di destinazione
		int j=0;
		List<IndexedProduction> chiusuraX =  new ArrayList<IndexedProduction>();
		//inizzializzazione tabella action Goto
		gotoTable= new String[automa.size()][grammatica.getV().size()];
		//inizzializzo la tabella a ERR inquanto i campi che non saranno riempiti con uno schift sono di errore
		for(int h=0;h<automa.size();h++)
			for(int k=0; k<grammatica.getV().size();k++)
				gotoTable[h][k]="err";
		actionTable=new String [automa.size()][grammatica.getT().size()];
		//inizzializzo la tabella a ERR inquanto i campi che non saranno riempiti con una reduce o uno schift sono di errore
		for(int h=0;h<automa.size();h++)
			for(int k=0; k<grammatica.getT().size();k++)
				actionTable[h][k]="err";
		//per ogni stato dell'automa
		for(State statoi : automa){
			//vediamo se ci sono riduzioni ed eventualmente le scriviamo
			esito=reduce(statoi);
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
					//scrivo nella tabella GOTO lo scift al posto di err
					gotoTable[statoi.getIndex()][grammatica.getV().indexOf(X)].replaceAll("err", "s"+j);
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
					//scrivo nella tabella ACTION lo scift
					esito = actionWrite(statoi.getIndex(),j,grammatica.getT().indexOf(X),"s");
			}
		}
		//stampo l'esito della creazione delle tabelle ACTION GOTO
		stampa();
		if (esito)
			System.out.println("La grammatica è LALR(1)");
		else
			System.out.println("La grammatica non è LALR(1)");
		
	}
	
	/**
	 * se il puntino è nell'ultima posizione scrive la reduce nell'action table restituisce false se ci sono conflitti
	 * @param stato stato da controllare
	 * @return
	 */
	public boolean reduce(State stato){
		//serve per vedere se ci sono stati di ambiguità
		boolean esito=false;
		//per ogni produzione dello stato
		for(IndexedProduction prodStato : stato.getKernels())
			//se il puntino si trove nell'ultima posizione ci troviamo nel caso di una reduce
			if(prodStato.getCurrentCharIndex()>=stato.size()){
				//se si tratta della reduce [S'::=S, $] è il caso dell'accettazione
				if(prodStato.getLeft().equals("S'"))
					////chiamo la funzione per scrivere nella tabella action
					esito = actionWrite(stato.getIndex(),-1,grammatica.getT().indexOf("$"),"acc");
				//se no ci troviamo nel caso di una risuzione 
				else{
					//per ogni simbolo di lookahead
					for(String la : prodStato.getLookahead())
						//chiamo la funzione per scrivere nella tabella action
						esito = actionWrite(stato.getIndex(),-1,grammatica.getT().indexOf(la),prodStato.getLeft()+"::="+prodStato.getRight());
				}
			}
		return esito;
	}
	
	/**
	 *  * scrive l'action Reduce o Scift nela tabella Action ritornando un false in caso di ambiguità restituisce false se ci sono ambiguità
	 * @param i stato attuale
	 * @param j -1 se reduce, se no lo stato di destinazione dello shift
	 * @param x simbolo per cui si va ascrivere nella tabella
	 * @param action "s" se si tratta di shift, altrimenti la produzione per cui si fa il reduce
	 * @return
	 */
	public boolean actionWrite(int i, int j, int x, String action){
		boolean esito=false;
		//se la cella è occupata da "err"
		if (actionTable[i][x].equals("err")){
			//e si tratta di uno shift
			if(action.equals("s"))
				//sostituisco la String "err" con lo shift alla posizione j
				actionTable[i][x].replaceAll("err", action + j);
			else
				// se no sostituisco la String "err" con la reduce action
				actionTable[i][x].replaceAll("err", action);
			//e setto esito a true per dire che è andato tutto bene
			esito =true;
		}
		else
			//se no evidenzio lo stato di ambiguità
			if(j>=0)
				System.out.println("stato di ambiguità"+ i+ actionTable[i][x]+ "shift" + j );
			else
				System.out.println("stato di ambiguità"+ i+ actionTable[i][x]+ action);
		return esito;
	}
	
	/**
	 * Stampa le tabelle Action Goto
	 */
	public void stampa(){
		String str="\t";
		System.out.println("\n\t\tTabella ACTION\n");
		str="\t";
		//stampo i simboli Terminali
		for (String t :grammatica.getT())
			str=str+t.toString()+"\t";
		System.out.println(str);
		//per ogni stato
		for (int i=0; i<automa.size();i++){
			str=i+"\t";
			//Per ogni terminale
			for (int j=0;j<grammatica.getT().size();j++)
				str=str+actionTable[i][j]+"\t";
			System.out.println(str);
		}
		System.out.println("\t\tTabella GOTO\n");
		//resetto la stringa
		str="\t";
		//stampo i Simboli Non Terminali
		for (String t :grammatica.getV())
			str=str+t.toString()+"\t";
		System.out.println(str);
		//per ogni stato
		for (int i=0; i<automa.size();i++){
			str=i+"\t";
			//per ogni NON TERMINALE
			for (int j=0;j<grammatica.getV().size();j++)
				str=str+gotoTable[i][j]+"\t";
			System.out.println(str);
		}
		
	}
}