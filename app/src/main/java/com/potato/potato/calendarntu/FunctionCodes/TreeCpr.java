package com.potato.potato.calendarntu.FunctionCodes;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class TreeCpr {
	ArrayList<Course> baselist;
	int maxChild;
	functions func;
	int leaves;
	private ArrayList<Node> nodeList;
    ArrayList<Index> buff = new ArrayList<Index>();
    boolean oneDay=false, concentrate=false;
	
	public TreeCpr(ArrayList<Course> courses, int max,boolean oneDay, boolean concentrate){
		this.baselist = courses;
        this.oneDay = oneDay;
        this.concentrate = concentrate;
        //dynamicChangeMax is a modification of maximum number.
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
				GenerateTree(newChild, 1);
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

    //additional function

    public void setOneDay(boolean oneDay) {
        this.oneDay = oneDay;
    }

    public void setConcentrate(boolean concentrate) {
        this.concentrate = concentrate;
    }

    public void applySetting(){
        if (oneDay){
            applyOneDaySetting();
        }if (concentrate){
            applyConcentrateSetting();
        }
    }

    boolean[] isDayOccupied = new boolean[5];
    private void applyOneDaySetting(){
        for (int i=0;i<5;i++){
            isDayOccupied[i] = false;
        }
        ArrayList<Node> bufferNodeList = new ArrayList<>();
        for (int i=0;i<nodeList.size();i++){
            ArrayList<Index> ind = nodeList.get(i).getIndexes();
            for (int j=0;j<ind.size();j++) {
                ArrayList<Section> sctlist = ind.get(j).getSctlist();
                for (int k = 0; k < sctlist.size(); k++) {
                    isDayOccupied[sctlist.get(k).getStarttime() % 100] = true;
                }
            }
            int num =0;
            for (int j=0;j<5;j++){
                if (isDayOccupied[i]){
                    num++;
                }
            }
            if (num<5){
                bufferNodeList.add(nodeList.get(i));
            }
        }
        nodeList = bufferNodeList;
    }

    private void applyConcentrateSetting(){
        ArrayList<Node> bufferNodeList = new ArrayList<>();
        bufferNodeList.add(nodeList.get(0));
        int max=-99;

        for (int i=0;i<nodeList.size();i++){
            int value = getConcentrateValue(nodeList.get(i));
            if (value>max){
                bufferNodeList.add(0,nodeList.get(i));
            }else{
                bufferNodeList.add(nodeList.get(i));
            }
        }

        nodeList = bufferNodeList;
    }

    //Modify memory usage, using global variables
    int[] start = new int[5];
    int[] end = new int[5];
    int[] time = new int[5];
    private int getConcentrateValue(Node node){
        for (int i=0;i<5;i++){
            start[i] = 99;
        }
        ArrayList<Index> indexBuffer = node.getIndexes();
        for (int i=0;i<indexBuffer.size();i++){
            ArrayList<Section> secBuffer = indexBuffer.get(i).getSctlist();
            for (int j=0;j<secBuffer.size();j++){
                Section sec = secBuffer.get(j);
                if (start[sec.getStarttime()/100]>sec.getStarttime()%100){
                    start[sec.getStarttime()/100] = sec.getStarttime()%100;
                }
                if (end[sec.getEndtime()/100]<sec.getEndtime()%100){
                    end[sec.getEndtime()/100] = sec.getEndtime()%100;
                }
                time[sec.getStarttime()/100] += (sec.getEndtime()-sec.getStarttime());
            }
        }

        int value=0;
        for (int i=0;i<5;i++){
            if (start[i]<end[i]){
                value -= (end[i]-start[i]-time[i]);
            }
        }

        return value;
    }
}
