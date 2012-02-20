package com.compilatore.inputParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.compilatore.error.ERROR_TYPE;
import com.compilatore.error.ErrorManager;
import com.compilatore.grammar.GrammarFactory;
import com.compilatore.grammar.IGrammar;
import com.compilatore.grammar.Production;

public class SingleLineInputParser extends A_InputParser {

	String S,file;
	List<Production> P;
	
	public SingleLineInputParser(String in) {
		file= in;
		S="";
		P=new ArrayList<Production>();
	}

	@Override
	public IGrammar parse() {
		String input="";
		try {
			BufferedReader f = new BufferedReader(new FileReader(file));
			input = f.readLine();
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

		return GrammarFactory.createGrammar(S,P);
	}

}
