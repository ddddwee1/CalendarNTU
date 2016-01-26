package com.potato.potato.calendarntu;

import android.app.Application;

import com.potato.potato.calendarntu.FunctionCodes.Course;
import com.potato.potato.calendarntu.FunctionCodes.Index;
import com.potato.potato.calendarntu.FunctionCodes.Section;
import com.potato.potato.calendarntu.FunctionCodes.TreeCpr;
import com.potato.potato.calendarntu.FunctionCodes.functions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AppGlobal extends Application {
    private ArrayList<Course> courses = new ArrayList<>(),courses2 = new ArrayList<>();
    private TreeCpr tree;
    private ArrayList<int[]> banTime = new ArrayList<>();

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void newTree(){
        tree = new TreeCpr(courses2,5);
    }

    //Better new the course list here
    @Override
    public void onCreate() {
        super.onCreate();
        newTree();
    }

    //regenerate the course list
    public void newCourseList(){
        courses = new ArrayList<>();
        courses2 = new ArrayList<>();
    }

    //Return a tree
    public TreeCpr getTree(){
        return tree;
    }

    //not encouraged, but also can be used
    @Deprecated
    public void setCourses(ArrayList<Course> courses){
        this.courses = courses;
        refreshCs2();
    }

    //add and delete course
    //add and delete course
    public void addCourse(Course course){
        courses.add(course);
        courses2.add(course);
        long start = System.currentTimeMillis();
        //tree.changeTree();
        System.out.println("Time: " + (System.currentTimeMillis() - start));
    }

    public void deleteCourse(String code){
        for(int i=0;i<courses.size();i++){
            if(courses.get(i).getCoursecode().toUpperCase().equals(code.toUpperCase())){
                courses.remove(i);
                courses2.remove(i);
                i--;
            }
        }
        //tree.changeTree();
    }
    public void reGenerate(){
        tree = new TreeCpr(courses2,5);
    }
    public void refreshCs2(){
        courses2 = new ArrayList<>();
        for (int i=0;i<courses.size();i++){
            ArrayList<Index> indexList = courses.get(i).getList();
            ArrayList<Index> buffList = new ArrayList<>();
            Course cs = new Course();
            cs.setCoursecode(courses.get(i).getCoursecode());
            for (int j=0;j<indexList.size();j++){
                buffList.add(indexList.get(j));
            }
            cs.setList(buffList);
            courses2.add(cs);
        }
    }

    public void deleteIndex(String index){
        //give a index and delete it
        for (int i=0;i<courses2.size();i++){
            ArrayList<Index> indexList = courses2.get(i).getList();
            for (int j=0;j<indexList.size();j++){
                if(indexList.get(j).getIndex().equals(index)){
                    indexList.remove(j);
                }
            }
            System.out.println("Deleted size" + indexList.size());
        }
    }

    public void setBanTime(ArrayList<int[]> banTime){
        this.banTime = banTime;
    }

    public boolean isInList(String title){
        for(int i=0;i<courses.size();i++){
            if(courses.get(i).getCoursecode().toUpperCase().equals(title.toUpperCase())){
                return true;
            }
        }
        return false;
    }

    public boolean isInList2(String index){
        for (int i=0;i<courses2.size();i++){
            ArrayList<Index> indList = courses2.get(i).getList();
            for (int j=0;j<indList.size();j++){
                if (indList.get(j).getIndex().equals(index)){
                    return true;
                }
            }
        }
        return false;
    }
    private void implementBanTime(){
        for(int i=0;i<banTime.size();i++){
            int start = banTime.get(i)[0];
            int end = banTime.get(i)[1];
            Section sc = new Section();
            sc.setStarttime(start);
            sc.setEndtime(end);
            ArrayList<Section> scL = new ArrayList<>();
            scL.add(sc);
            Index ind = new Index();
            ind.setSctlist(scL);
            for(int j=0;j<courses2.size();j++){
                functions functions = new functions();
                ArrayList<Index> indList = courses2.get(j).getList();
                for (int k=0;k<indList.size();k++){
                    if(functions.cprIndex(ind,indList.get(k))){
                        indList.remove(k);
                        k--;
                    }
                }
            }
        }
    }
}
//get another array to store the banned result