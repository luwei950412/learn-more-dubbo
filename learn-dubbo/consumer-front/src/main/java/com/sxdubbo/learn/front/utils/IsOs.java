package com.sxdubbo.learn.front.utils;

import java.util.Properties;

public class IsOs {

    public static boolean isOsWindows(){
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        System.out.println(os);
        if(os != null && os.toLowerCase().indexOf("linux") >-1){
            return false;
        }else{
            return true;
        }
//        return true;
    }
}
