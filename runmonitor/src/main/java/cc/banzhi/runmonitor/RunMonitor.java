package cc.banzhi.runmonitor;

import android.content.Context;

import cc.banzhi.runmonitor.executor.MonitorExecutorProxy;

/**
 * @program: ZRunMonitor
 * @description: 监视器
 * @author: zoufengli01
 * @create: 2022/3/10 3:43 下午
 **/
public class RunMonitor {
    /**
     * 初始化
     *
     * @param context 上下文对象
     */
    public static void init(Context context) {
        if (context != null) {
            MonitorExecutorProxy.getInstance().init(context);
        }
    }

}
