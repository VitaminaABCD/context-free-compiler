package inputParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import contextFree.grammar.GrammarFactory;
import contextFree.grammar.IGrammar;
import contextFree.grammar.Production;


import error.ERROR_TYPE;
import error.ErrorManager;

/**
 * Parse a single line grammar format.
 * es. S::= { S: TE | +TE; T: FT | xFT; E : eps; F: a | (E)} 
 * @author Paolo Pino
 *
 */
public class SingleLineInputParser extends AbstractInputParser {

	String S,file;
	List<Production> P;
	
	public SingleLineInputParser() {
		file= null;
		S="";
		P=new ArrayList<Production>();
	}
	
	public SingleLineInputParser(String in) {
		file= in;
		S="";
		P=new ArrayList<Production>();
	}

	@Override
	public IGrammar parse() throws Exception {
		String input="";
		try {
			BufferedReader f = new BufferedReader(new FileReader(file));
			input = f.readLine();
			f.close();
			input=input.replaceAll(" ", "");
			input=input.replaceAll("\\{", "");
			input=input.replaceAll("\\}","");
		} catch (IOException e) {
			ErrorManager.manage(ERROR_TYPE.INPUT_FILE,e);
		}
			
		
		String[] assioma_set = input.split("::=");
		S = assioma_set[0];
		
		String[] production = assioma_set[1].split("\\;");
		for(int i=0;i<production.length;i++){
			String[] temp = production[i].split("\\:");
			if(temp.length!=2){
				ErrorManager.manage(ERROR_TYPE.FILE_FORMAT);
				return null;
			}
			
			String[] tempP = temp[1].split("\\|");
			
			for(int j=0;j<tempP.length;j++){
				P.add(new Production(temp[0],tempP[j]));
			}
		}

		return GrammarFactory.createContextFreeGrammar(S,P);
	}
	
	public IGrammar parseString(String input) throws Exception {
			input=input.replaceAll(" ", "");
			input=input.replaceAll("\\{", "");
			input=input.replaceAll("\\}","");

		String[] assioma_set = input.split("::=");
		S = assioma_set[0];
		
		String[] production = assioma_set[1].split("\\;");
		for(int i=0;i<production.length;i++){
			String[] temp = production[i].split("\\:");
			if(temp.length!=2){
				ErrorManager.manage(ERROR_TYPE.FILE_FORMAT);
				return null;
			}
			
			String[] tempP = temp[1].split("\\|");
			
			for(int j=0;j<tempP.length;j++){
				P.add(new Production(temp[0],tempP[j]));
			}
		}

		return GrammarFactory.createContextFreeGrammar(S,P);
	}

}
