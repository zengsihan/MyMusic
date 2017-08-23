package com.zsh.learn.mymusic.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 共享参数，用来判断是否第一次进入APP
 * Created by zsh on 2017/4/19.
 */

public class SaveMsg {

    /**
     * 保存是否首次进入APP
     * @param context
     * @param f true 下次进入还会当做是第一次进入APP，false,不是了。
     */
    public static void setIsFirstInApp(Context context,boolean f){
        SharedPreferences spf=context.getSharedPreferences("isFirstInApp",Context.MODE_PRIVATE);//申明对象，设置模式
        SharedPreferences.Editor editor=spf.edit();//获取editor操作题
        editor.putBoolean("first",f);//保存数据
        editor.commit();//提交
    }

    /**
     * 获取是否首次进入APP
     * @param context
     * @return
     */
    public static boolean getIsFirstInApp(Context context){
        SharedPreferences spf=context.getSharedPreferences("isFirstInApp",Context.MODE_PRIVATE);//这个要和set方法对应，name要一样
        return spf.getBoolean("first",true);//取出共享参数的数据。
    }

    /**
     * 在退出时，保存播放歌曲的信息
     * @param context
     * @param name 歌曲的name
     * @param position 歌曲的播放进度
     */
    public static void setMusicPlayInfo(Context context,String name,int position){
        SharedPreferences spf = context.getSharedPreferences("musicInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("name",name);
        editor.putInt("position",position);
        editor.commit();
    }

    /**
     * 获取在退出时保存的歌曲name
     * @param context
     * @return name
     */
    public static String getMusicPlayInfoName(Context context){
        SharedPreferences spf = context.getSharedPreferences("musicInfo",Context.MODE_PRIVATE);
        return spf.getString("name","nothing");
    }

    /**
     * 获取在退出时保存的歌曲播放进度
     * @param context
     * @return
     */
    public static int getMusicPlayInfoPosition(Context context){
        SharedPreferences spf = context.getSharedPreferences("musicInfo",Context.MODE_PRIVATE);
        return spf.getInt("position",-1);
    }
}
