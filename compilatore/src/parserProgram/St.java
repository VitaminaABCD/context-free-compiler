package parserProgram;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Abstract Syntax Three class.
 * @author Paolo Pino
 */
public class St {
	/**the chronology of parsing*/
	private List<HistoryElement> history;
	/**the root of the three*/
	private DefaultMutableTreeNode root;
	
	private List<String> V,T;
	/**
	 * Default constructor.
	 */
	public St(){
		this.history=null;
		this.root=null;
	}
	
	/**
	 * Construct the object with specified history.
	 * @param h history 
	 */
	public St(List<HistoryElement> h){
		this.history=h;
		this.root = null;
	}	

	/**
	 * 
	 * @return the root of the three.
	 */
	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Initialize the three from the parsing history.
	 * @throws Exception if error.
	 */
	public void initFromHistory() throws Exception{
		int size = this.history.size();
		int index=size-1;
		//ROOT INIT//
		try{
			this.root = new DefaultMutableTreeNode(this.history.get(index).getProduction().getLeft());
			HistoryElement current = this.history.get(index);
			for(String c : current.getProduction().getRightSimbols()){													
				this.root.add(new DefaultMutableTreeNode(c));					
			}
			//END//
			if(size < 2) return;  //TODO:
			DefaultMutableTreeNode currentLevel = root;
			index--;
			while(index>=0){															//scorre la cronologia del parsing
				current = this.history.get(index);										
				if(!current.isShift()){													//se è stata fatta una reduce
					for(int j=0; j<currentLevel.getChildCount();j++){					//cerca un figlio nel nodo puntato con UserObject(valore) uguale alla parte  sinistra della produzione
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentLevel.getChildAt(j);
						if(node.getUserObject().toString().equals(current.getProduction().getLeft())){		//se trova il nodo cercato
							for(String c : current.getProduction().getRightSimbols()){													
								node.add(new DefaultMutableTreeNode(c));					
							}
							currentLevel=node;													//avanza il current level e ferma il ciclo
							break;
						}
					}	
				}else{
					currentLevel=(DefaultMutableTreeNode) currentLevel.getParent();		//se è uno shift sale di un livello
				}
				index--;
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
	}	
	
	
	
	@Override
	public String toString(){	
		String result=root.getLastLeaf().toString();
		return result;
	}
}