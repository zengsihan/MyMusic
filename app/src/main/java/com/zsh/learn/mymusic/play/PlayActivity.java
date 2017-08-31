package com.zsh.learn.mymusic.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zsh.learn.mymusic.R;
import com.zsh.learn.mymusic.base.BaseActivity;
import com.zsh.learn.mymusic.home.MainActivity;

/**
 * 音乐播放的详细界面
 * Created by zsh on 2017/4/19.
 */

public class PlayActivity extends BaseActivity implements View.OnClickListener{
    private ImageView img_goback;
    private TextView tv_name,tv_author;
    private TextView tv_lrc;
    private TextView tv_currentPosition,tv_duration;
    private SeekBar seekBar;
    private ImageView img_love,img_pre,img_play,img_next,img_delete;
    private MusicService musicService;


    Thread thread; //多线程，后台更新UI
    public static boolean playStatus = true; // 控制后台线程退出
    private boolean isBound = false; // 是否和service绑定了

    private boolean isNext = true; // 用于自动播放下一首的标识

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_play);
    }

    @Override
    public void getData() {
        thread = new Thread(new MyThread());
        Intent serviceIntent = new Intent(this,MusicService.class);
        // 绑定service
        if(!isBound){
            Log.i("aa","getData(), binService");
            bindService(serviceIntent,connection,Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("aa","ServiceConnection, ok");
            // 链接成功时，获取binder
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            // 得到service
            musicService = (MusicService) musicBinder.getService();
            // 绑定成功，修改flag
            isBound = true;

            setUIInfo();

            // 开启线程，更新UI
            thread.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("aa","ServiceConnection, faild");
            // 断开链接时，修改flag
            isBound = false;
        }
    };

    @Override
    public void getView() {
        img_goback = (ImageView) findViewById(R.id.play_goback);
        tv_name = (TextView) findViewById(R.id.play_music_name);
        tv_author = (TextView) findViewById(R.id.play_author);
        tv_lrc = (TextView) findViewById(R.id.play_lrc);
        tv_currentPosition = (TextView) findViewById(R.id.play_currentPosition);
        seekBar = (SeekBar) findViewById(R.id.play_progressbar);
        tv_duration = (TextView) findViewById(R.id.play_duration);
        img_love = (ImageView) findViewById(R.id.play_music_fav);
        img_pre = (ImageView) findViewById(R.id.play_music_pre);
        img_play = (ImageView) findViewById(R.id.play_music_play);
        img_next = (ImageView) findViewById(R.id.play_music_next);
        img_delete = (ImageView) findViewById(R.id.play_music_abandon);
    }

    @Override
    public void setView() {
        img_goback.setOnClickListener(this);
        img_love.setOnClickListener(this);
        img_pre.setOnClickListener(this);
        img_play.setOnClickListener(this);
        img_next.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { //手动调节进度
                // seekbar的拖动位置
                int dest = seekBar.getProgress();
                // seekbar 的最大值
                int max = seekBar.getMax();
                // 调用service调节播放进度
                musicService.setProgress(max,dest);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_goback: //返回
                startActivity(MainActivity.class);
                break;
            case R.id.play_music_fav: //收藏，暂不处理。
                break;
            case R.id.play_music_pre:
                playPreMusic();
                break;
            case R.id.play_music_play:
                Log.i("aa","onclick, play");
                playOrPauseMusic();
                break;
            case R.id.play_music_next:
                playNextMusic();
                break;
            case R.id.play_music_abandon:
                break;
            default :
                break;
        }
    }

    // 处理进度条更新
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                // 处理progress进度条
                case 0:
                    // 从bundle中获取进度
                    double progress = msg.getData().getDouble("progress");
                    int max = seekBar.getMax();
                    // 计算seekbar的实际位置
                    int position = (int) (max*progress);
                    seekBar.setProgress(position);
                    tv_currentPosition.setText(getTimeString(musicService.getCurrentPosition()));
                    break;
                // 处理自动播放下一首
                case 1:
                    playNextMusic();
                    isNext = true;
                    break;
                default:
                    break;
            }
        }
    };


    // 实现runnable接口，多线程更新进度条
    public class MyThread implements Runnable{
        // 通知UI更新的消息


        // 用来向UI线程传递进度的值
        Bundle bundle = new Bundle();

        // 更新UI的间隔时间,毫秒
        int millisecond = 100;
        double progress;

        @Override
        public void run() {
            // 用来标识是个还在播放状态，用来控制线程的退出
            while(playStatus){
                if(isBound){
                    // 发送消息，要求更新UI
                    Message msg = new Message();
                    bundle.clear();
                    // 得到当前播放进度
                    progress = musicService.getProgress();

                    // 当播放结束后，发消息，准备播放下一首
                    if (progress>=1 && isNext){
                        isNext = false;
                        handler.sendEmptyMessage(1);
                    }
                    msg.what = 0;
                    bundle.putDouble("progress",progress);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                try {
                    Thread.sleep(millisecond); //每隔100ms更新一个UI
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean cheekIsPlaying(){
        return false;
    }

    /**
     * 播放或者暂停
     */
    private void playOrPauseMusic(){
        Log.i("aa","playOrPauseMusic(),isBound="+isBound);
        if (isBound){
            setUIInfo();
            if (musicService.isPlaying()){
                img_play.setImageResource(R.drawable.play_btn_play);
                musicService.pause();
            }else {
                img_play.setImageResource(R.drawable.play_btn_pause);
                musicService.play();
            }
        }
    }

    /**
     * 播放上一首歌曲
     */
    private void playPreMusic(){
        Log.i("aa","playPreMusic()");
        if (isBound){
            musicService.playPre();
            setUIInfo();
            img_play.setImageResource(R.drawable.play_btn_pause);
        }
    }

    /**
     * 播放下一首歌曲
     */
    private void playNextMusic(){
        Log.i("aa","playNextMusic()");
        if (isBound){
            musicService.playNext();
            setUIInfo();
            img_play.setImageResource(R.drawable.play_btn_pause);
        }
    }


    private void deleteMusic(){

    }


    private String getTimeString(int millisecond){
        int min = (int) Math.floor(millisecond/1000/60);
        int seconds =(millisecond/1000)-(min*60);
        String mins = min+"";
        if (min<10){
            mins = "0"+mins;
        }
        return mins+":"+String.format("%02d",seconds);
    }

    /**
     * 填写UI界面的歌曲信息
     */
    private void setUIInfo(){
        tv_duration.setText(getTimeString(musicService.getDuration()));
        tv_currentPosition.setText(getTimeString(musicService.getCurrentPosition()));
        tv_name.setText(musicService.getMusicName());
        tv_author.setText(musicService.getMusicAuthor());
    }

    @Override
    protected void onDestroy() {
        playStatus = false;
        super.onDestroy();
    }
}
