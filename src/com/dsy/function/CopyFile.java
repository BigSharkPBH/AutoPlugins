package com.dsy.function;

import cn.hutool.core.io.FileUtil;
import com.dsy.domain.PluginsInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CopyFile {
    public CopyFile() {
        compare();
    }

    ArrayList<PluginsInfo> baseList = new ArrayList<>();
    //    ArrayList<PluginsInfo> newList = new ArrayList<>();
    String fSrc;//基准文件夹的路径

    /*
     * 比较和基准列表中有什么不一样
     * */
    public void compare() {
        //读取现在的插件列表
        BufferedReader br1 = null;
        try {
            br1 = new BufferedReader(new FileReader("list.txt", StandardCharsets.UTF_8));
            String line;
            while ((line = br1.readLine()) != null) {
                if ("0".equals(line.split("=")[0])) {
                    fSrc = line.split("=")[1];
                }
            }
            getAnyFile(new File(fSrc), baseList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br1 != null) {
                    br1.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //读取需要被同步的文件夹列表
        BufferedReader br3 = null;
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            br3 = new BufferedReader(new FileReader("list.txt", StandardCharsets.UTF_8));
            String line;
            while ((line = br3.readLine()) != null) {
                if (!"0".equals(line.split("=")[0])) {
                    File file = new File(line.split("=")[1]);
                    fileArrayList.add(file);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br3 != null) {
                    br3.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<String> whiteList = FileUtil.readUtf8Lines("..\\..\\..\\white.txt");
        //遍历需要被同步文件夹下的所有文件
        for (File file : fileArrayList) {
            ArrayList<PluginsInfo> newList = new ArrayList<>();
            getAnyFile(file, newList);
            for (PluginsInfo pi1 : newList) {
                String[] a11 = pi1.getName().split("\\.");
                if (whiteList.contains(pi1.getName()) || whiteList.contains(a11[a11.length - 1])) {
                    continue;
                }
                //如果newList里不包含pi这个对象
                if (!baseList.contains(pi1)) {
                    //进入文件夹获取到里面的文件
                    StringBuilder sb = getRelativePath(pi1);
                    FileUtil.del(file.getPath() + sb);
                }
            }
            for (PluginsInfo pi1 : baseList) {
                String[] a11 = pi1.getName().split("\\.");
                if (whiteList.contains(pi1.getName()) || whiteList.contains(a11[a11.length - 1])) {
                    continue;
                }
                //如果baseList里不包含pi这个对象
                if (!newList.contains(pi1)) {
                    //去其他文件夹粘贴这个文件
                    for (File sonFile : fileArrayList) {
                        //进入文件夹获取到里面的文件
                        findFile(sonFile, pi1, true);
                    }

                }
            }

        }
    }

    private static StringBuilder getRelativePath(PluginsInfo pi) {
        String[] strArr = pi.getPath().split("\\\\");
        StringBuilder sb = new StringBuilder();
        for (int i = strArr.length - 1; i >= 0; i--) {
            if ("plugins".equals(strArr[i])) {
                break;
            } else {
                sb.insert(0, strArr[i]);
                sb.insert(0, "\\");
            }
        }
        return sb;
    }

    public void getAnyFile(File src, ArrayList<PluginsInfo> list) {
        //获取现在的基准文件夹里所有的文件
        File[] files = src.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                long length = file.length();
                long lastModified = file.lastModified();
                String path = file.getPath();
                PluginsInfo pi = new PluginsInfo(name, length, lastModified, path);
                list.add(pi);
            } else {
                getAnyFile(file, list);
            }
        }
    }

    /*
     * 第一个参数
     *  传送过来要同步的文件夹路径
     * 第二个参数
     *  发生变化文件
     * 第三个参数
     *   true:覆盖
     *   false:删除
     * */
    public void findFile(File src, PluginsInfo pi, boolean isCopy) {
        //获取现在的基准文件夹里所有的文件
        File[] files = src.listFiles();
        if (isCopy) {
            StringBuilder sb = getRelativePath(pi);
            FileUtil.copy(pi.getPath(), src.getPath() + sb, true);
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    if (pi.getName().equals(name)) {
                        file.delete();
                        return;
                    }
                    long length = file.length();
                    long lastModified = file.lastModified();
                    String path = file.getPath();

                } else {
                    findFile(file, pi, false);
                }
            }
        }
    }
}
