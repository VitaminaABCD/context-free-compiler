package contextFree.grammar;

import inputParser.SplitInSymbols;

import java.util.LinkedList;
import java.util.List;

public class Production {

		private String left,right;
        private List<String> leftSimbols;
        private List<String> rightSimbols;


        public Production(){
        	leftSimbols=null;
        	rightSimbols=null;
        }
        /**
         * Creates an object production and initialize it's rightSimbolsList.
         * passing between the rightSimbols and leftSimbols, check if is an empty string (epsilon)
         * @param leftSimbols part of production
         * @param rightSimbols part of production
         * @author Pierluigi Sottile
         */
        public Production(List<String> lt, List<String> rt){
                leftSimbols=lt;
                rightSimbols=rt;
        }
       
        /**
         * Create a Complete production representation.
         * Initialize two list with one element with entire symbol 
         * on left and right part of the production.
         * @param lt	Left part of the production
         * @param rt	Right part of the production
         */
        public Production(String lt, String rt){
        	left=lt;
        	right=rt;
        	leftSimbols= new LinkedList<String>();
        	rightSimbols = new LinkedList<String>();
        	leftSimbols.add(lt);
        	rightSimbols.add(rt);
         }
 
        /**
         * Create a Complete production representation.
         * Initialize two list with symbols of left and right part of the production.
         * @param lt	Left part of the production
         * @param rt	Right part of the production
         * @param V		The list of terminal
         * @param T
         * @throws InterruptedException
         * @author Paolo Pino
         */
        public Production(String lt, String rt,List<String> V,List<String> T) throws InterruptedException{
        	leftSimbols=new LinkedList<String>();
        	rightSimbols=new LinkedList<String>();
        	
        	List<String> symbols = new LinkedList<String>();
        	symbols.addAll(T);
        	symbols.addAll(V);
			Runnable rtObj = new SplitInSymbols(rt,this.rightSimbols,symbols);  //Splitta la stringa in lista di simboli in base
			Runnable ltObj = new SplitInSymbols(lt,this.leftSimbols,symbols);
			Thread threadR = new Thread(rtObj);
			Thread threadL = new Thread(ltObj);
			threadR.start();
			threadL.start();	
			threadL.join();													//attende il completamento
			threadR.join();
    }
        
        /**
         * 
         * @return theleftSimbolst side of production
         */
        public List<String> getLeftSimbols() {
                return leftSimbols;
        }

        public void setLeftSimbols(List<String>lt) {
                this.leftSimbols =lt;
        }

        public List<String> getRightSimbols() {
                return rightSimbols;
        }

        public void setRightSimbols(List<String>rt) {
                this.rightSimbols =rt;
        }

        public String getLeft(){
        	if(left==null){
            	this.left = this.leftSimbols.toString().replaceAll("[\\[\\],]", "");
        	}
        	return this.left;
        }
        
    	public String getRight() {
    		if(right==null){
            	this.right= this.rightSimbols.toString().replaceAll("[\\[\\],]", "");
        	}
    				return this.right;
    	}
        /**
         * return a formatted string in the form axioms :: = expression
         */
        @Override
        public String toString() {
                if(right.equals(" "))
                        return left+ "::=" + "eps";
                else{
                                return left+ "::=" +right;
                }
        }
}