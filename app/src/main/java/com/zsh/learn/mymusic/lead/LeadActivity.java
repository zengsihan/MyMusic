package com.zsh.learn.mymusic.lead;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsh.learn.mymusic.R;
import com.zsh.learn.mymusic.loading.LoadingActivity;
import com.zsh.learn.mymusic.util.SaveMsg;
import com.zsh.learn.mymusic.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面，用于第一次进入app使用
 * Created by zsh on 2017/4/19.
 */
public class LeadActivity extends AppCompatActivity{
    ViewPager vp;
    Button btn;
    TextView tv;
    LeadAdapter leadAdapter;
    List<View> list;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.fullScreen(this);
        if(SaveMsg.getIsFirstInApp(this)){//判断是否第一次进入APP
            setLayout();
            getView();
            setView();
        }else{//跳转到loading页面
            Intent i= new Intent(this, LoadingActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void setLayout() {
        setContentView(R.layout.activity_lead);//绑定xml布局
    }

    private void getView() {
        vp= (ViewPager) findViewById(R.id.lead_viewpager);
        btn= (Button) findViewById(R.id.lead_btn);
        tv= (TextView) findViewById(R.id.lead_tv);
    }

    private void setView() {
        list=new ArrayList<View>();
        layoutInflater =LayoutInflater.from(this);//获取到LayoutInflater的实例
        ImageView view1= (ImageView) layoutInflater.inflate(R.layout.lead_item,null);//要加载的布局
        view1.setImageResource(R.mipmap.lead_1);//图片资源
        ImageView view2= (ImageView) layoutInflater.inflate(R.layout.lead_item,null);
        view2.setImageResource(R.mipmap.lead_2);
        ImageView view3= (ImageView) layoutInflater.inflate(R.layout.lead_item,null);
        view3.setImageResource(R.mipmap.lead_3);
        list.add(view1);
        list.add(view2);
        list.add(view3);

        leadAdapter=new LeadAdapter(this,list);
        vp.setAdapter(leadAdapter);//给viewPager绑定适配器

        //btn的监听器
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMsg.setIsFirstInApp(LeadActivity.this,false);//保存共享参数信息，下次进入不走引导页面。
                Intent i=new Intent(LeadActivity.this,LoadingActivity.class);//跳转到loading页面
                startActivity(i);
                finish();
            }
        });

        //ViewPager的监听器
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //在屏幕滚动过程中不断被调用
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //手指抬起来就会立即执行这个方法，
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    btn.setVisibility(View.GONE);//隐藏btn
//                    tv.setVisibility(View.VISIBLE);
                    tv.setText("WHEN YOU HAPPY");
                }else if(position==1){
                    btn.setVisibility(View.GONE);//隐藏btn
//                    tv.setVisibility(View.VISIBLE);
                    tv.setText("WHEN YOU BLUE");
                }else if(position==2){
//                    tv.setVisibility(View.GONE);//隐藏tvR
                    btn.setVisibility(View.VISIBLE);//显示btn
                    tv.setText("TO FIND YOUR PERSONAL SONG");
                }
            }

            //在手指操作屏幕的时候发生变化。
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
