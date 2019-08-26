package com.sxdubbo.learn.front.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;

public class HdfsUtil {
    private static String HDFSUri = "hdfs://namenode:9000";
    //System.setProperty("hadoop.home.dir", "C:\\projectBS\\hadoop-2.7.7");

    /**
     * 获取文件系统
     *
     * @return FileSystem 文件系统
     */
    public static FileSystem getFileSystem(){
        //
        Configuration conf = new Configuration();
        FileSystem fs = null;
        String hdfsUri = HDFSUri;
        if(StringUtils.isBlank(hdfsUri)){
            try{
                fs = FileSystem.get(conf);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try {
                URI uri = new URI(hdfsUri.trim());
                fs = FileSystem.get(uri,conf);
            } catch (Exception e) {
              //  e.printStackTrace();
            }
        }
        return  fs;
    }

    /**
     * 创建文件目录
     *
     * @param path 文件路径
     */
    public static void mkdir(String path) {
        try {
            FileSystem fs = getFileSystem();
            System.out.println("FilePath="+path);
            // 创建目录
            fs.mkdirs(new Path(path));
            //释放资源
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断目录是否存在
     *
     * @param filePath 目录路径
     * @param create 若不存在是否创建
     */
    public static  boolean existDir(String filePath, boolean create){
        boolean flag = false;

        if (StringUtils.isEmpty(filePath)){
            return flag;
        }

        try{
            Path path = new Path(filePath);
            // FileSystem对象
            FileSystem fs = getFileSystem();

            if (create){
                if (!fs.exists(path)){
                    fs.mkdirs(path);
                }
            }

            if (fs.isDirectory(path)){
                flag = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 本地文件上传至 HDFS
     *
     * @param srcFile 源文件 路径
     * @param destPath hdfs路径
     */
    public static void copyFileToHDFS(File file,String destPath)throws Exception{


        FileInputStream fis=new FileInputStream(file);//读取本地文件
        Configuration config=new Configuration();
        FileSystem fs=FileSystem.get(URI.create(HDFSUri+destPath), config);
        OutputStream os=fs.create(new Path(destPath));
        //copy
        IOUtils.copyBytes(fis, os, 4096, true);
        System.out.println("拷贝完成...");
        fs.close();
    }
    public static void copyFileToHDFS(InputStream fis,String destPath)throws Exception{


        //FileInputStream fis=new FileInputStream(file);//读取本地文件
        Configuration config=new Configuration();
        FileSystem fs=FileSystem.get(URI.create(HDFSUri+destPath), config);
        OutputStream os=fs.create(new Path(destPath));
        //copy
        IOUtils.copyBytes(fis, os, 4096, true);
        System.out.println("拷贝完成...");
        fs.close();
    }
    /**
     * 从 HDFS 下载文件到本地
     *
     * @param srcFile HDFS文件路径
     * @param destPath 本地路径
     */
    public static void getFile(String srcFile,String destPath)throws Exception {
        //hdfs文件 地址
        String file=HDFSUri+srcFile;
        Configuration config=new Configuration();
        //构建FileSystem
        FileSystem fs = FileSystem.get(URI.create(file),config);
        //读取文件
        InputStream is=fs.open(new Path(file));
        IOUtils.copyBytes(is, new FileOutputStream(new File(destPath)),2048, true);//保存到本地  最后 关闭输入输出流
        System.out.println("下载完成...");
        fs.close();
    }

    /**
     * 删除文件或者文件目录
     *
     * @param path
     */
    public static void rmdir(String path) {
        try {
            // 返回FileSystem对象
            FileSystem fs = getFileSystem();

            String hdfsUri = HDFSUri;
            if(StringUtils.isNotBlank(hdfsUri)){
                path = hdfsUri + path;
            }
            System.out.println("path:"+path);
            // 删除文件或者文件目录  delete(Path f) 此方法已经弃用
            System.out.println( fs.delete(new Path(path),true));

            // 释放资源
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件的内容
     * @param filePath
     * @throws IOException
     */
    public static void readFile(String filePath) throws IOException{
        Configuration config = new Configuration();
        String file=HDFSUri+filePath;
        FileSystem fs = FileSystem.get(URI.create(file),config);
        //读取文件
        InputStream is=fs.open(new Path(file));
        //读取文件
        IOUtils.copyBytes(is, System.out, 2048, false); //复制到标准输出流
        fs.close();
    }

//    public static void WriteToHDFS(String file, String source) throws IOException, URISyntaxException {
//        Configuration conf = new Configuration();
//        FileSystem fs = FileSystem.get(URI.create(file), conf);
//        Path path = new Path(file);
//        FSDataOutputStream out = fs.create(path); // 创建文件
//
//         FSDataInputStream in = fs.open(new Path(source));
//         IOUtils.copyBytes(in, out, 4096, true); //4096为一次复制块大小，true表示复制完成后关闭流
//        out.close();
//    }

}
