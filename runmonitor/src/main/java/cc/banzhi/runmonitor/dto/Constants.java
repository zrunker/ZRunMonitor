package cc.banzhi.runmonitor.dto;

/**
 * @program: ZRunMonitor
 * @description: 常量类
 * @author: zoufengli01
 * @create: 2022/3/11 5:09 下午
 **/
public class Constants {
    // 内存监控开关KEY
    public static String IS_OPEN_MEMORY_KEY = "is_open_memory_key";

    // 主线程Handler监控开关KEY
    public static String IS_OPEN_MAIN_KEY = "is_open_main_key";

    // 主线程执行时长阀值
    public static long MAIN_HANDLER_TIME = 4000;
}
