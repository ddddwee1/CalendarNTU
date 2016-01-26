package com.potato.potato.calendarntu.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by - on 2016/1/4.
 */
public class TimeTile {
    String[] times = {
            "8:30",
            "9:00",
            "9:30",
            "10:00",
            "10:30",
            "11:00",
            "11:30",
            "12:00",
            "12:30",
            "13:00",
            "13:30",
            "14:00",
            "14:30",
            "15:00",
            "15:30",
            "16:00",
            "16:30",
            "17:00",
            "17:30",
            "18:00",
            "18:30",
            "19:00",
            "19:30",
            "20:00"
    };
    Context context;
    int width,height,gap;
    public TimeTile(Context context,int width,int height,int gap){
        this.context = context;
        this.height = height;
        this.width = width;
        this.gap = gap;
    }
    public RelativeLayout getTimeline(){
        RelativeLayout rl = new RelativeLayout(context);
        rl.addView(getTextView("time",0,0));
        for(int i=0;i<times.length;i++){
            rl.addView(getTextView(times[i],0,(height+gap)*(i+1)));
        }
        rl.setBackgroundColor(Color.argb(200,245,245,245));
        return rl;
    }
    public TextView getTextView(String text, int left, int top){
        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width,height);
        lp.setMargins(left, top, 0, 0);
        tv.setLayoutParams(lp);
        tv.setText(text);
        return tv;
    }
    public int getMaxHeight(){
        return (times.length+1)*(gap+height);
    }
}
