package com.perqin.wechatted.database;


import android.provider.BaseColumns;

public final class EnMicroMsgContract {
    public static abstract class RconversationEntry implements BaseColumns {
        public static final String TABLE = "rconversation";
        public static final String COLUMN_MSG_COUNT = "msgCount";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_UN_READ_COUNT = "unReadCount";
        public static final String COLUMN_CHATMODE = "chatmode";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_IS_SEND = "isSend";
        public static final String COLUMN_CONVERSATION_TIME = "conversationTime";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_MSG_TYPE = "msgType";
        public static final String COLUMN_CUSTOM_NOTIFY = "customNotify";
        public static final String COLUMN_SHOW_TIPS = "showTips";
        public static final String COLUMN_FLAG = "flag";
        public static final String COLUMN_DIGEST = "digest";
        public static final String COLUMN_DIGEST_USER = "digestUser";
        public static final String COLUMN_HAS_TRUNC = "hasTrunc";
        public static final String COLUMN_PARENT_REF = "parentRef";
        public static final String COLUMN_ATTRFLAG = "attrflag";
        public static final String COLUMN_EDITING_MSG = "editingMsg";
        public static final String COLUMN_AT_COUNT = "atCount";
        public static final String COLUMN_SIGHT_TIME = "sightTime";
        public static final String COLUMN_UN_READ_MUTE_COUNT = "unReadMuteCount";
        public static final String COLUMN_LAST_SEQ = "lastSeq";
        public static final String COLUMN_UN_DELIVER_COUNT = "UnDeliverCount";
    }
}
