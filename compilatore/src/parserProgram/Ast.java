package parserProgram;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class Ast {
	
	private List<HistoryElement> history;
	public DefaultMutableTreeNode getRoot() {
		return root;
	}

	private DefaultMutableTreeNode root;
	
	public Ast(){
		this.history=null;
		this.root=null;
	}
	
	public Ast(List<HistoryElement> h){
		this.history=h;
		this.root = null;
	}	
	
	public boolean initFromHistory(){
		int size = this.history.size();
		int index=size-1;
		//ROOT INIT//
		try{
			this.root = new DefaultMutableTreeNode(this.history.get(index).getProduction().getLeft());
			HistoryElement current = this.history.get(index);
			char temp[] = current.getProduction().getRight().toCharArray();
			for(char c : temp){													
				this.root.add(new DefaultMutableTreeNode(c));					
			}
			//END//
			if(size < 2) return true;  //TODO:
			DefaultMutableTreeNode currentLevel = root;
			index--;
			while(index>=0){															//scorre la cronologia del parsing
				current = this.history.get(index);										
				if(!current.isShift()){													//se � stata fatta una reduce
					for(int j=0; j<currentLevel.getChildCount();j++){					//cerca un figlio nel nodo puntato con UserObject(valore) uguale alla parte  sinistra della produzione
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentLevel.getChildAt(j);
						if(node.getUserObject().toString().equals(current.getProduction().getLeft())){		//se trova il nodo cercato
							temp = current.getProduction().getRight().toCharArray();
							for(char c : temp){													//aggiunge i figli al livello corrente
								node.add(new DefaultMutableTreeNode(c));					
							}
							currentLevel=node;													//avanza il current level e ferma il ciclo
							break;
						}
					}	
				}else{
					currentLevel=(DefaultMutableTreeNode) currentLevel.getParent();		//se � uno shift sale di un livello
				}
				index--;
			}
			
			return true;
		}catch (Exception e) {
			return false;
			
		}
	}	
	
	
	
	@Override
	public String toString(){	
		String result=root.getLastLeaf().toString();
		return result;
	}
}