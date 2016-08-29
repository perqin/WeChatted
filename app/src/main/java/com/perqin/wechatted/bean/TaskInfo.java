package com.perqin.wechatted.bean;


public class TaskInfo {
    public static final int NOT_START = 0;
    public static final int RUNNING = 1;
    public static final int SUCCEED = 2;
    public static final int FAIL = 3;

    public int state;
    public String description;

    public TaskInfo() {
        this(NOT_START, "");
    }

    public TaskInfo(int state, String description) {
        this.state = state;
        this.description = description;
    }
}
