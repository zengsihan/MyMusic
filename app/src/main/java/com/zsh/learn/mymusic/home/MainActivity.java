package com.zsh.learn.mymusic.home;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zsh.learn.mymusic.R;
import com.zsh.learn.mymusic.base.BaseActivity;
import com.zsh.learn.mymusic.db.MusicDB;
import com.zsh.learn.mymusic.entity.MusicInfo;
import com.zsh.learn.mymusic.play.PlayActivity;
import com.zsh.learn.mymusic.util.Tools;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private Button btn_download,btn_delete, btn_paly,btn_otherPlay;
    private TextView tv_show,tv_show2,tv_show3,tv_show4,tv_show5,tv_show6,tv_show7;
    private MusicDB musicDB;
    private long downloadId;
    private DownloadManager downloadManager;
    private static int number=0; //下载成功
    private static int number2=0;//下载失败

    private static final int DOWNLOAD_SUCCESS = 1;
    private static final int DOWNLOAD_FAILD = 2;

    private static int num = 100;
    private int successNum=0;
    private int faildNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_main);

    }

    @Override
    public void getData() {

    }

    @Override
    public void getView() {
        tv_show= (TextView) findViewById(R.id.main_tv_showinfo);
        tv_show2= (TextView) findViewById(R.id.main_tv_showinfo2);
        tv_show3= (TextView) findViewById(R.id.main_tv_showinfo3);
        tv_show4= (TextView) findViewById(R.id.main_tv_showinfo4);
        tv_show5= (TextView) findViewById(R.id.main_tv_showinfo5);
        tv_show6= (TextView) findViewById(R.id.main_tv_showinfo6);
        tv_show7= (TextView) findViewById(R.id.main_tv_showinfo7);
        btn_download= (Button) findViewById(R.id.main_btn_download);
        btn_delete= (Button) findViewById(R.id.main_btn_delete);
        btn_paly= (Button) findViewById(R.id.main_btn_play);
        btn_otherPlay= (Button) findViewById(R.id.main_btn_otherplay);
    }

    @Override
    public void setView() {
        musicDB = new MusicDB(this);
        btn_download.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_paly.setOnClickListener(this);
        btn_otherPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_download:
                tv_show.setText("开始下载...");
                Log.i("aa","click btn_download: 开始下载。。。");
                if(Tools.count==0){
                    readExcelToDB(0,30);
                }else{
                    Toast.makeText(this, "删除原来的数据库和歌曲", Toast.LENGTH_SHORT).show();
                    Log.i("aa","click btn_download: 删除原来的数据库和歌曲");
                    deleteDBAll();
                    deleteMusicFile();
                    readExcelToDB(Tools.count,30);
                }
                break;
            case R.id.main_btn_delete:
                deleteDBAll();
                deleteMusicFile();
                break;
            case R.id.main_btn_play:
                startActivity(PlayActivity.class); // 跳转到播放页
                break;
            case R.id.main_btn_otherplay:
                openFileByOtherApp();
                break;
            default:
                break;
        }
    }

    private void openFileByOtherApp() {
        File file = new File(Environment.getExternalStorageDirectory()+"/MusicDownload");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(file), "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivity(intent);
    }

    /**
     * 删除下载下来的音乐
     */
    private void deleteMusicFile(){
        File dir = new File(Environment.getExternalStorageDirectory()+"/MusicDownload");
        deleteDirWithFile(dir);
    }

    public  void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile()){
                file.delete(); // 删除所有文件
            }
            else if (file.isDirectory()){
                deleteDirWithFile(file); // 递规的方式删除文件夹
            }
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 删除数据库里的所有数据
     */
    private void deleteDBAll(){
        musicDB.deleteAll();
    }

    /**
     * 把数据从Excel里面读取到数据库里，一开始是0-99 ，100首歌
     * @param k 开始的行数
     * @param num1 读取的数量
     */
    private void readExcelToDB(int k,int num1) {
        Log.i("aa","readExcelToDB(): k="+k+" ,num="+num1);
        number=0;
        number2=0;
        faildNum=0;
        successNum=0;
        num=num1;
        try {
            Workbook workbook = Workbook.getWorkbook(getAssets().open("music.xls"));//打开Excel表的路径
            Sheet sheet = workbook.getSheet(0);// 获取第一张表的对象
            // 行数
            int rows = sheet.getRows();
            // 列数
            int cols = sheet.getColumns();

            if(k+num<=rows){
                for(int i=k;i<num+k;i++){
                    String name = sheet.getCell(1,i).getContents();
                    String author = sheet.getCell(2,i).getContents();
                    String url = sheet.getCell(4,i).getContents();
                    Tools.count++; // 记录Excel的数据读取到了到第几行。
                    Log.i("aa","readExcelToDB() for 循环: Tools.count="+Tools.count);
                    tv_show5.setText("Tools.conut="+Tools.count);
                    if(URLUtil.isHttpUrl(url)||URLUtil.isHttpsUrl(url)){
                        downLoadMusic(url,name);
                        MusicInfo musicInfo = new MusicInfo(name,author,url,"0","0","0");
                        musicDB.addSingleMusicInfo(musicInfo);
                    }else {
                        number2++;
                        Message message2 = handler.obtainMessage();
                        message2.what=DOWNLOAD_FAILD;
                        message2.arg1=number2;
                        handler.sendMessage(message2);
                        Log.i("aa","readExcelToDB()，for ,else, 下载失败，number2="+number2);
                    }
                }
                tv_show.setText("excel数据复制到数据库，一共查询了 "+ num1 +" 条数据。");
            }else{
                Toast.makeText(this, "Excel里的数据不够这么多条！", Toast.LENGTH_SHORT).show();
                Tools.count=0;
                readExcelToDB(0,100);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载音乐，
     * @param url 下载的URL
     * @return 0 未下载成功， 1下载成功。
     */
    private void downLoadMusic(String url,String name){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));// 创建下载任务
        request.setDestinationInExternalPublicDir("/MusicDownload/",name+".mp3");//保存地址

        // 移动网络情况下是否允许漫游？？
        request.setAllowedOverRoaming(false);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        // 允许下载下来的MP3被其他应用扫描
        request.setVisibleInDownloadsUi(true);

        downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);// 获取DownloadManager

        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadManager.enqueue(request);

        // 注册广播接受者，监听下载状态
        this.registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    //监听下载状态的广播
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            cheekStatus(downloadId);
        }
    };

    // 检查下载状态的方法
    private void cheekStatus(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        // 通过下载的ID查找
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if(cursor.moveToFirst()){
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status){
                // 下载暂停
                case DownloadManager.STATUS_PAUSED:
//                    tv_show6.setText("下载暂停："+id);
                    Log.i("aa","cheekStatus() 下载暂停，id="+id);
                    break;
                // 下载延迟
                case DownloadManager.STATUS_PENDING:
//                    tv_show5.setText("下载延迟："+id);
                    Log.i("aa","cheekStatus() 下载延迟，id="+id);
                    break;
                // 正在下载
                case DownloadManager.STATUS_RUNNING:
//                    tv_show2.setText("正在下载："+id);
                    Log.i("aa","cheekStatus() 正在下载，id="+id);
                    break;
                // 下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    number++;
                    Message message = handler.obtainMessage();
                    message.what=DOWNLOAD_SUCCESS;
                    message.arg1=number;
                    handler.sendMessage(message);
                    Log.i("aa","cheekStatus() 下载成功，number="+number+", id="+id);
                    tv_show3.setText("下载成功,number="+number);
                    String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                    if(musicDB.isHaveThisMusicByURL(url)){
                        musicDB.updateSingleMusicIsUse(url,"1");
                    }
                    break;
                // 下载失败
                case DownloadManager.STATUS_FAILED:
                    downloadManager.remove(id);
                    number2++;
                    Message message2 = handler.obtainMessage();
                    message2.what=DOWNLOAD_FAILD;
                    message2.arg1=number2;
                    handler.sendMessage(message2);
                    Log.i("aa","cheekStatus() 下载失败，number2="+number2+", id="+id);
                    tv_show4.setText("下载失败,number="+number2+" , downloadId="+id);
                    String url2 = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                    if(musicDB.isHaveThisMusicByURL(url2)){
                        musicDB.deleteSingleMusicByURL(url2);
                    }
                    break;
            }
        }
    }

   private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {

           switch (msg.what){
               case DOWNLOAD_SUCCESS:
                   successNum = msg.arg1;
                   Log.i("aa","handler  DOWNLOAD_SUCCESS，successNum="+successNum);
                   break;
               case DOWNLOAD_FAILD:
                   faildNum = msg.arg1;
                   Log.i("aa","handler  DOWNLOAD_FAILD，faildNum="+faildNum);
                   break;
               default:
                   break;
           }
           if((successNum+faildNum) == num){
               Log.i("aa","handler  if ，successNum+faildNum="+(successNum+faildNum));
               if(faildNum>0){
                   readExcelToDB(Tools.count,faildNum);
                   Log.i("aa","handler  readExcelToDB，num="+faildNum);
               }else{
                   tv_show.setText("100首歌更新完成。");
                   Log.i("aa","100首更新完成。");
               }
           }
       }
   };


}
