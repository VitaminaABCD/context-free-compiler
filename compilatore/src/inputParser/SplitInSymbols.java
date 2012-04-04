package inputParser;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Split the input string in symbols list
 * @author Paolo Pino
 *
 */
public class SplitInSymbols implements Runnable {
	static Logger logger = Logger.getLogger(SplitInSymbols.class.getName());
	
	private String obj;
	private List<String> result,symbols;
	//Constructor
	public SplitInSymbols(){this.obj=null;this.result=this.symbols=null;}
	public SplitInSymbols(String in){this.obj=in;}	
	public SplitInSymbols(String in,List<String> symb){this.obj=in;this.symbols=symb;}
	/**
	 * Constructor with input String
	 * @param ob the input string
	 * @param res where the result are stored
	 * @param symb the list of allowed symbols
	 */
	public SplitInSymbols(String ob,List<String> res,List<String> symb){
		this.obj=ob;
		this.result=res;
		this.symbols = symb;
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
		return symbols;
	}
	public void setSymbols(List<String> symb) {
		this.symbols = symb;
	}
	
	public void addSymbols(List<String> symb) {
		if(this.symbols==null) this.symbols = new LinkedList<String>();
		this.symbols.addAll(symb);
	}
	
	public void run() {
		if(obj.equals("eps")) { result.add(" ");	return; }
		
		int size=obj.length();
		int index=0;
		boolean founded;
		
		while(index<size){
			founded=false;
			String temp=Character.toString(obj.charAt(index));
			int max=0,min=0;
			Pattern pattern = Pattern.compile(Pattern.quote(temp)+".*");
			for(String k : this.symbols){
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
				if(symbols.contains(temp)){
					this.result.add(temp);
					index+=max;
					founded=true;
					break;
				}
				max--;
			}
			
			if(!founded) {
				logger.info("La grammatica corrente non contiene '" + temp + "' tra i caratteri ammessi");
				this.result=null;
				return;
			}
		}
		

	}
	
	public int count(){
		result= new LinkedList<String>();
		run();
		return this.result.size();
	}
}