package com.potato.potato.calendarntu.FunctionCodes;

import java.util.ArrayList;


public class TreeCpr {
	ArrayList<Course> baselist;
	int maxChild;
	functions func;
	int leaves;
	private ArrayList<Node> nodeList;
    ArrayList<Index> buff = new ArrayList<Index>();
	
	public TreeCpr(ArrayList<Course> courses, int max){
		this.baselist = courses;
		dynamicChangeMax();
		func = new functions();
		nodeList = new ArrayList<>();
		leaves = 0;
		GenerateTree();
		System.out.println("leaves: " + leaves);
	}

    public ArrayList<Node> getNodeList(){
        return nodeList;
    }

    public void changeTree(){
        changeTree(baselist);
    }
	public void changeTree(ArrayList<Course> courses){
		changeTree(courses, maxChild);
	}
	
	public void changeTree(ArrayList<Course> courses, int max){
        this.nodeList = new ArrayList<>();
		this.baselist = courses;
		//this.maxChild = max;
        dynamicChangeMax();
		leaves = 0;
		GenerateTree();
		System.out.println("leaves: "+leaves);
	}

	public void dynamicChangeMax(){
        if (baselist.size()<=4){
            maxChild = 100;
        }else if(baselist.size()<6){
            maxChild = 7;
        }else if (baselist.size()<8){
			maxChild = 4;
		}else if(baselist.size()<=9){
            maxChild = 3;
        }else{
            maxChild = 2;
        }
    }
	public void GenerateTree(){
		//a root node
		Node root = new Node(null);
        if(baselist.size()==0){
            return;
        }
		//first level
		for(int i=0;i<baselist.get(0).getList().size()&&root.childNum<maxChild;i++){
			Node newChild = new Node(root);
			//System.out.println("nodes in depth1: "+root.childNum)
			buff.add(baselist.get(0).getList().get(i));
			if(baselist.size()>1){
				GenerateTree(newChild,1);
				if(newChild.childNum==0){
					root.deleteChild(newChild);
				}
			}
            buff.remove(0);
		}
	}
	
	public void GenerateTree(Node parent, int level){
		//recurs function
		for(int i=0;i<baselist.get(level).getList().size()&&parent.childNum<maxChild;i++){
            //heavy memory occupation here
			//whether clash
			if(func.cprList2Index(buff, baselist.get(level).getList().get(i))){
                Node newChild = new Node(parent);
                buff.add(baselist.get(level).getList().get(i));
				if(level != baselist.size()-1){
					GenerateTree(newChild,level+1);
					if(newChild.childNum==0){
						parent.deleteChild(newChild);
					}
				}else{
                    newChild.setIndexes(buff);
					leaves++;
                    nodeList.add(newChild);
				}
                buff.remove(buff.size()-1);
			}
		}
	}
}

//try use single buffer arraylist to speed up