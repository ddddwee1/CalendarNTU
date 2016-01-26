package com.potato.potato.calendarntu.FunctionCodes;

import java.util.ArrayList;


public class functions {
	
	//using this comparing structure brings more flexibility and more efficiency
	public boolean cprWeeks(Section a, Section b){
		if(a.weeks==null || b.weeks==null){
			return false;
		}
		if (a.weeks.size()==0 || b.weeks.size()==0){
			return false;
		}
		ArrayList<String> aWeeks = a.getWeeks();
		for(int i=0;i<aWeeks.size();i++){
			if(b.isInWeeks(aWeeks.get(i))){
				return false;
			}
		}
		return true;
	}
	
	//compare section return boolean
	public boolean cprScts(Section a,Section b){
		int astart = a.getStarttime();
		int bstart = b.getStarttime();
		int aend = a.getEndtime();
		int bend = b.getEndtime();
		if(aend>bstart && astart<bend){
			return cprWeeks(a, b);
			//return false;
		}else{
			return true;
		}
	}
	
	public boolean cprIndex(Index a,Index b){
		ArrayList<Section> asec = a.getSctlist();
		ArrayList<Section> bsec = b.getSctlist();
		for(int i=0;i<asec.size();i++){
			for(int j=0;j<bsec.size();j++){
				if(!cprScts(asec.get(i),bsec.get(j))){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean cprList2Index(ArrayList<Index> a,Index b){
		ArrayList<Index> alist = a;
		for(int i=0;i<alist.size();i++){
			if(!cprIndex(alist.get(i), b)){
				return false;
			}
		}
		return true;
	}
	
	public boolean cprCourse2Index(Course a,Index b){
		ArrayList<Index> alist = a.getList();
		for(int i=0;i<alist.size();i++){
			if(!cprIndex(alist.get(i), b)){
				return false;
			}
		}
		return true;
	}
}