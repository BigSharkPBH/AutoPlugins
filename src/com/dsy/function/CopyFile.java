package com.dsy.function;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.dsy.domain.PluginsInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringJoiner;

public class CopyFile {
    public CopyFile() {
        compare();
    }

    ArrayList<PluginsInfo> baseList = new ArrayList<>();
    ArrayList<PluginsInfo> newList = new ArrayList<>();
    String fSrc;//基准文件夹的路径

    /*
     * 比较和基准列表中有什么不一样
     * */
    public void compare() {
        //baseList
        BufferedReader br1 = null;
        try {
            br1 = new BufferedReader(new FileReader("plugins.txt", StandardCharsets.UTF_8));
            String line;
            while ((line = br1.readLine()) != null) {
                addList(line);
            }
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
//        System.out.println(baseList.toString());
        //读取现在的插件列表
        BufferedReader br2 = null;
        try {
            br2 = new BufferedReader(new FileReader("other.txt", StandardCharsets.UTF_8));
            fSrc = br2.readLine();
            getAnyFileOutput(new File(fSrc));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br2 != null) {
                    br2.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        System.out.println(newList.toString());
        //读取需要被同步的文件夹
        BufferedReader br3 = null;
        ArrayList<File> fileArrayList = new ArrayList<>();
        try {
            br3 = new BufferedReader(new FileReader("list.txt", StandardCharsets.UTF_8));
            String line;
            while ((line = br3.readLine()) != null) {
                File file = new File(line);
                fileArrayList.add(file);
//                C:\Users\PBHls\Desktop\测试\同步1\plugins
//                C:\Users\PBHls\Desktop\测试\同步2\plugins
            }
//            System.out.println(fileArrayList);
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
        //比较两个集合有什么不一样
//        if (baseList.size() > newList.size()) {
        //如果baseList比newList大,说明有文件被删除
        for (PluginsInfo pi1 : baseList) {
            //如果newList里不包含pi这个对象
            if (!newList.contains(pi1)) {
                //去其他文件夹删除这个文件
                for (File file : fileArrayList) {
                    //进入文件夹获取到里面的文件
                    findFile(file, pi1, false);
                }

            }
            //遍历newList挨个比较
                /*for (PluginsInfo pi2 : newList) {
                    //如果对象不一样
                    if (!pi2.equals(pi1)) {
                        //如果对象的名字不一样
                        if (!pi2.getName().equals(pi1.getName())) {

                        }
                    }
                }*/
        }
        for (PluginsInfo pi1 : newList) {
            //如果baseList里不包含pi这个对象
            if (!baseList.contains(pi1)) {
                //去其他文件夹粘贴这个文件
                for (File file : fileArrayList) {
                    //进入文件夹获取到里面的文件
                    findFile(file, pi1, true);
                }

            }
        }
//        }

    }

    public void addList(String str) {
        //把基准文本里的数据读取到内存
        String[] splitArr = str.split("&");
        String name = splitArr[0].split("=")[1];
        long length = Long.parseLong(splitArr[1].split("=")[1]);
        long lastModified = Long.parseLong(splitArr[2].split("=")[1]);
        String path = splitArr[3].split("=")[1];
        PluginsInfo pi = new PluginsInfo(name, length, lastModified, path);
        baseList.add(pi);

    }

    public void getAnyFileOutput(File src) {
        //获取现在的基准文件夹里所有的文件
        File[] files = src.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                long length = file.length();
                long lastModified = file.lastModified();
                String path = file.getPath();
                PluginsInfo pi = new PluginsInfo(name, length, lastModified, path);
                newList.add(pi);
            } else {
                getAnyFileOutput(file);
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
            FileUtil.copy(pi.getPath(), src.getPath(), true);
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    if (pi.getName().equals(name)) {
                        file.delete();
                    }
                    long length = file.length();
                    long lastModified = file.lastModified();
                    String path = file.getPath();

                } else {
                    getAnyFileOutput(file);
                }
            }
        }
    }
}
