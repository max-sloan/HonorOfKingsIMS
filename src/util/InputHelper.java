package util;

import java.util.Scanner;

/**
 * InputHelper —— 控制台输入帮助类
 *
 * 封装了 Scanner，提供带校验的输入方法。
 * 所有方法都是 static 的，可以直接用 InputHelper.readInt() 调用，
 * 不需要 new InputHelper()。
 */
public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 读取一个整数，如果输入不合法就提示重新输入
     * @param prompt 提示文字
     * @return 用户输入的整数
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[错误] 请输入一个有效的整数！");
            }
        }
    }

    /**
     * 读取一个整数，允许为空（按回车跳过）
     * @param prompt 提示文字
     * @param defaultValue 默认值
     * @return 用户输入的整数或默认值
     */
    public static int readIntOrDefault(String prompt, int defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("[提示] 输入格式错误，使用默认值: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * 读取一个非空字符串
     * @param prompt 提示文字
     * @return 用户输入的字符串
     */
    public static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[错误] 输入不能为空，请重新输入！");
        }
    }

    /**
     * 读取一行字符串（允许为空）
     */
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * 读取一个在指定范围内的整数
     * @param prompt 提示文字
     * @param min 最小值
     * @param max 最大值
     * @return 范围内的整数
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("[错误] 请输入 " + min + " ~ " + max + " 之间的数字！");
        }
    }

    /**
     * 等待用户按回车继续
     */
    public static void waitForEnter() {
        System.out.print("\n按回车键继续...");
        scanner.nextLine();
    }
}
