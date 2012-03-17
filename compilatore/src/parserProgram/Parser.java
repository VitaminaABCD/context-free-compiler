package parserProgram;



import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import contextFree.grammar.IGrammar;
import contextFree.grammar.Production;



public class Parser {
	static Logger logger = Logger.getLogger(Parser.class.getName());
	
	private Stack<String> stack;
	private String simbol;
	private String input;
	private IGrammar grammar;
	private List<HistoryElement> history;
	
	
	private Hashtable<String,List<String>> actionTable;
	private Hashtable<String,List<String>> gotoTable;
	
	public Parser(){
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input="";
		this.grammar=null;
		this.simbol = "";
		this.history = new LinkedList<HistoryElement>();
		this.actionTable = new Hashtable<String, List<String>>();
		this.gotoTable = new Hashtable<String, List<String>>();
	}

	public Parser(String in){
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input=in;
		this.grammar=null;  //TODO
		this.history = new LinkedList<HistoryElement>();
		this.actionTable = new Hashtable<String, List<String>>();
		this.gotoTable = new Hashtable<String, List<String>>();
		this.simbol = "";
	}
	
	
	
	public Parser(IGrammar g,
			Hashtable<String, List<String>> action,
			Hashtable<String, List<String>> goTo) {
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input="";
		this.grammar=g;
		this.actionTable=action;
		this.gotoTable=goTo;
		this.history = new LinkedList<HistoryElement>();
		this.simbol = "";
	}

	public void setActionTable(Hashtable<String, List<String>> actionTable) {
		this.actionTable = actionTable;
	}

	public void setGotoTable(Hashtable<String, List<String>> gotoTable) {
		this.gotoTable = gotoTable;
	}
	public Stack<String> getStack() {
		return this.stack;
	}
	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}
	public String getInput() {
		return this.input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	
	public IGrammar getGrammar() {
		return this.grammar;
	}

	public void setGrammar(IGrammar grammar) {
		this.grammar = grammar;
	}
	
	public List<HistoryElement> getHistory() {
		return history;
	}

	/**
	 * 
	 * @return
	 */
	public RESULT parse(){
		Integer state;
		int size=this.input.length();
		if(size==0){
			logger.info("Nessun input");
			return RESULT.INVALID_IN;
		}
		int index=0;
		while(index<size){
			String temp=Character.toString(this.input.charAt(index));  	//scorro i caratteri in input
			state=Integer.parseInt(stack.lastElement());				//stato in cima allo stack
			if(!actionTable.containsKey(temp)) {
				logger.info("La grammatica corrente non contiene '" + temp + "' tra i caratteri ammessi");
				return RESULT.INVALID_IN;
			}
			String act = actionTable.get(temp).get(state);   //valore della tabella action riga(lo stato) e colonna (il carattere)
			char [] splitAct = act.toCharArray();
			if(splitAct[0]=='s'){ //se inizia per 's' e' uno schift
				this.stack.push(String.valueOf(splitAct[1]));
				this.simbol+=temp;								//aggiunge il carattere nello stack simboli
/*   salva   */	String[] tp = new String[stack.size()]; 
/*cronologia */	stack.copyInto(tp);
				String tempSimbol = this.simbol.toString();
				this.history.add(new HistoryElement(tp,null,tempSimbol));
				logger.debug("inserito simbolo nello stack e nella storia a seguito di uno shift");				
//////END*/////			
				index++;	//punta "l'indice" al prossimo carattere
			}else if(act.equals("acc")) break;  //accetta
			else if(act.equals("err")) return RESULT.ERROR;
			else{ //e' una riduzione
				String [] production=act.split("::=");		//act=produzione nella actionTable
				for(int i=0;i<production[1].length();i++){ //rimuove beta simboli dallo stack
					stack.pop();
				}
				state=Integer.parseInt(stack.lastElement());	//prende lo stato sulla testa dello stack
				this.stack.push(gotoTable.get(production[0]).get(state));	//inserisce nello stack il valore della gotoTable corristondente a [stato][non_terminale_della_produzione_nella_actionTable]
				this.simbol=replaceLast(this.simbol,production[1], production[0]); // esegue la riduzione nello stack simboli
				String tempSimbol =simbol.toString();
				String [] tp = new String[stack.size()]; 					//formatta lo stack in uno String[] per passarlo all'history
				stack.copyInto(tp);
				this.history.add(new HistoryElement(tp, new Production(production[0], production[1]),tempSimbol)); //aggiunge l'elemento nella storia
				logger.debug("inserito simbolo e produzione nello stack e nella storia a seguito di un reduce");
			}
		}
		return RESULT.ACCEPT;
	}
	
	
	/*	(?s)    # enable dot-all option
		A       # match the character 'A'
		B       # match the character 'B'
		(?!     # start negative look ahead
  		.*?    	#   match any character and repeat it zero or more times, reluctantly
  		A      	#	match the character 'A'
  		B     	# 	match the character 'B'
		)       # end negative look a
	 * 
	 */
	private static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }
	
	
	
}