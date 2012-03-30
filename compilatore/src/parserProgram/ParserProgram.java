package parserProgram;



//import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import contextFree.grammar.IGrammar;
import contextFree.grammar.Production;

/**
 * This class is responsible of the parsing of input string, and stack parsing creation.
 * It also save the parsing chronology.
 * It use a Grammar and its Action and Goto table.
 * All this operation are executed with parse() method.
 * @author Paolo Pino
 */
public class ParserProgram {
	static Logger logger = Logger.getLogger(ParserProgram.class.getName());
	
	private Stack<String> stack;
	private String simbol;
	private String input;
	private IGrammar grammar;
	private List<HistoryElement> history;
	
	
	private Hashtable<String,List<String>> actionTable;
	private Hashtable<String,List<String>> gotoTable;
	
	public ParserProgram(){
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input="";
		this.grammar=null;
		this.simbol = "";
		this.history = new LinkedList<HistoryElement>();
		this.actionTable = new Hashtable<String, List<String>>();
		this.gotoTable = new Hashtable<String, List<String>>();
	}

	public ParserProgram(String in){
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input=in;
		this.grammar=null;  //TODO
		this.history = new LinkedList<HistoryElement>();
		this.actionTable = new Hashtable<String, List<String>>();
		this.gotoTable = new Hashtable<String, List<String>>();
		this.simbol = "";
	}
	
	
	
	public ParserProgram(IGrammar g,
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
	 * The leading method of the class, responsible of the parsing of input string and consequent stack parsing and history creation. 
	 * @return RESULT.ACCEPT if the string has been accepted, RESULT.ERROR if not, RESULT.INVALID_ID if the input string is invalid 
	 * @throws InterruptedException 
	 */
	public RESULT parse() throws InterruptedException{
		Integer state;
		int size=this.input.length();
		if(size==0){
			logger.info("Nessun input");
			return RESULT.INVALID_IN;
		}
		int index=0;
		
		Enumeration<String> e = actionTable.keys();   
	    //iterate through Hashtable keys Enumeration
		List<String> key = new LinkedList<String>();
	    while(e.hasMoreElements()){
	      key.add(e.nextElement());
		}
	
		while(index<size){
			String temp=Character.toString(this.input.charAt(index));
			int max=0,min=0;
			boolean founded=false;
			Pattern pattern = Pattern.compile(Pattern.quote(temp)+".*");
			for(String k : key){
				Matcher matcher = pattern.matcher(k);
				while(matcher.find()){
					int sz=matcher.group().length();
					if(sz>max) max= sz;
					if(sz<min) min= sz;
					founded=true;
					}
			}
			
			if(!founded){
				logger.info("La grammatica corrente non contiene '" + temp + "' tra i caratteri ammessi");
				return RESULT.INVALID_IN;
			}
			
			founded=false;
			while(max>min){
				if(index+max<this.input.length()) temp=this.input.substring(index,index+max);
				else temp=this.input.substring(index);
				if(key.contains(temp)){
					founded=true;
					break;
				}
				max--;
			}
			if(!founded) return RESULT.INVALID_IN; //TODO: replicato perchè in teoria dovrebbe sempre trovare qualcosa dato il controllo che avviene in precedenza sulla lunghezza del result
			
				
//			if(!actionTable.containsKey(temp)) {
//				logger.info("La grammatica corrente non contiene '" + temp + "' tra i caratteri ammessi");
//				return RESULT.INVALID_IN;
//			}

			
			
			state=Integer.parseInt(stack.lastElement());				//stato in cima allo stack
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
				index+=max;	//punta "l'indice" al prossimo carattere
			}else if(act.equals("acc")) break;  //accetta
			else if(act.equals("err")) return RESULT.ERROR;
			else{ //e' una riduzione
				String [] production=act.split("::=");		//act=produzione nella actionTable.
				
				
				String right="";
				for(String t : key){	
					right=production[1].replaceAll(Pattern.quote(t), t+" ");
				}	
				
			    String [] split = right.split(" ");
	
				for(int i=0;i<split.length;i++){ //rimuove beta simboli dallo stack  			//TODO: rimuove beta simboli, nonn beta caratteri
					stack.pop();
				}
				state=Integer.parseInt(stack.lastElement());	//prende lo stato sulla testa dello stack
				this.stack.push(gotoTable.get(production[0]).get(state));	//inserisce nello stack il valore della gotoTable corristondente a [stato][non_terminale_della_produzione_nella_actionTable]
				this.simbol=replaceLast(this.simbol,production[1], production[0]); // esegue la riduzione nello stack simboli
				String tempSimbol =simbol.toString();
				String [] tp = new String[stack.size()]; 					//formatta lo stack in uno String[] per passarlo all'history
				stack.copyInto(tp);
				this.history.add(new HistoryElement(tp, new Production(production[0], production[1],getV(),getT()),tempSimbol)); //aggiunge l'elemento nella storia
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

	public List<String> getT(){
		List<String> result = new LinkedList<String>();
		Enumeration<String> e = actionTable.keys();   
	    while(e.hasMoreElements()){
	    	String elem = e.nextElement();
	    	result.add(elem);
		}
	    return result;
	}

	public List<String> getV(){
		List<String> result = new LinkedList<String>();
		Enumeration<String> e = gotoTable.keys();   
	    while(e.hasMoreElements()){
	    	String elem = e.nextElement();
	    	result.add(elem);
		}
	    return result;
	}
	
}