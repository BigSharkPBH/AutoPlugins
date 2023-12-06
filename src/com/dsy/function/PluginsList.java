package com.dsy.function;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class PluginsList {
    //获取插件文件列表
    public PluginsList() {
        tips();
    }

    public void tips() {
        while (true) {
            System.out.println("----------------欢迎使用插件自动同步工具---------------");
            System.out.println("1.更新基准文件夹");
            System.out.println("2.添加目标文件夹");
            System.out.println("3.主菜单");
            System.out.println("请输入对应的数字");
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
                    initPluginsFile();
                }
                case 2 -> {
                    addList();
                }
                case 3 -> {
                    return;
                }
                default -> System.out.println("请输入正确的数字");
            }
        }
    }

    public void addList() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入目标文件夹的路径,如xxx\\plugins");
        String fSrc;
        while (true) {
            fSrc = sc.nextLine();
            String[] arr = fSrc.split("\\\\");
            if ("plugins".equals(arr[arr.length - 1])) {
                StringJoiner sj = new StringJoiner("\\", "", "");
                for (String s : arr) {
                    sj.add(s);
                }
                fSrc = sj.toString();
                if (!FileUtil.exist("list.txt") || FileUtil.size(new File("..\\list.txt")) == 0) {
                    System.out.println("请先指定基准文件夹");
                    break;
                } else {
                    List<String> list = FileUtil.readUtf8Lines("list.txt");
                    int number = list.size();
                    list.add(number + "=" + fSrc);
                    FileUtil.writeUtf8Lines(list, "list.txt");
                }
                break;
            } else {
                System.out.println("路径错误,请重新输入");
            }
        }

    }

    public void initPluginsFile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入基准文件夹的路径,如xxx\\plugins");
        String fSrc;
        while (true) {
            fSrc = sc.nextLine();
            String[] arr = fSrc.split("\\\\");
            if ("plugins".equals(arr[arr.length - 1])) {
                StringJoiner sj = new StringJoiner("\\", "", "");
                for (String s : arr) {
                    sj.add(s);
                }
                fSrc = sj.toString();

                if (!FileUtil.exist("list.txt") || FileUtil.size(new File("..\\list.txt")) == 0) {
                    FileUtil.writeUtf8String("0=" + fSrc, "list.txt");
                } else {
                    List<String> list = FileUtil.readUtf8Lines("list.txt");
                    for (int i = 0; i < list.size(); i++) {
                        if ("0".equals(list.get(i).split("=")[0])) {
                            list.set(i, "0=" + fSrc);
                            break;
                        }
                    }
                    FileUtil.writeUtf8Lines(list, "list.txt");
                }
                break;
            } else {
                System.out.println("路径错误,请重新输入");
            }
        }

    }
}
