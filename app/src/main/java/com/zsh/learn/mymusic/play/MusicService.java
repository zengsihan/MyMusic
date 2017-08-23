package com.zsh.learn.mymusic.play;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zsh.learn.mymusic.db.MusicDB;
import com.zsh.learn.mymusic.entity.MusicInfo;
import com.zsh.learn.mymusic.util.SaveMsg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsh on 2017/8/17.
 */

public class MusicService extends Service {
    private IBinder musicBinder = new MusicBinder();

    private MusicDB musicDB = null;

    private MediaPlayer mediaPlayer;

    // 本地歌曲的路径  Environment.getExternalStorageDirectory()+"/MusicDownload"
    private String path = Environment.getExternalStorageDirectory()+"/MusicDownload/";
    private String name = null;
    private List<MusicInfo> list = null;
    private static int index = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    // 初始化音乐播放器
    private void init() {
        musicDB = new MusicDB(this);
        list = new ArrayList<MusicInfo>();
        mediaPlayer = new MediaPlayer();
        list = musicDB.findAllCanUse();//获取下载好了的歌曲list

        String lastName = SaveMsg.getMusicPlayInfoName(this);
        if (lastName.equals("nothing")){//第一次进来
            index = 0;
        }else{
            boolean f = musicDB.isHaveThisMusicByName(lastName);
            if (f){//有这首歌
                for(int i=0;i<list.size();i++){
                    if (lastName.equals(list.get(i).getName())){
                        index = i;
                        break;
                    }
                }
            }else{
                index = 0;
            }
        }

        initMusic();
    }

    /**
     * 初始化音乐播放
     */
    private void initMusic(){
//        mediaPlayer = new MediaPlayer();
        name = getMusicName();
        try {
            mediaPlayer.setDataSource(path+name+".mp3");
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.prepareAsync(); //用这个点下一首会出错
            mediaPlayer.prepare();
//            int position = SaveMsg.getMusicPlayInfoPosition(this);
//            if(position != -1){
//                mediaPlayer.seekTo(position);
//            }

            Log.i("aa","initMusic(), prep");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                mp.reset();
                return false;
            }
        });
    }


    /**
     * 获取正在播放的歌曲名字。
     * @return name
     */
    public String getMusicName() {
        MusicInfo mi = list.get(index);
        if (mi.getIsPlay().equals("0")){
            musicDB.updateSingleMusicIsPlay(mi.getUrl(),"1"); //修改是否播放过
        }
        return mi.getName();
    }

    /**
     * 获取正在播放的歌曲的author
     * @return
     */
    public String getMusicAuthor(){
        MusicInfo mi = list.get(index);
        return mi.getAuthor();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    class MusicBinder extends Binder{
        public Service getService(){
            return MusicService.this;
        }
    }

    /**
     * 返回当前播放进度，double类型，百分比
     * @return
     */
    public double getProgress(){
        int position = mediaPlayer.getCurrentPosition(); //获取当前播放时间
        int duration = mediaPlayer.getDuration(); //获取总时间
        double progress = (double)position / (double)duration;
        return progress;
    }

    /**
     * 通过activi调节播放进度
     * @param max seekbar的最大值，
     * @param dest seekbar的当前点
     */
    public void setProgress(int max, int dest){
        int duratin = mediaPlayer.getDuration();
        mediaPlayer.seekTo(duratin* dest/max);
    }

    public boolean isPlaying(){
        if (mediaPlayer!=null){
            return mediaPlayer.isPlaying();
        }else {
            return false;
        }
    }

    /**
     * 得到音乐的总时长
     * @return
     */
    public int getDuration(){
        if (mediaPlayer!=null){
            return mediaPlayer.getDuration();
        }else{
            return 0;
        }
    }

    /**
     * 得到音乐的当前时间
     * @return
     */
    public int getCurrentPosition(){
        if (mediaPlayer!=null){
            return mediaPlayer.getCurrentPosition();
        }else {
            return 0;
        }
    }

    public MediaPlayer.TrackInfo[] getInfo(){
        if (mediaPlayer!=null){
            return mediaPlayer.getTrackInfo();
        }else {
            return null;
        }
    }

    /**
     * 播放音乐
     */
    public void play(){
        if (mediaPlayer!=null){
            Log.i("aa","service  play()");
            mediaPlayer.start();
//            mediaPlayer.setNextMediaPlayer(mediaPlayer);
        }
    }

    /**
     * 暂停
     */
    public void pause(){
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            Log.i("aa","service  pause()");
            mediaPlayer.pause();
        }
    }

    /**
     * 下一首
     */
    public void playNext(){
        if (mediaPlayer!=null){
            Log.i("aa","service palyNext");
            if (index>=list.size()-1){
                index = 0;
            }else {
                index ++;
            }
            mediaPlayer.reset();
            initMusic();
            play();
        }
    }

    /**
     * 上一首
     */
    public void playPre(){
        if (mediaPlayer!=null){
            Log.i("aa","service palyPre");
            if (index<=0){
                index = list.size()-1;
            }else {
                index --;
            }
            mediaPlayer.reset();
            initMusic();
            play();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer!=null){
            SaveMsg.setMusicPlayInfo(this,name,mediaPlayer.getCurrentPosition());
        }
        // 回收资源
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}