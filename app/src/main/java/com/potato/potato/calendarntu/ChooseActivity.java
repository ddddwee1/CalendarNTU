package com.potato.potato.calendarntu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.potato.potato.calendarntu.FunctionCodes.Course;
import com.potato.potato.calendarntu.FunctionCodes.GetVacancy;
import com.potato.potato.calendarntu.FunctionCodes.Index;
import com.potato.potato.calendarntu.FunctionCodes.Stringdownload;
import com.potato.potato.calendarntu.FunctionCodes.contenthandler;
import com.potato.potato.calendarntu.TreeViewHolder.HeaderHolderSelectable;
import com.potato.potato.calendarntu.TreeViewHolder.IconHolderSelectable;
import com.potato.potato.calendarntu.TreeViewHolder.TreeItem;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.xml.parsers.SAXParserFactory;

//Write at first:
//I like to write codes into modules, even in one class, I put the relative functions nearby
//I dont know whether it is a good choice or bad one
//This is an open source program, so if you want to modify it, just

public class ChooseActivity extends AppCompatActivity {

    EditText edit;
    Button btn;
    ScrollView sv;
    AndroidTreeView tView;
    View tt;
    TreeNode root;
    Thread t;
    ArrayList<String> coursecodes;
    AppGlobal app;
    Course cs;
    ProgressDialog pd;

    // I inverse the boolean for the checkboxes
    String[] progressHint = {
            "Communicating with server...",
            "Stealing documents from office...",
            "Let me think...",
            "To be honest, I don't wanna take this course.",
            "Connection so lag!",
            "Shaking your phone makes it faster",
            "Just leave me alone..."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Planner");

        edit = (EditText) findViewById(R.id.courseText);
        btn = (Button) findViewById(R.id.addButton);
        sv = (ScrollView) findViewById(R.id.chooseScroll);
        t = new Thread(r);
        coursecodes = new ArrayList<>();
        app = (AppGlobal)getApplication();

        edit.setBackgroundResource(R.drawable.inputbg);
        btn.setBackgroundResource(R.drawable.btnbg);

        newTreeView();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive() && getCurrentFocus() != null) {
                    if (getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                if (!t.isAlive()) {
                    if (!app.isInList(edit.getText().toString())) {
                        t = new Thread(r);
                        coursecodes.add(edit.getText().toString());
                        t.start();
                        pd = ProgressDialog.show(ChooseActivity.this, "Loading...", progressHint[new Random().nextInt(progressHint.length)], true);
                    } else {
                        Toast ts = Toast.makeText(ChooseActivity.this, "Already chosen!", Toast.LENGTH_SHORT);
                        ts.show();
                    }
                }
            }
        });

        initPage();
    }

    private void newTreeView(){
        root = TreeNode.root();
        tView = new AndroidTreeView(ChooseActivity.this,root);
        tView.setDefaultAnimation(true);

        tt = tView.getView();
        sv.removeAllViews();
        sv.addView(tt);
    }

    private void initPage(){
        ArrayList<Course> list = app.getCourses();
        System.out.println("Listsize:"+list.size());
        for(int i=0;i<list.size();i++){
            final TreeNode course = new TreeNode(new TreeItem(R.string.ic_drive_ms_excel,list.get(i).getCoursecode().toUpperCase()));
            course.setSelected(true);
            course.setViewHolder(new HeaderHolderSelectable(ChooseActivity.this));
            ArrayList<Index> indlist = list.get(i).getList();
            course.setLongClickListener(new TreeNode.TreeNodeLongClickListener() {
                @Override
                public boolean onLongClick(TreeNode treeNode, Object o) {
                    Message msg = new Message();
                    msg.obj = course;
                    msg.setTarget(deleteNode);
                    msg.sendToTarget();
                    return false;
                }
            });
            for (int j=0;j<indlist.size();j++) {
                TreeNode index = new TreeNode(indlist.get(j).getIndex());
                if (app.isInList2(indlist.get(j).getIndex())) {
                    index.setSelected(false);
                } else {
                    index.setSelected(true);
                }
                index.setViewHolder(new IconHolderSelectable(ChooseActivity.this));
                tView.addNode(course, index);
            }
            tView.addNode(root, course);
        }
        tView.setSelectionModeEnabled(true);
    }

    //adding or deleting course
    //adding or deleting course

    Runnable r = new Runnable() {
        @Override
        public void run() {
            Stringdownload sd = new Stringdownload();
            if(coursecodes.size()!=0){
                String code = coursecodes.get(0);
                try {
                    System.out.println("thread started");
                    String result = sd.getstring(code);
                    SAXParserFactory pf = SAXParserFactory.newInstance();
                    XMLReader rd = pf.newSAXParser().getXMLReader();
                    contenthandler ctthandler = new contenthandler();
                    rd.setContentHandler(ctthandler);
                    rd.parse(new InputSource(new StringReader(result)));
                    cs = new Course();
                    cs.setList(ctthandler.getList());
                    cs.setCoursecode(code);

                    if(!app.isInList(cs.getCoursecode())) {
                        app.addCourse(cs);
                        System.out.println("Get!");
                        Message msg = new Message();
                        msg.setTarget(addTreeNode);
                        msg.sendToTarget();
                    }else {
                        coursecodes.remove(0);
                        Message dismiss = new Message();
                        dismiss.setTarget(dismisspd);
                        dismiss.sendToTarget();
                    }
                }catch (Exception e){
                    System.out.println(e);
                    Message msg = new Message();
                    msg.setTarget(loadingFail);
                    msg.sendToTarget();
                }
            }
        }
    };

    Handler dismisspd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pd.dismiss();
        }
    };
    Handler addTreeNode = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            final TreeNode course = new TreeNode(new TreeItem(R.string.ic_drive_ms_excel,coursecodes.get(0).toUpperCase()));
            coursecodes.remove(0);
            course.setLongClickListener(new TreeNode.TreeNodeLongClickListener() {
                @Override
                public boolean onLongClick(TreeNode treeNode, Object o) {
                    Message msg = new Message();
                    msg.obj = course;
                    msg.setTarget(deleteNode);
                    msg.sendToTarget();
                    return false;
                }
            });
            course.setSelected(true);
            course.setViewHolder(new HeaderHolderSelectable(ChooseActivity.this));
            for(int i=0;i<cs.getList().size();i++){
                TreeNode index = new TreeNode(cs.getList().get(i).getIndex());
                index.setSelected(false);
                index.setViewHolder(new IconHolderSelectable(ChooseActivity.this));
                course.addChild(index);
            }
            tView.addNode(root, course);
            pd.dismiss();
        }
    };

    Handler deleteNode = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            TreeNode tn = (TreeNode)msg.obj;
            String course = ((TreeItem)tn.getValue()).text;
            app.deleteCourse(course);
            tView.removeNode(tn);
        }
    };

    Handler loadingFail = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pd.dismiss();
            AlertDialog.Builder albd = new AlertDialog.Builder(ChooseActivity.this);
            albd.setMessage(":( Loading failed, retry pls");
            albd.setTitle("Failed");
            albd.show();
        }
    };


    //options menu

    MenuItem empty,vacancyBtn;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        vacancyBtn = menu.add(0,1,0,"change");
        vacancyBtn.setIcon(R.drawable.vacancyicon);
        vacancyBtn.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        empty = menu.add(0,2,1,"empty");
        empty.setIcon(R.drawable.empty);
        empty.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            endProcess();
        }
        if(item.getItemId()==1){
            getVacancy();
        }
        if (item.getItemId()==2){
            app.newCourseList();
            newTreeView();
            initPage();
        }
        return super.onOptionsItemSelected(item);
    }


    //end saving process


    private void SaveSelection(){
        List<TreeNode> selected = tView.getSelected();
        app.refreshCs2();
        System.out.println("Size" + selected.size());
        for (int i=0;i<selected.size();i++){
            Object value = selected.get(i).getValue();
            if (value instanceof String){
                app.deleteIndex((String)value);
            }
        }
        Toast ts = Toast.makeText(ChooseActivity.this, "Choice saved!", Toast.LENGTH_SHORT);
        ts.show();
        app.reGenerate();
    }

    Runnable rEnd = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.setTarget(finishHandler);
            msg.sendToTarget();
        }
    };
    Thread finishActivity = new Thread(rEnd);
    ProgressDialog pdEnd;
    long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }else {
                //put the progress dialog here, new thread and handler to finish the activity
                endProcess();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void endProcess(){
        if(!finishActivity.isAlive()) {
            pdEnd = ProgressDialog.show(ChooseActivity.this, "Saving", "Saving your selection...", true);
            finishActivity.start();
        }
    }


    Handler finishHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            SaveSelection();
            pdEnd.dismiss();
            finish();
        }
    };


    //The vacancy part
    //The vacancy part
    //The vacancy part

    Runnable rVacancy = new Runnable() {
        @Override
        public void run() {
            GetVacancy gv = new GetVacancy();
            for (int i=0;i<app.getCourses().size();i++){
                try {
                    Message msg = new Message();
                    msg.setTarget(hVacancy);
                    msg.arg1 = i;
                    msg.sendToTarget();
                    ArrayList<String> bufList = gv.main(app.getCourses().get(i).getCoursecode());
                    System.out.println("buflist:"+bufList.size());
                    for (int j=0;j<bufList.size();j++){
                        System.out.println(bufList.get(j));
                        app.deleteIndex(bufList.get(j));
                    }
                }catch (Exception e){
                    continue;
                }
            }
            Message msg2 = new Message();
            msg2.setTarget(hVancancyEnd);
            msg2.sendToTarget();
        }
    };

    ProgressDialog pdVacancy;
    Thread tVacancy = new Thread(rVacancy);
    public void getVacancy(){
        pdVacancy = ProgressDialog.show(ChooseActivity.this,"Loading","Reaching vacancy list...",true);
        tVacancy.start();
    }

    Handler hVacancy = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int index = msg.arg1;
            pdVacancy.setMessage("Reaching vacancy list of "+app.getCourses().get(index).getCoursecode()+"...");
        }
    };

    Handler hVancancyEnd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pdVacancy.dismiss();
            List<TreeNode> childs = root.getChildren();
            for (int i=0;i<childs.size();i++){
                tView.removeNode(childs.get(i));
            }
            newTreeView();
            initPage();
            AlertDialog.Builder ab = new AlertDialog.Builder(ChooseActivity.this);
            Date dt = new Date();
            SimpleDateFormat df = new SimpleDateFormat("HH");
            //System.out.println(df.format(dt));
            int time = Integer.valueOf(df.format(dt));
            //System.out.println(time);
            if(time>=22||time<9){
                ab.setMessage("Check vacancies is only avaliable from 9am to 10pm daily!");
            }else {
                ab.setMessage("Finished!");
            }
            ab.setTitle("Get vacancy");
            ab.show();
            tVacancy = new Thread(rVacancy);
        }
    };
}
