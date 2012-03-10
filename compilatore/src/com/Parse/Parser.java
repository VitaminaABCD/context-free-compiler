package com.Parse;


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.compilatore.grammar.IGrammar;
import com.compilatore.grammar.Production;


public class Parser {
	static Logger logger = Logger.getLogger(Parser.class.getName());
	
	private Stack<String> stack;
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
			if(splitAct[0]=='s'){ //se inizia per 's' è uno schift
				this.stack.push(String.valueOf(splitAct[1]));
				String[] tp = new String[stack.size()]; 
				stack.copyInto(tp);
				this.history.add(new HistoryElement(tp));
				logger.debug("inserito simbolo nello stack e nella storia a seguito di uno shift");
				index++;
			}else if(act.equals("acc")) break;  //accetta
			else if(act.equals("err")) return RESULT.ERROR;
			else{ //e' una riduzione
				String [] production=act.split("::=");
				for(int i=0;i<production[1].length();i++){ //rimuove beta simboli dallo stack
					stack.pop();
				}
				state=Integer.parseInt(stack.lastElement());
				this.stack.push(gotoTable.get(production[0]).get(state));
				String [] tp = new String[stack.size()]; 
				stack.copyInto(tp);
				this.history.add(new HistoryElement(tp, new Production(production[0], production[1])));
				logger.debug("inserito simbolo e produzione nello stack e nella storia a seguito di un reduce");
			}
		}
		return RESULT.ACCEPT;
	}
}