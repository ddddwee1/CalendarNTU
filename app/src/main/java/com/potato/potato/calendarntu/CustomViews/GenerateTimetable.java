package com.potato.potato.calendarntu.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.potato.potato.calendarntu.CustomViews.PlannerTableOneDay;
import com.potato.potato.calendarntu.CustomViews.TimeTile;
import com.potato.potato.calendarntu.FunctionCodes.Course;
import com.potato.potato.calendarntu.FunctionCodes.Index;
import com.potato.potato.calendarntu.FunctionCodes.Section;

import java.util.ArrayList;

public class GenerateTimetable {
    Context context;
    int width,height,gap;
    RelativeLayout rl,timetile;
    ArrayList<PlannerTableOneDay> ptList;
    //0 is timetile, 1 to 5 is days
    ArrayList<RelativeLayout> daysList;
    public GenerateTimetable(Context context,int width,int height, int gap,RelativeLayout rl){
        this.context = context;
        this.width = width;
        this.height = height;
        this.gap = gap;
        this.rl = rl;
        daysList = new ArrayList<>();
        ptList = new ArrayList<>();
        mainfunc();
    }

    private void postProcess(){
        for(int i=0;i<ptList.size();i++){
            ptList.get(i).postProcess();
        }
    }

    public void mainfunc(){
        daysList = new ArrayList<>();
        ptList = new ArrayList<>();
        TimeTile tt = new TimeTile(context,width-15,height,gap);
        timetile = tt.getTimeline();
        rl.addView(timetile);
        daysList.add(timetile);
        timetile.setId(ViewGroup.generateViewId());

        for(int i=0;i<5;i++){
            PlannerTableOneDay pt = new PlannerTableOneDay(context,width,height,gap,i);
            RelativeLayout ptrl = pt.getRl();
            ptrl.setId(ViewGroup.generateViewId());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, tt.getMaxHeight());
            lp.addRule(RelativeLayout.RIGHT_OF,daysList.get(i).getId());
            lp.setMargins(gap / 2, 0, 0, 0);
            ptrl.setLayoutParams(lp);
            ptrl.setBackgroundColor(Color.argb(200, 243, 243, 243));
            rl.addView(ptrl);
            daysList.add(ptrl);
            ptList.add(pt);
        }
        postProcess();
    }

    public void refreshView(ArrayList<Index> indexList,ArrayList<Course> courses){
        System.out.println(indexList.size());
        rl.removeAllViews();
        mainfunc();
        for (int i=0;i<indexList.size();i++){
            ArrayList<Section> secList = indexList.get(i).getSctlist();
            System.out.println("index:" + indexList.get(i).getIndex());
            for(int j=0;j<secList.size();j++){
                int start = secList.get(j).getStarttime();
                int step = secList.get(j).getEndtime()-start;
                int day = start/100;
                start = start%100;
                String type = secList.get(j).getStrtype();
                String weeks ="";
                if(secList.get(j).getStrWeeks()!=null) {
                    weeks += secList.get(j).getStrWeeks();
                }
                //System.out.println("Start:"+start+"Endtime:"+secList.get(j).getEndtime()+"Step:"+step);
                ptList.get(day).addSection(start,step,(courses.get(i).getCoursecode()+"\n"+type+"\n"+weeks+"\n"+indexList.get(i).getIndex()),i);
                ArrayList<String> wklst = secList.get(j).getWeeks();
                /*for (int p=0;p<wklst.size();p++){
                    System.out.println(j+"Week: "+wklst.get(p));
                }*/
            }
        }
        postProcess();
    }

}
