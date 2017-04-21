package com.zsh.learn.mymusic.lead;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * LeaddActivity 中Viewpager的适配器
 * Created by zsh on 2017/4/19.
 */

public class LeadAdapter extends PagerAdapter{
    Context context;
    List<View> list;

    public LeadAdapter(Context context, List<View> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=list.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view =list.get(position);
        container.removeView(view);
    }
}
