package com.zsh.learn.mymusic.entity;

/**
 * Created by zsh on 2017/8/10.
 */

public class MusicInfo {
    private String name;  // 歌曲名字
    private String author; // 唱歌的人
    private String url;  // URL 下载地址
    private String isUse;  // 是否能下载 ，0不能，1能
    private String isPlay;  // 是否播放过 0 没有，1 播放了
    private String isLove;  // 是否喜欢这首歌 0 否，1 是

    public MusicInfo(String name, String author, String url, String isUse, String isPlay, String isLove) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.isUse = isUse;
        this.isPlay = isPlay;
        this.isLove = isLove;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", isUse='" + isUse + '\'' +
                ", isPlay='" + isPlay + '\'' +
                ", isLove='" + isLove + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(String isPlay) {
        this.isPlay = isPlay;
    }

    public String getIsLove() {
        return isLove;
    }

    public void setIsLove(String isLove) {
        this.isLove = isLove;
    }
}
