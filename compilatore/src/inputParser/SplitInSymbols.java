package inputParser;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parserProgram.ParserProgram;

/**
 * Split the input string in symbols list
 * @author Paolo Pino
 *
 */
public class SplitInSymbols implements Runnable {
	static Logger logger = Logger.getLogger(SplitInSymbols.class.getName());
	
	private String obj;
	private List<String> result,Symbols;
	//Constructor
	public SplitInSymbols(){this.obj=null;this.result=this.Symbols=null;}
	public SplitInSymbols(String in,List<String> symb){this.obj=in;this.Symbols=symb;}
	/**
	 * Constructor with input String
	 * @param ob the input string
	 * @param res where the result are stored
	 * @param symb the list of allowed symbols
	 */
	public SplitInSymbols(String ob,List<String> res,List<String> symb){
		this.obj=ob;
		this.result=res;
		this.Symbols = symb;
		}
	//
	
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}
	public List<String> getResult() {
		return result;
	}
	public void setResult(List<String> result) {
		this.result = result;
	}
	public List<String> getSymbols() {
		return Symbols;
	}
	public void setSymbols(List<String> symbols) {
		Symbols = symbols;
	}
	
	public void run() {
		if(obj.equals("eps")) { result.add(" ");	return; }
		
		int size=obj.length();
		int index=0;
		boolean founded=false;
		
		while(index<size){
			String temp=Character.toString(obj.charAt(index));
			int max=0,min=0;
			Pattern pattern = Pattern.compile(Pattern.quote(temp)+".*");
			for(String k : this.Symbols){
				Matcher matcher = pattern.matcher(k);
				while(matcher.find()){
					int sz=matcher.group().length();
					if(sz>max) max= sz;
					if(sz<min) min= sz;
				}
			}				
			while(max>min){		//entra solo se ne ha trovato almeno uno 
				if(index+max<obj.length()) temp=obj.substring(index,index+max);		//controlla la più lunga sequenza di caratteri
	 			else temp=obj.substring(index);
				if(Symbols.contains(temp)){
					founded=true;
					this.result.add(temp);
					index+=max;
					break;
				}
				max--;
			}
		}
		
		if(!founded) {
			logger.warning("La stringa di ingresso contiene caratteri non consentiti");
			this.result=null;
		}
	}
	
	public int count(){
		result= new LinkedList<String>();
		run();
		return this.result.size();
	}
}