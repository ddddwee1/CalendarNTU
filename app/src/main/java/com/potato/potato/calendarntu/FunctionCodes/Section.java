package com.potato.potato.calendarntu.FunctionCodes;

import java.util.ArrayList;


public class Section {
	int starttime;
	int endtime;
	int type;
	String place,strtype,strWeeks;
	ArrayList<String> weeks;
	public void setPlace(String place){
		this.place = place;
	}
	public String getPlace(){
		return place;
	}
	public int getStarttime() {
		return starttime;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setWeeks(ArrayList<String> weeks){
		this.weeks = weeks;
	}
	public ArrayList<String> getWeeks(){
		return weeks;
	}

	public String getStrWeeks() {
		return strWeeks;
	}

	public void setStrWeeks(String strWeeks) {
		this.strWeeks = strWeeks;
	}

	public void setStrtype(String strtype) {
		this.strtype = strtype;
	}

	public String getStrtype() {
		return strtype;
	}

	public boolean isInWeeks(String day){
		for(int i=0;i<weeks.size();i++){
			if(weeks.get(i).equals(day)){
				return true;
			}
		}
		return false;
	}
}
