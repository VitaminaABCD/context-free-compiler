package contextFree.grammar;

import java.util.ArrayList;
import java.util.List;

public class Production {

        private String left;
        private String right;
        private List <String> rightList;


        public Production(){
                left="";
                right="";
        }
        /**
         *creates an object Prodaction complete RightList 
         *passing between the right and left, check if eps
         * @param left part of prodaction
         * @param right part of prodaction
         * @author Pierluigi Sottile
         */
        public Production(String lt, String rt){
                left=lt;
                right=rt;
                rightList = new ArrayList<String>();
                if (!right.equals("eps")){
                	char[] temp = right.toCharArray();
                	for(int i=0;i<temp.length;i++)
                        this.rightList.add(Character.toString(temp[i]));
                }
                else 
                	this.rightList.add(" ");
        }
        
        public Production(String lt, String rt,boolean flag){
                left=lt;
                right=rt;
                rightList = new ArrayList<String>();
        }
        /**
         * 
         * @return the left side of production
         */
        public String getLeft() {
                return left;
        }

        public void setLeft(String left) {
                this.left = left;
        }

        public String getRight() {
                return right;
        }

        public void setRight(String right) {
                this.right = right;
        }
        
        public List<String> getRightList() {
                return rightList;
        }

        public void setRightList(List<String> rightList) {
                this.rightList = rightList;
        }
        /**
         * return a formatted string in the form ass :: = expr
         */
        @Override
        public String toString() {
                if (right.equals(" "))
                        return left + "::=" + "eps";
                else{
                                return left + "::=" + right ;
                }
        }
}