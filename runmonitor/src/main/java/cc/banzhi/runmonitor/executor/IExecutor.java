package cc.banzhi.runmonitor.executor;

import android.os.Message;

/**
 * @program: ZRunMonitor
 * @description: 分发接口
 * @author: zoufengli01
 * @create: 2022/3/10 4:54 下午
 **/
public interface IExecutor {
    void run(Message msg);

    void runDelay(Message msg, long delayMillis);
}