package com.potato.potato.calendarntu.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.potato.potato.calendarntu.R;

import java.util.ArrayList;

public class PlannerTableOneDay {
    Context context;
    int width, height,gap,day,maxline = 24;
    RelativeLayout rl;
    ArrayList<Integer> topList,btmList,columList,leftList,stepList;
    ArrayList<TextView> tvList,tvList2;
    //ArrayList<ArrayList<Integer>> relations;
    int column,maxWidth;
    int[] resources = {R.drawable.coursebg,R.drawable.coursebg2,R.drawable.coursebg3,R.drawable.coursebg4,
            R.drawable.coursebg5,R.drawable.coursebg6,R.drawable.coursebg7,R.drawable.coursebg8,R.drawable.coursebg9,R.drawable.coursebg10};

    public PlannerTableOneDay(Context context, int width, int height, int gap, int day){
        this.context = context;
        this.width = width;
        this.height = height;
        this.gap = gap;
        this.day = day;
        rl = getOneday();
        topList = new ArrayList<Integer>();
        btmList = new ArrayList<Integer>();
        tvList = new ArrayList<TextView>();
        columList = new ArrayList<Integer>();
        tvList2 = new ArrayList<TextView>();
        leftList = new ArrayList<Integer>();
        stepList = new ArrayList<Integer>();
        column = 0;
        maxWidth = width;
    }

    private TextView getTitle(String text){
        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width,height);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        tv.setLayoutParams(lp);
        tv.setText(text);
        tv.setBackgroundColor(Color.argb(0, 255, 255, 255));
        return tv;
    }

    private TextView getSection(String text,int top, int left,int step,int diff){
        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((maxWidth-diff*gap/2)/(diff+1),height*step+gap*step-gap);
        lp.setMargins(left, top, 0, 0);
        tv.setLayoutParams(lp);
        return tv;
    }

    private TextView getDiv(int left, int top){
        TextView tv = new TextView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(maxWidth+gap,gap/2);
        lp.setMargins(left, top, 0, 0);
        tv.setLayoutParams(lp);
        tv.setBackgroundColor(Color.argb(255, 255, 255, 255));
        return tv;
    }

    private RelativeLayout getOneday(){
        RelativeLayout rl = new RelativeLayout(context);
        switch (day){
            case 0:
                rl.addView(getTitle("MON"));
                break;
            case 1:
                rl.addView(getTitle("TUE"));
                break;
            case 2:
                rl.addView(getTitle("WED"));
                break;
            case 3:
                rl.addView(getTitle("THU"));
                break;
            case 4:
                rl.addView(getTitle("FRI"));
                break;
        }

        return rl;
    }

    public void postProcess(){
        for(int i=0;i<maxline;i++){
            rl.addView(getDiv(0,(i+1)*(height+gap)-gap*2/3),0);

        }
    }

    public void addSection(int startNumber, int step,String text,int color){
        int top = (startNumber+1)*(height+gap);
        int btn = top+height*step+gap*step-gap;
        int left = gap;
        ArrayList<Integer> bufflist = new ArrayList<>();
        int diff = 0;
        for(int i=0;i<topList.size();i++){
            int tvtop = topList.get(i);
            int tvbtm = btmList.get(i);
            //System.out.println("top:"+top+"btn"+btn+"tvtop"+tvtop+"tvbtn"+tvbtm);
            if(top<tvbtm && btn>tvtop){
                left += width+gap/2;
                bufflist.add(i);
                diff ++;
                //System.out.println("detected");
            }
        }
        for(int a=0;a<bufflist.size();a++){
            int i=bufflist.get(a);
            int buffer = columList.get(i);
            if(diff>buffer) {
                buffer++;
                columList.remove(i);
                columList.add(i, buffer);
            }
        }
        if(diff>column){
            column = diff;
        }
        maxWidth = width+column*(width+gap/2);
        for(int i=0;i<tvList.size();i++){
            int clm = columList.get(i);
            int wid = (maxWidth-clm*gap/2)/(clm+1);
            TextView tvv = tvList.get(i);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(wid,height*stepList.get(i)+gap*stepList.get(i)-gap);
            lp.setMargins(leftList.get(i), topList.get(i), 0, 0);
            tvv.setLayoutParams(lp);
        }

        TextView tv = getSection(text,top,left,step,diff);
        tv.setTextSize(10);
        tv.setBackgroundResource(resources[color]);
        rl.addView(tv);
        topList.add(top);
        btmList.add(btn);
        tvList.add(tv);
        leftList.add(left);
        columList.add(diff);
        stepList.add(step);
    }

    public RelativeLayout getRl(){
        return rl;
    }

}
