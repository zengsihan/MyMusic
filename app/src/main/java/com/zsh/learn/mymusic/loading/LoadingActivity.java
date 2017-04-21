package com.zsh.learn.mymusic.loading;

import android.view.View;
import android.widget.TextView;

import com.zsh.learn.mymusic.R;
import com.zsh.learn.mymusic.base.BaseActivity;
import com.zsh.learn.mymusic.home.MainActivity;

/**
 * 进入app的动画页面
 * Created by zsh on 2017/4/19.
 */

public class LoadingActivity extends BaseActivity{

    TextView tv;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_loading);
    }

    @Override
    public void getData() {

    }

    @Override
    public void getView() {
        tv= (TextView) findViewById(R.id.loading_tv_test);
    }

    @Override
    public void setView() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.class);//跳到主页面
            }
        });
    }
}
