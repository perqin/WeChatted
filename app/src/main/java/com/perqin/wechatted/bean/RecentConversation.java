package com.perqin.wechatted.bean;


import android.database.Cursor;

import com.perqin.wechatted.database.EnMicroMsgContract;

public class RecentConversation {
    private long msgCount;
    private String username;
    private long unReadCount;
    private long chatmode;
    private long status;
    private long isSend;
    private long conversationTime;
    private String content;
    private String msgType;
    private String customNotify;
    private long showTips;
    private long flag;
    private String digest;
    private String digestUser;
    private long hasTrunc;
    private String parentRef;
    private long attrflag;
    private String editingMsg;
    private long atCount;
    private long sightTime;
    private long unReadMuteCount;
    private long lastSeq;
    private long UnDeliverCount;

    public static RecentConversation fromCursor(Cursor cursor) {
        RecentConversation bean = new RecentConversation();
        bean.setMsgCount(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_MSG_COUNT)));
        bean.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_USERNAME)));
        bean.setUnReadCount(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_UN_READ_COUNT)));
        bean.setChatmode(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_CHATMODE)));
        bean.setStatus(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_STATUS)));
        bean.setIsSend(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_IS_SEND)));
        bean.setConversationTime(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_CONVERSATION_TIME)));
        bean.setContent(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_CONTENT)));
        bean.setMsgType(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_MSG_TYPE)));
        bean.setCustomNotify(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_CUSTOM_NOTIFY)));
        bean.setShowTips(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_SHOW_TIPS)));
        bean.setFlag(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_FLAG)));
        bean.setDigest(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_DIGEST)));
        bean.setDigestUser(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_DIGEST_USER)));
        bean.setHasTrunc(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_HAS_TRUNC)));
        bean.setParentRef(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_PARENT_REF)));
        bean.setAttrflag(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_ATTRFLAG)));
        bean.setEditingMsg(cursor.getString(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_EDITING_MSG)));
        bean.setAtCount(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_AT_COUNT)));
        bean.setSightTime(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_SIGHT_TIME)));
        bean.setUnReadMuteCount(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_UN_READ_MUTE_COUNT)));
        bean.setLastSeq(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_LAST_SEQ)));
        bean.setUnDeliverCount(cursor.getLong(cursor.getColumnIndexOrThrow(EnMicroMsgContract.RconversationEntry.COLUMN_UN_DELIVER_COUNT)));
        return bean;
    }

    public long getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(long msgCount) {
        this.msgCount = msgCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public long getChatmode() {
        return chatmode;
    }

    public void setChatmode(long chatmode) {
        this.chatmode = chatmode;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getIsSend() {
        return isSend;
    }

    public void setIsSend(long isSend) {
        this.isSend = isSend;
    }

    public long getConversationTime() {
        return conversationTime;
    }

    public void setConversationTime(long conversationTime) {
        this.conversationTime = conversationTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getCustomNotify() {
        return customNotify;
    }

    public void setCustomNotify(String customNotify) {
        this.customNotify = customNotify;
    }

    public long getShowTips() {
        return showTips;
    }

    public void setShowTips(long showTips) {
        this.showTips = showTips;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDigestUser() {
        return digestUser;
    }

    public void setDigestUser(String digestUser) {
        this.digestUser = digestUser;
    }

    public long getHasTrunc() {
        return hasTrunc;
    }

    public void setHasTrunc(long hasTrunc) {
        this.hasTrunc = hasTrunc;
    }

    public String getParentRef() {
        return parentRef;
    }

    public void setParentRef(String parentRef) {
        this.parentRef = parentRef;
    }

    public long getAttrflag() {
        return attrflag;
    }

    public void setAttrflag(long attrflag) {
        this.attrflag = attrflag;
    }

    public String getEditingMsg() {
        return editingMsg;
    }

    public void setEditingMsg(String editingMsg) {
        this.editingMsg = editingMsg;
    }

    public long getAtCount() {
        return atCount;
    }

    public void setAtCount(long atCount) {
        this.atCount = atCount;
    }

    public long getSightTime() {
        return sightTime;
    }

    public void setSightTime(long sightTime) {
        this.sightTime = sightTime;
    }

    public long getUnReadMuteCount() {
        return unReadMuteCount;
    }

    public void setUnReadMuteCount(long unReadMuteCount) {
        this.unReadMuteCount = unReadMuteCount;
    }

    public long getLastSeq() {
        return lastSeq;
    }

    public void setLastSeq(long lastSeq) {
        this.lastSeq = lastSeq;
    }

    public long getUnDeliverCount() {
        return UnDeliverCount;
    }

    public void setUnDeliverCount(long unDeliverCount) {
        UnDeliverCount = unDeliverCount;
    }
}
