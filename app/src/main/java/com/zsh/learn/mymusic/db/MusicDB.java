package com.zsh.learn.mymusic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zsh.learn.mymusic.entity.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsh on 2017/8/10.
 */


public class MusicDB {
    Context context;
    SQLiteDatabase db;
    public MusicDB(Context context){
        this.context=context;
        MusicDBHelper musicDBHelper=new MusicDBHelper(context);
        db= musicDBHelper.getReadableDatabase();
    }

    /**
     * 判断是否存在这首歌曲,通过url 来判断。
     * @param url1 url
     * @return
     */
    public boolean isHaveThisMusicByURL(String url1){
        Cursor cursor = null;
        cursor=db.rawQuery("select * from music where url=?",new String[]{url1});
        if(cursor!=null){//通过游标判断
            if(cursor.moveToNext()){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在这首歌曲，通过name来判断
     * @param name1 name
     * @return
     */
    public boolean isHaveThisMusicByName(String name1){
        Cursor cursor = null;
        cursor=db.rawQuery("select * from music where name=?",new String[]{name1});
        if(cursor!=null){//通过游标判断
            if(cursor.moveToNext()){
                return true;
            }
        }
        return false;
    }


    /**
     * 添加单首音乐信息
     * @param
     */
    public void addSingleMusicInfo(MusicInfo mi){
        ContentValues cv=new ContentValues();
        cv.put("name",mi.getName());
        cv.put("author",mi.getAuthor());
        cv.put("url",mi.getUrl());
        cv.put("isUse",mi.getIsUse());
        cv.put("isPlay",mi.getIsPlay());
        cv.put("isLove",mi.getIsLove());
        db.insert("music",null,cv);
    }

    /**
     * 查询一个,通过URL 查询
     * @param url1  url
     * @return
     */
    public MusicInfo findSingleMusicInfoByURL(String url1){
        Cursor cursor=null;
        MusicInfo hi=null;
        cursor=db.rawQuery("select * from music where url=?",new String[]{url1});
        if(cursor!=null){
            if (cursor.moveToNext()){
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String author=cursor.getString(cursor.getColumnIndex("author"));
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String isUse=cursor.getString(cursor.getColumnIndex("isUse"));
                String isPlay=cursor.getString(cursor.getColumnIndex("isPlay"));
                String isLove=cursor.getString(cursor.getColumnIndex("isLove"));
                hi=new MusicInfo(name,author,url,isUse,isPlay,isLove);
            }
        }
        cursor.close();
        return hi;
    }
    /**
     * 查询一个,通过name 查询
     * @param namel name
     * @return
     */
    public MusicInfo findSingleMusicInfoByName(String namel){
        Cursor cursor=null;
        MusicInfo hi=null;
        cursor=db.rawQuery("select * from music where name=?",new String[]{namel});
        if(cursor!=null){
            if (cursor.moveToNext()){
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String author=cursor.getString(cursor.getColumnIndex("author"));
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String isUse=cursor.getString(cursor.getColumnIndex("isUse"));
                String isPlay=cursor.getString(cursor.getColumnIndex("isPlay"));
                String isLove=cursor.getString(cursor.getColumnIndex("isLove"));
                hi=new MusicInfo(name,author,url,isUse,isPlay,isLove);
            }
        }
        cursor.close();
        return hi;
    }

    /**
     * 更新整首歌的信息，通过名字来更新。
     * @param name1 name
     * @param mi 音乐信息，MusicInfo
     */
    public void updateSingleMusicInfoByName(String name1,MusicInfo mi){
        ContentValues cv=new ContentValues();
        cv.put("name",mi.getName());
        cv.put("author",mi.getAuthor());
        cv.put("url",mi.getUrl());
        cv.put("isUse",mi.getIsUse());
        cv.put("isPlay",mi.getIsPlay());
        cv.put("isLove",mi.getIsLove());
        db.update("music", cv, "name=?", new String[]{name1});
    }

    /**
     * 更新整首歌的信息，通过URL来更新。
     * @param url1 url
     * @param mi 音乐信息，MUsicInfo
     */
    public void updateSingleMusicInfoByURL(String url1,MusicInfo mi){
        ContentValues cv=new ContentValues();
        cv.put("name",mi.getName());
        cv.put("author",mi.getAuthor());
        cv.put("url",mi.getUrl());
        cv.put("isUse",mi.getIsUse());
        cv.put("isPlay",mi.getIsPlay());
        cv.put("isLove",mi.getIsLove());
        db.update("music", cv, "url=?", new String[]{url1});
    }

    /**
     * 修改 isUse 字段，通过URL来修改
     * @param url1 URL
     * @param isUse1 isUse 属性值
     */
    public void updateSingleMusicIsUse(String url1,String isUse1){
        MusicInfo mi =findSingleMusicInfoByURL(url1);
        ContentValues cv=new ContentValues();
        cv.put("name",mi.getName());
        cv.put("author",mi.getAuthor());
        cv.put("url",mi.getUrl());
        cv.put("isUse",isUse1);
        cv.put("isPlay",mi.getIsPlay());
        cv.put("isLove",mi.getIsLove());
        db.update("music", cv, "url=?", new String[]{url1});
    }

    /**
     * 修改isPlay字段，通过url来修改
     * @param url1 URL
     * @param isPlay1
     */
    public void updateSingleMusicIsPlay(String url1,String isPlay1){
        MusicInfo mi =findSingleMusicInfoByURL(url1);
        ContentValues cv=new ContentValues();
        cv.put("name",mi.getName());
        cv.put("author",mi.getAuthor());
        cv.put("url",mi.getUrl());
        cv.put("isUse",mi.getIsUse());
        cv.put("isPlay",isPlay1);
        cv.put("isLove",mi.getIsLove());
        db.update("music", cv, "url=?", new String[]{url1});
    }



    /**
     * 删除一首歌,通过name来删除
     * @param name1 name
     */
    public void deleteSingleMusicByName(String name1){
        db.delete("music","name=?",new String[]{name1});
    }

    /**
     * 删除一首歌，通过URL来删除
     * @param url1 url
     */
    public void deleteSingleMusicByURL(String url1){
        db.delete("music","url=?",new String[]{url1});
    }

    /**
     * 删除全部
     */
    public void deleteAll(){
        db.delete("music",null,null);
    }


    /**
     * 模糊查询，通过name来查询
     * @param name1 name
     * @return
     */
    public List<MusicInfo> mohuCheck(String name1){
        List<MusicInfo> list=new ArrayList<MusicInfo>();
        Cursor cursor=null;

        //select  * from 表名 where 列名 like ？ ,new String []{'%'+传递进来查询的值+'%'};注意用rawQuery方法只能用单引号
        //Cursor cursor=db.rawQuery("select * from LLL where name like ?",new String[]{'%'+name+'%'});//Like代表通配符

        cursor=db.rawQuery("select * from music where name like '%"+name1+"%'",null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String author=cursor.getString(cursor.getColumnIndex("author"));
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String isUse=cursor.getString(cursor.getColumnIndex("isUse"));
                String isPlay=cursor.getString(cursor.getColumnIndex("isPlay"));
                String isLove=cursor.getString(cursor.getColumnIndex("isLove"));
                MusicInfo mi=new MusicInfo(name,author,url,isUse,isPlay,isLove);
                list.add(mi);
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 查询所有可以用的，下载好的
     * @return
     */
    public List<MusicInfo> findAllCanUse(){
        List<MusicInfo> list=new ArrayList<MusicInfo>();
        Cursor cursor=null;

        cursor=db.rawQuery("select * from music where isUse=1",null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String author=cursor.getString(cursor.getColumnIndex("author"));
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String isUse=cursor.getString(cursor.getColumnIndex("isUse"));
                String isPlay=cursor.getString(cursor.getColumnIndex("isPlay"));
                String isLove=cursor.getString(cursor.getColumnIndex("isLove"));
                MusicInfo mi=new MusicInfo(name,author,url,isUse,isPlay,isLove);
                list.add(mi);
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 查询所有
     * @return
     */
    public List<MusicInfo> findAll(){
        List<MusicInfo> list=new ArrayList<MusicInfo>();
        Cursor cursor=null;

        cursor=db.rawQuery("select * from music",null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String author=cursor.getString(cursor.getColumnIndex("author"));
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String isUse=cursor.getString(cursor.getColumnIndex("isUse"));
                String isPlay=cursor.getString(cursor.getColumnIndex("isPlay"));
                String isLove=cursor.getString(cursor.getColumnIndex("isLove"));
                MusicInfo mi=new MusicInfo(name,author,url,isUse,isPlay,isLove);
                list.add(mi);
            }
        }
        cursor.close();
        return list;
    }

}