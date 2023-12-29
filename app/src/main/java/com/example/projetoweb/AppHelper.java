package com.example.projetoweb;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AppHelper {
    public static final String BOUNDARY = "apiclient-" + System.currentTimeMillis();
    public static final String PARAMS_ENCODING = "UTF-8";
    public static byte[] getFileDataFromPath(Context context, String path) {
        byte[] data = null;
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static String getParamsEncoding() {
        return PARAMS_ENCODING;
    }
}