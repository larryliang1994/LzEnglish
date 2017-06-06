package com.jiubai.lzenglish.config;

import com.jiubai.lzenglish.bean.Cartoon;

import java.util.ArrayList;

/**
 * 存放通用变量
 */
public class Config {
    public static String ThirdSession = "";
    public static String ResourceUrl = "http://cdnkid.quantest.cn/";

    public static String[] AgeGroups;
    public static String[] Ages;
    public static String[] PreferenceVideos;
    public static ArrayList<Cartoon> CartoonList;

    public static String AgeIndex;
    public static String PreferenceVideoIndex;

    public static int AppbarHeight = 0;
    public static int StatusbarHeight = 0;

    public static boolean IS_CONNECTED = false;
    public static int DeviceType;

    public static String UserName = "";
    public static String UserImage = "";
}