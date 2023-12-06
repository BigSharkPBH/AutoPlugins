package com.dsy.function;

import cn.hutool.core.io.FileUtil;
import com.dsy.domain.PluginsInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CopyFile {
    public CopyFile() {
        compare();
    }

    ArrayList<PluginsInfo> baseList = new ArrayList<>();
    String fSrc;//基准文件夹的路径

    /*
     * 比较和基准列表中有什么不一样
     * */
    public void compare() {
        //读取现在的插件列表
        if (!FileUtil.exist("list.txt")) {
            System.out.println("你必须先指定基准文件夹和目标文件夹");
            new PluginsList();
        }
        List<String> list = FileUtil.readUtf8Lines("list.txt");
        ArrayList<File> fileArrayList = new ArrayList<>();
        for (String s : list) {
            if ("0".equals(s.split("=")[0])) {
                fSrc = s.split("=")[1];
            } else {
                File file = new File(s.split("=")[1]);
                fileArrayList.add(file);
            }
        }
        getAnyFile(new File(fSrc), baseList);
        //如果白名单不存在就生成一个默认的
        if (!FileUtil.exist("white.yml") || FileUtil.size(FileUtil.file("white.yml")) == 0) {
            ArrayList<String> tempList = new ArrayList<>();
            Collections.addAll(tempList,
                    "# 一种是后缀名",
                    "db",
                    "h2",
                    "# 一种是文件名",
                    "#案例-5.3 - 副本.jar");
            FileUtil.writeUtf8Lines(tempList, "white.yml");
        }
        //读取需要被同步的文件夹列表
        List<String> whiteList = FileUtil.readUtf8Lines("white.yml");
        ArrayList<String> confirmList = new ArrayList<>();
        //遍历需要被同步文件夹下的所有文件
        for (File file : fileArrayList) {
            ArrayList<PluginsInfo> newList = new ArrayList<>();
            getAnyFile(file, newList);
            for (PluginsInfo pi1 : newList) {
                String[] a11 = pi1.getName().split("\\.");
                if (whiteList.contains(pi1.getName()) || whiteList.contains(a11[a11.length - 1])) {
                    continue;
                }
                //如果newList里不包含pi这个对象,说明有文件被删除了
                if (!baseList.contains(pi1)) {
                    //进入文件夹获取到里面的文件
                    StringBuilder sb = getRelativePath(pi1);
                    confirmList.add(file.getPath() + sb);
//                    FileUtil.del(file.getPath() + sb);
                }

            }
            for (PluginsInfo pi1 : baseList) {
                String[] a11 = pi1.getName().split("\\.");
                if (whiteList.contains(pi1.getName()) || whiteList.contains(a11[a11.length - 1])) {
                    continue;
                }
                //如果newList里不包含pi这个对象,说明有新增文件
                if (!newList.contains(pi1)) {
                    //去其他文件夹粘贴这个文件
                    for (File sonFile : fileArrayList) {
                        //进入文件夹获取到里面的文件
                        findFile(sonFile, pi1, true);
                    }

                }
            }

        }
        if (confirmList.size() != 0) {
            confirm(confirmList);
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
        if (isCopy) {
            StringBuilder sb = getRelativePath(pi);
            FileUtil.copy(pi.getPath(), src.getPath() + sb, true);
        } /*else {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    if (pi.getName().equals(name)) {
                        file.delete();
                        return;
                    }
                } else {
                    findFile(file, pi, false);
                }
            }
        }*/
    }

    public void confirm(ArrayList<String> confirmList) {
        s1:
        while (true) {
            System.out.println("----------------欢迎使用插件自动同步工具---------------");
            for (String s : confirmList) {
                System.out.println(s);
            }
            System.out.println("以上是将要删除的文件,请确认!!!");
            System.out.println("按1确认,按2取消操作.");
            System.out.println("---------------------[作者:大鲨鱼]--------------------");
            Scanner sc = new Scanner(System.in);
            int number = 0;
            while (true) {
                try {
                    number = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("请输入正确的数字");
                }
                break;
            }
            switch (number) {
                case 1 -> {
                    for (String s : confirmList) {
                        FileUtil.del(s);
                    }
                    break s1;
                }
                case 2 -> {
                    System.exit(0);
                }
            }
        }
    }
}
