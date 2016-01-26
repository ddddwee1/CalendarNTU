package com.potato.potato.calendarntu.FunctionCodes;

import java.util.ArrayList;


public class Node {
	Node parent;
	int childNum;
	ArrayList<Index> indexes;
	ArrayList<Node> children;
	
	public Node(Node parent){
		this.parent = parent;
		childNum = 0;
		children = new ArrayList<Node>();
		if(parent!=null){
			parent.addChild(this);
		}
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getChildNum() {
		return childNum;
	}
	
	public ArrayList<Index> getIndexes() {
		return indexes;
	}

	public void setIndexes(ArrayList<Index> indexes) {
		this.indexes = new ArrayList<>();
		for (int i=0;i<indexes.size();i++){
			this.indexes.add(indexes.get(i));
		}
	}

	public void deleteChild(Node child){
		children.remove(child);
		childNum--;
	}
	
	public void addChild(Node child){
		children.add(child);
		childNum++;
	}
	
}
