package com.zsh.learn.mymusic.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zsh.learn.mymusic.util.Tools;

/**
 * Created by zsh on 2017/4/19.
 */

public abstract class BaseActivity extends AppCompatActivity{
    private String TAG="BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.fullScreen(this);//强制满屏
        setLayout();
        getData();
        getView();
        setView();
    }
    public abstract void setLayout();//加载布局
    public abstract void getData();//加载数据
    public abstract void getView();//得到控件
    public abstract void setView();//对控件的操作

    /**
     * 启动跳转
     * @param c 目标Activity
     */
    public void startActivity(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
        finish();
    }

    /**
     * 启动跳转
     * @param c 目标Activity
     * @param inId  页面载入动画
     * @param outId 页面退出动画
     */
    public void startActivity(Class c, int inId, int outId){
        Intent intent=new Intent(this,c);
        startActivity(intent);
        overridePendingTransition(inId,outId);
        finish();
    }

    /**
     * 带参数传递的跳转方法
     * @param c 目标Activity
     * @param inId  页面载入动画
     * @param outId 页面退出动画
     * @param bundle 参数传递体
     */
    public void startActivity(Class c, int inId, int outId,Bundle bundle){
        Intent intent=new Intent(this,c);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(inId,outId);
        finish();
    }

}
