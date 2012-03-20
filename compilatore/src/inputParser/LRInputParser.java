package inputParser;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import contextFree.grammar.IGrammar;

import parserProgram.Parser;
 
/**	Parse a txt file with action table, goto table and grammar in one line format (*.1l).
 * @author Paolo Pino
 *
 */
public class LRInputParser extends InputParser {
	static Logger logger = Logger.getLogger(LRInputParser.class.getName());
	
	private String input;

	public LRInputParser(){
		input="";
	}
	public LRInputParser(String in) {
		this.input=in;
	}

	/** Parse method
	 * 
	 */
	@Override
	public Parser parse() throws Exception {
		Hashtable<String,List<String>> actionTable = new Hashtable<String, List<String>>();
		Hashtable<String,List<String>> gotoTable = new Hashtable<String, List<String>>();
		IGrammar grammar = null;
		try{
			BufferedReader f
			   = new BufferedReader(new FileReader(this.input));
			String in;
			while((in=f.readLine())!=null){
				if(in.contains("Tabella ACTION")){
					readTable(f,actionTable);
				}
				else if(in.contains("Tabella GOTO")){
					readTable(f,gotoTable);
				}
				else if(in.contains("Grammatica")){
					in=f.readLine();
					grammar = new SingleLineInputParser().parseString(in);
				}
			}	
			f.close();
			return new Parser(grammar, actionTable,gotoTable);
			//es.     actionTable.get("*").get(8)); 		recupera lo stato 8 del simbolo "*" nella tabella action
		}catch(IOException e){
			logger.error("Error in file read operation",e);
		}
		return null;
	}
	
	/**
	 * read the table in the first part of the file
	 * and store it in an hashtable with simbol as key and a list
	 * with an element for each state
	 * @param f the input file buffer
	 * @param table the hashtable where the data is stored
	 * @throws IOException
	 */
	private void readTable(BufferedReader f, Hashtable<String, List<String>> table) throws IOException {
		String in=f.readLine();
		String []simbol =  in.split("\t");
		for(int i=1;i<simbol.length;i++) table.put(simbol[i], new LinkedList<String>());
		while(true){
			in = f.readLine();
			String[] temp =  in.split("\t");
			if(temp.length!=simbol.length) break;
			for(int i = 1; i<temp.length;i++)
				table.get(simbol[i]).add(temp[i]);
		}	
	}
}
