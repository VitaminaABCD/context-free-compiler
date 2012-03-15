package com.Parse;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

public class DisplayingJTreeData {
  class CellRenderer extends JLabel implements TreeCellRenderer {
  CellRenderer(){}
	  public Component getTreeCellRendererComponent(JTree tree, Object value,
			  boolean selected, boolean expanded, boolean file, int row,boolean hasFocus){ 
		  StringBuffer buffer = new StringBuffer();
		  if (expanded)
		  buffer.append("File:");
		  if (file)
		  buffer.append("Subfile:");
		  if (hasFocus)
		  buffer.append("SubNode:");
		  buffer.append(row + "->");
		  buffer.append(value.toString());
		  setForeground(selected ? Color.RED : Color.RED);
		  setText(buffer.toString());
		  return this;
	  }
  }
}
  
  