package com.perqin.wechatted.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5 {
    private String originalString;

    public MD5(String origin) {
        originalString = origin;
    }

    public String getMD5edString() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] originalBytes = messageDigest.digest(originalString.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : originalBytes) {
                stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return getMD5edString();
    }
}
