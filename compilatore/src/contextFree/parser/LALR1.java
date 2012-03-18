package contextFree.parser;

import java.util.*;

import org.apache.log4j.Logger;

import contextFree.grammar.IGrammar;
import contextFree.grammar.Production;


import error.ERROR_TYPE;
import error.ErrorManager;

public class LALR1 extends LR0{
	static Logger logger = Logger.getLogger(LALR1.class.getName());
	
	private boolean isAmbiguous=false;
	private String ambiguo="";
	private Automa automa;


	private String[][] actionTable;
	private String[][] gotoTable;
	
	public LALR1(){
		automa=new Automa();
		grammatica=null;
	}
	
	public LALR1(IGrammar gram){
		automa = new Automa();
		setGrammar(gram);
		}
	
	public String[][] getActionTable() {
		return actionTable;
	}

	public void setActionTable(String[][] actionTable) {
		this.actionTable = actionTable;
	}

	public String[][] getGotoTable() {
		return gotoTable;
	}

	public void setGotoTable(String[][] gotoTable) {
		this.gotoTable = gotoTable;
	}
	
	@Override
	public Automa getAutoma() {
		return automa;
	}
		
	@Override
	public IGrammar getGrammar(){
		return this.grammatica;
	}
	
	@Override
	public void setGrammar(IGrammar gram) {
		grammatica=gram;
	}
	
	@Override
	public int init() throws Exception{
		try{
			Automa AutomaLR0 = new Automa(Item());

			logger.debug("Inizializzazione dell'automa con stati contenenti i kernel LR0");
			this.automa.setStates(AutomaLR0.newItemsFromKernels());
			
			//////////////////potrebbe non essere necessario (ci vorrebbero meno cicli///////////////
			//genera i simboli e rimuove il dollaro in tutte le produzioni ad eccezione della prima//
			calculateSymbol(this.automa);
			this.automa.removeDollarLookahed();
			////////////////////////////////////////////////////////////////////////////////////////
			
			int count=1; //variabile di supporto per il numero di cicli nel logger
			int flag=1;
			while(flag!=0){
				flag=0;
					logger.debug("\nCiclo "+count);
					flag+=calculateSymbol(this.automa);
					count++;
				}
			
			for(State s : this.automa.getStates()){				//Calcolo la chiusura dei kernel per completare gli stati
				s.setItems(chiusuraLR1(s.getItems()));
			}
			
			if(tableCostruction()==1) return 1;
			else {
				isAmbiguous=true;
				return 0;
			}
		}catch (Exception e) {
			ErrorManager.manage(ERROR_TYPE.LALR1_INIT,e);
			return -1;
		}
	}
	
	private int calculateSymbol(Automa atm) throws Exception{
		List<IndexedProduction> J;
		int flag = 0;
		try{
			for(State s : atm.getStates()){								//  1.Per ogni stato dell'automa 
				for(IndexedProduction K : s.getKernels()){				//	1.1 per ogni kernel dello stato
					/**Trasformo la singola produzione k in una list per poterla passare
					** a chiusura LR1 */
					List<IndexedProduction> temp = new LinkedList<IndexedProduction>();
					temp.add(K);
					J = chiusuraLR1(temp);								//1.1.1 calcolo la chiusura
					for(IndexedProduction jItem : J){						
						for(String lookahead : jItem.getLookahead()){   //1.1.1.1	per ogni simbolo di lookahead
							Integer i;
							State tmpState;
							if((i=s.gotoStateIndex(jItem.getCharAfter()))!=null)	//1.1.1.1.1  mi faccio dire a quale stato arrivo per un determinato simbolo
							if((tmpState = atm.getState(i))!=null){
								for(IndexedProduction p : tmpState.getItems()){		//1.1.1.1.1.1 confronto ogni produzione all'interno dello stato di arrivo con la produzione della chiusura J
									if(p.compare(jItem)	&& 
											p.getCurrentCharIndex() == (jItem.getCurrentCharIndex()+1)){
										Set<String> kLook = K.getLookahead();
										if(!kLook.contains(lookahead)){				//1.1.1.1.1.2 se non contiene il simbolo di lookahead corrente il simbolo viene generato per produzione che "segue" nello stato successivo
											if(p.getLookahead().add(lookahead)) flag = 1;
											logger.debug("Simbolo [" + lookahead + "] generato per "+ p.toString());
										}else{										//1.1.1.1.1.2 se contiene il simbolo di lookahead corrente i simboli della produzione in esame della chiusura vengono propagati alla produzione che "segue" nello stato successivo
											if(p.getLookahead().addAll(K.getLookahead())) flag = 1;
											logger.debug("Simboli "+K.getLookahead()+" propagati da " + K.toString() +"a "+p.toString());
										}
									}
								}	
							}					
						}
					}
				}
			}
			return flag;
		}catch (Exception e) {
			ErrorManager.manage(ERROR_TYPE.LOOKAHEAD_PROPAGATION_ERROR, e);
			return -1;
		}
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
				//Se il puntino si trova nell'ultima posizione, ossia l'indice di posizione � maggiore o uguale 
				//della lunghezza del Rightlist
				punto= item.getCurrentCharIndex();
				if(punto >= item.getRightList().size()){
					//esci dal while xk� ci troviamo nel caso chiusura e quindi non possiamo 
					//trovare alcuna produzione che ha nella parte sinistra l'elemento che segue il punto
					//quindi inseriamo nella tabella action la Reduce per la produzione giusta...
					//ossi R = P::=aBc.
					//se la casella � gi� occupata abbiamo un conflitto e cerchiamo di risolverlo avvalendocci dei simboli di lookahead
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
						//applchiamo un algoritmo simile a quello usato per calcolare il first cos� come � fatto nella teoria
						look= new HashSet<String>();
						//conrtolliamo se esiste un elemento dopo il simbolo evidenziato dal punto [A:= a.Bc, d] 
						if(!(punto+1 >= item.getRightList().size())){
							//se esiste allora aggiungiamo ai simboli di lookahead i first(cd)
							//quindi per prima cosa controlliamo se c � un terminale o un non terminale
							la = (String) right[punto+1];
							//se "la" � un Terminale aggiungiamo il simbolo alla lista dei  lookahead per la produzione che dovremo inserire
							if (grammatica.getT().contains(la))
								//aggiungiamo il simbolo di lookahead per la produzione che dobbiamo inserire nella chiusura
								look.add(la);
							//se � un NoN Terminale aggiungiamo i first legati a quel simbolo
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
							//se la produzione � gi� presente all'interno di j dobbiamo semplicemente aggiornare i simboli di lookahead
							Iterator <IndexedProduction> iter1 = j.iterator();
							//quindi scorriamo le produzioni d j fino a quando non troviamo quella che dobbiamo modificare
							while(iter1.hasNext()){
								//per ogni Item appartenente a j
								item1 = iter1.next();
								//controllo che  sia se la parte destra che la parte sinistra sono gi� presenti
								if (item1.getRight().equals(corrente.getRight()) & item1.getLeft().equals(corrente.getLeft()))
									{
									//quindi aggiorno i simbolii di look ahead per questa produzione e esco dal ciclo perch� ho gi� trovato quella che ci interessava
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
	
	/**
	 * costruisce le tabella Action GoTo a partire da un Automa LALR(1) e ci dice se � di tipo LALR1 o meno
	 * 
	 * @param automa da analizzare
	 * @return 1 se la grammatica � di tipo LALR(1), 0 altrimenti
	 * @throws Exception 
	 */
	public int tableCostruction() throws Exception{
		try{
			//serve per vedere se ci sono stati di ambiguit�
			boolean esito=false;
			//inizzializzazione tabella action Goto
			gotoTable= new String[automa.size()][grammatica.getV().size()];
			//inizzializzo la tabella a ERR inquanto i campi che non saranno riempiti con uno schift sono di errore
			for(int h=0;h<automa.size();h++)
				for(int k=0; k<grammatica.getV().size();k++)
					gotoTable[h][k]="err";
			actionTable=new String [automa.size()][grammatica.getT().size()];
			//inizzializzo la tabella a ERR inquanto i campi che non saranno riempiti con una reduce o uno schift sono di errore
			for(int h=0;h<automa.size();h++)
				//controllo nella lista degli shift se � presente X e in caso lo scrivo nella tabella
				for(int k=0; k<grammatica.getT().size();k++)
					actionTable[h][k]="err";
			
			//per ogni stato dell'automa
			for(State statoi : automa.getStates()){
				//vediamo se ci sono riduzioni ed eventualmente le scriviamo
				if(!reduce(statoi))
				esito= true;
				//per ogni NON TERMINALE X nella grammatica
				for(String X :grammatica.getV()){
					//controllo nella lista degli shift dello stato se � presente per X
					if(statoi.getShift().get(X)!=null)
						//scrivo nella tabella GOTO lo scift al posto di err
						gotoTable[statoi.getIndex()][grammatica.getV().indexOf(X)]= statoi.getShift().get(X).toString();
				}
				//per ogni simbolo Terminale
				for(String X :grammatica.getT()){
					//controllo nella lista degli shift dello stato se � presente X
					if(statoi.getShift().get(X)!=null)
						//scrivo nella tabella ACTION lo shift 
						if(!actionWrite(statoi.getIndex(),statoi.getShift().get(X),grammatica.getT().indexOf(X),"s"))
							esito=true;
				}
			}
			return esito? 0 : 1;
		}catch (Exception e) {
			ErrorManager.manage(ERROR_TYPE.TABLE_CONSTRUCTION, e);
			return -1;
		}
	}
	
	/**
	 * se il puntino � nell'ultima posizione scrive la reduce nell'action table restituisce false se ci sono conflitti
	 * @param stato stato da controllare
	 * @return
	 */
	public boolean reduce(State stato){
		//serve per vedere se ci sono stati di ambiguit�
		boolean esito=false;
		boolean ret=true;
		//per ogni produzione dello stato
		for(IndexedProduction prodStato : stato.getKernels())
			//se il puntino si trove nell'ultima posizione ci troviamo nel caso di una reduce
			if(prodStato.getCurrentCharIndex()>=prodStato.getRightList().size()){
				//se si tratta della reduce [S'::=S, $] � il caso dell'accettazione
				if(prodStato.getLeft().equals("S'")){
					////chiamo la funzione per scrivere nella tabella action
					esito = actionWrite(stato.getIndex(),-1,grammatica.getT().indexOf("$"),"acc");
					if (!esito)
						ret=false;
						
				}
				//se no ci troviamo nel caso di una risuzione 
				else{
					//per ogni simbolo di lookahead
					for(String la : prodStato.getLookahead())
						//chiamo la funzione per scrivere nella tabella action
						esito = actionWrite(stato.getIndex(),-1,grammatica.getT().indexOf(la),prodStato.getLeft()+"::="+prodStato.getRight());
					if (!esito)
						ret=false;
				}
			}
		return ret;
	}
	
	/**
	 *  * scrive l'action Reduce o Scift nela tabella Action ritornando un false in caso di ambiguit� restituisce false se ci sono ambiguit�
	 * @param i stato attuale
	 * @param j -1 se reduce, se no lo stato di destinazione dello shift
	 * @param x simbolo per cui si va ascrivere nella tabella
	 * @param action "s" se si tratta di shift, altrimenti la produzione per cui si fa il reduce
	 * @return
	 */
	public boolean actionWrite(int i, int j, int x, String action){
		boolean esito=false;
		//se la cella � occupata da "err"
		if (actionTable[i][x].equals("err")){
			//e si tratta di uno shift
			if(action.equals("s"))
				//sostituisco la String "err" con lo shift alla posizione j
				actionTable[i][x]= action + j;
			else
				// se no sostituisco la String "err" con la reduce action
				actionTable[i][x]= action;
			//e setto esito a true per dire che � andato tutto bene
			esito =true;
		}
		else
			//se no evidenzio lo stato di ambiguit�
			if(j>=0)
				ambiguo+="\nAmbiguita' allo stato "+ i+": "+ actionTable[i][x]+ ", shift " + j ;
			else
				ambiguo+="\nAmbiguita' allo stato "+ i+ ": "+ actionTable[i][x]+ ", "+action;
		return esito;
	}
	@Override
	public boolean isAmbiguos(){
		return isAmbiguous;
	}
	/**
	 * restiruisce una stringa con le  tabelle Action Goto
	 * @return
	 */
	public String printTable(){
		String str="\n\t\tTabella ACTION\n\t";
		//stampo i simboli Terminali
		for (String t :grammatica.getT())
			str+=t.toString()+"\t";
		//per ogni stato
		for (int i=0; i<automa.size();i++){
			str+="\n"+i+"\t";
			//Per ogni terminale
			for (int j=0;j<grammatica.getT().size();j++)
				str=str+actionTable[i][j]+"\t";
		}
		str+="\n\n\t\tTabella GOTO\n\t";
		//stampo i non terminali
		for (String t :grammatica.getV())
			str+=t.toString()+"\t";
		//per ogni stato
		for (int i=0; i<automa.size();i++){
			str+="\n"+i+"\t";
//			Per ogni NON Terminale
			for (int j=0;j<grammatica.getV().size();j++)
				str=str+gotoTable[i][j]+"\t";
		}
		return str;
	}

	@Override
		public String toString(){
			if(isAmbiguous) return ambiguo;
	//		return "Grammatica:\n"+this.grammatica.toString()+ "\n" + this.automa.toString() + "\n"+printTable();
			return printTable();
		}
}