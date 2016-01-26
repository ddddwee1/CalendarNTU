package com.potato.potato.calendarntu.FunctionCodes;

import java.util.ArrayList;

public class Course {
	ArrayList<Index> list = new ArrayList<Index>();
	String coursecode;
	public ArrayList<Index> getList() {
		return list;
	}
	public void setList(ArrayList<Index> list) {
		this.list=list;
	}
	public String getCoursecode() {
		return coursecode;
	}
	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}
}
