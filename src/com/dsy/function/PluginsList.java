package com.dsy.function;

import com.dsy.domain.PluginsInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.StringJoiner;

public class PluginsList {
    //获取插件文件列表
    public PluginsList() {
        initPluginsFile();
    }

    public void initPluginsFile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入基准文件夹的路径,如xxx\\plugins");
        String fSrc;
        while (true) {
            fSrc = sc.nextLine();
            String[] arr = fSrc.split("\\\\");
            if ("plugins".equals(arr[arr.length - 1])) {
                StringJoiner sj = new StringJoiner("\\\\", "", "");
                for (String s : arr) {
                    sj.add(s);
                }
                fSrc = sj.toString();
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter("other.txt",StandardCharsets.UTF_8));
                    bw.write(fSrc);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            } else {
                System.out.println("路径错误,请重新输入");
            }
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("plugins.txt", StandardCharsets.UTF_8));
            getAnyFileOutput(new File(fSrc), bw);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    //遍历文件夹,将字符串写进"plugins.txt"内
    public void getAnyFileOutput(File src, BufferedWriter bw) {
        File[] files = src.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                long length = file.length();
                long lastModified = file.lastModified();
                String path = file.getPath();
                PluginsInfo pi = new PluginsInfo(name, length, lastModified, path);
                try {
                    bw.write(pi.toString());
                    bw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                getAnyFileOutput(file, bw);
            }
        }
    }
}
