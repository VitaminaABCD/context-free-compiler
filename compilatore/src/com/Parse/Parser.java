package com.Parse;

import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.compilatore.grammar.IGrammar;

public class Parser {
	static Logger logger = Logger.getLogger(Parser.class.getName());
	
	private Stack<String> stack;
	private String input;
	private IGrammar grammar;

	Hashtable<String,List<String>> actionTable = new Hashtable<String, List<String>>();
	Hashtable<String,List<String>> gotoTable = new Hashtable<String, List<String>>();
	
	public Parser(){
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input="";
		grammar=null;
	}

	public Parser(String in){
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input=in;
		this.grammar=null;  //TODO
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
	}

	public void setActionTable(Hashtable<String, List<String>> actionTable) {
		this.actionTable = actionTable;
	}

	public void setGotoTable(Hashtable<String, List<String>> gotoTable) {
		this.gotoTable = gotoTable;
	}
	public Stack<String> getStack() {
		return stack;
	}
	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	
	public IGrammar getGrammar() {
		return grammar;
	}

	public void setGrammar(IGrammar grammar) {
		this.grammar = grammar;
	}
	
	public RESULT parse(){
		String result=this.input;
		Integer state;
		if(result.length()==0){
			logger.info("Nessun input");
			return RESULT.INVALID_IN;
		}
		int index=0;
		while(true){
			String temp=Character.toString(result.charAt(index));
			state=Integer.parseInt(stack.lastElement());
			String act = actionTable.get(temp).get(state);   //TODO: ritorna null se il simbolo non è presente tra le key->solleva eccezione
			char [] splitAct = act.toCharArray();
			if(splitAct[0]=='s'){
				stack.push(String.valueOf(splitAct[1]));
				index++;
			}else if(act.equals("acc")) break;
			else if(act.equals("err")) return RESULT.ERROR;
			else{ //è una riduzione
				String [] t=act.split("::=");
				for(int i=0;i<t[1].length();i++){ //rimuove beta simboli dallo stack
					stack.pop();
				}
				state=Integer.parseInt(stack.firstElement());
				stack.push(gotoTable.get(t[0]).get(state));
				index++;
			}
		}
		return RESULT.ACCEPT;
	}
	
}
