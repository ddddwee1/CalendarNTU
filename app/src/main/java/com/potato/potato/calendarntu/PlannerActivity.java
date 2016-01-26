package com.potato.potato.calendarntu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.potato.potato.calendarntu.CustomViews.GenerateTimetable;

import java.util.Random;

public class PlannerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView lv;
    RelativeLayout rl;
    AppGlobal app;
    int width = 170;
    int height = 100;
    int gap = 8;
    int listNumber = 0;
    GenerateTimetable gt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lv = (ListView) findViewById(R.id.navdrawer);
        rl = (RelativeLayout)findViewById(R.id.plannerRL);
        app = (AppGlobal) getApplication();


        String[] options = new String[]{
                "My calendar",
                "Plan courses",
                "Choose courses",
                "Plan settings",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,options){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                if(position==1){
                    view.setBackgroundColor(Color.argb(90,0,0,0));
                }
                return view;
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                drawerLayout.closeDrawer(lv);
                switch (position) {
                    case 0:
                        intent.setClass(PlannerActivity.this, MyCalendarActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        intent.setClass(PlannerActivity.this, ChooseActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(PlannerActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });

        gt = new GenerateTimetable(PlannerActivity.this,width,height,gap,rl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(app.getTree().getNodeList().size()>0){
            gt.refreshView(app.getTree().getNodeList().get(0).getIndexes(),app.getCourses());
        }
    }


    //options menu
    //options menu
    MenuItem random,next,prev;

    //to-do find icons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        random = menu.add(0,1,1,"Change");
        random.setIcon(R.drawable.change);
        random.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        prev = menu.add(0,2,2,"prev");
        prev.setIcon(R.drawable.arrowleft);
        prev.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        next = menu.add(0,3,3,"next");
        next.setIcon(R.drawable.arrowright);
        next.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home){
            if (drawerLayout.isDrawerOpen(lv)){
                drawerLayout.closeDrawer(lv);
            }else {
                drawerLayout.openDrawer(lv);
            }
        }
        if (id==1&&app.getTree().getNodeList().size()!=0){
            int randNum = new Random().nextInt(app.getTree().getNodeList().size());
            gt.refreshView(app.getTree().getNodeList().get(randNum).getIndexes(),app.getCourses());
        }
        if (id==2){
            if (listNumber>0){
                listNumber--;
                gt.refreshView(app.getTree().getNodeList().get(listNumber).getIndexes(),app.getCourses());
                String text = "Choice number "+listNumber+" of total "+app.getTree().getNodeList().size();
                Toast ts = Toast.makeText(PlannerActivity.this,text,Toast.LENGTH_SHORT);
            }
        }if (id==3){
            if (listNumber<app.getCourses().size()-1){
                listNumber++;
                gt.refreshView(app.getTree().getNodeList().get(listNumber).getIndexes(),app.getCourses());
                String text = "Choice number "+listNumber+" of total "+app.getTree().getNodeList().size();
                Toast ts = Toast.makeText(PlannerActivity.this,text,Toast.LENGTH_SHORT);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
