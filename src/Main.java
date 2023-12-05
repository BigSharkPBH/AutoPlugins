import com.dsy.function.CopyFile;
import com.dsy.function.PluginsList;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("----------------欢迎使用插件自动同步工具---------------");
            System.out.println("1.设置");
            System.out.println("2.同步插件");
            System.out.println("3.退出");
            System.out.println("请输入对应的数字");
            System.out.println("---------------------[作者:大鲨鱼]--------------------");
            Scanner sc = new Scanner(System.in);
            int number=0;
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
                    new PluginsList();
                }
                case 2 -> {
                    new CopyFile();
                }
                case 3 -> {
                    System.exit(0);
                }
                default -> System.out.println("请输入正确的数字");
            }
        }
    }
}