package com.zsh.learn.mymusic.loading;

import android.os.Handler;
import android.os.Message;

import com.zsh.learn.mymusic.R;
import com.zsh.learn.mymusic.base.BaseActivity;
import com.zsh.learn.mymusic.home.MainActivity;

/**
 * 进入app的动画页面
 * Created by zsh on 2017/4/19.
 */

public class LoadingActivity extends BaseActivity{
    private SuperLoadingProgress loadingProgress;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_loading);
    }

    @Override
    public void getData() {

    }

    @Override
    public void getView() {
        loadingProgress = (SuperLoadingProgress) findViewById(R.id.loading_superloading);
    }

    @Override
    public void setView() {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        startActivity(MainActivity.class,R.anim.show,R.anim.hide);
                        break;
                    default:
                        break;
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                try {
                    loadingProgress.setProgress(0);
                    while (loadingProgress.getProgress()<100){
                        Thread.sleep(10);
                        loadingProgress.setProgress(loadingProgress.getProgress()+1);
                    }
                    loadingProgress.finishSuccess();
                    Thread.sleep(1800);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
