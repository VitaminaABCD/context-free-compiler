package inputParser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Split the input string in symbols list
 * @author Paolo Pino
 *
 */
public class SplitInSymbols implements Runnable {
	private String obj;
	private List<String> result,Symbols;
	//Constructor
	public SplitInSymbols(){this.obj=null;this.result=this.Symbols=null;}
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
		if(obj.equals("eps")) {
			result.add(" ");
			return;
		}
		
		int size=obj.length();
		int index=0;
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
				
			boolean founded=false;
			while(max>min){
				if(index+max<obj.length()) temp=obj.substring(index,index+max);
	 			else temp=obj.substring(index);
				if(Symbols.contains(temp)){
					founded=true;
					break;
				}
				max--;
			}
			if(founded) {
				this.result.add(temp);
				index+=max;
			}
		}
	}
}