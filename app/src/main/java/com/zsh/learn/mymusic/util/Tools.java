package com.zsh.learn.mymusic.util;

import android.app.Activity;
import android.view.WindowManager;

/**
 * 常用的工具类
 * Created by zsh on 2017/4/19.
 */

public class Tools {

    /**
     * 强制满屏
     * @param activity 传一个activity过来。
     */
    public static void fullScreen(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
