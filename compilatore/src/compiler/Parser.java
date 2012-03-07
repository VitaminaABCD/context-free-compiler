package compiler;

import java.util.Stack;

import org.apache.log4j.Logger;

import com.compilatore.grammar.IGrammar;

public class Parser {
	static Logger logger = Logger.getLogger(Parser.class.getName());
	
	private Stack<String> stack;
	private String input;
	private IGrammar grammar;

	private String[][] actionTable;
	private String[][] gotoTable;
	
	public Parser(){
		this.stack = null;
		this.input="";
		grammar=null;
	}
	
	public Parser(String in){
		this.stack = new Stack<String>();
		this.stack.add("0");
		this.input=in;
		this.grammar=null;  //TODO
	}
	
	public void setActionTable(String[][] actionTable) {
		this.actionTable = actionTable;
	}

	public void setGotoTable(String[][] gotoTable) {
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
	
	public RESULT parse(){
		String result=this.input;
		Integer state;
		if(result.length()>0){
			logger.info("Nessun input");
			return RESULT.INVALID_IN;
		}
		int index=0;
		while(true){
			String temp=Character.toString(result.charAt(index));
			state=Integer.parseInt(stack.firstElement());
			String act = actionTable[state][grammar.getV().indexOf(temp)];       //non ho un riferimento a quale colonna si riferisce al carattere temp letto
			char [] splitAct = act.toCharArray();
			if(splitAct[0]=='s'){
				stack.push(String.valueOf(splitAct[1]));
				index++;
			}else if(act=="acc") break;
			else if(act=="err") return RESULT.ERROR;
			else{ //è una riduzione
				String [] t=act.split("::=");
				for(int i=0;i<t[1].length();i++){ //rimuove beta simboli dallo stack
					stack.pop();
				}
				state=Integer.parseInt(stack.firstElement());
				stack.push();
			}
		}
	}
	
}
