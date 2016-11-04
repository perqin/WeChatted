package com.perqin.wechatted.adapter.bean;


import net.sqlcipher.Cursor;

public class Conversation {
    private String title;
    private String time;
    private String digest;
    private int messageCount;

    // TODO
    public static Conversation fromCursor(Cursor cursor) {
        return new Conversation();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
}
