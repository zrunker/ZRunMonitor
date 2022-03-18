package cc.banzhi.runmonitor.executor;

import android.annotation.SuppressLint;
import android.content.Context;

import cc.banzhi.runmonitor.dto.HandleEvent;

/**
 * @program: ZRunMonitor
 * @description: MonitorExecutor代理类
 * @author: zoufengli01
 * @create: 2022/3/10 4:54 下午
 **/
public class MonitorExecutorProxy implements IExecutor {
    private IExecutor iExecutor;
    @SuppressLint("StaticFieldLeak")
    private static volatile MonitorExecutorProxy instance;

    private MonitorExecutorProxy() {
    }

    public static MonitorExecutorProxy getInstance() {
        if (instance == null) {
            synchronized (MonitorExecutorProxy.class) {
                if (instance == null) {
                    instance = new MonitorExecutorProxy();
                }
            }
        }
        return instance;
    }

    public synchronized void init(Context context) {
        if (context != null) {
            this.iExecutor = new MonitorExecutor(context.getApplicationContext());
        }
    }

    @Override
    public <T> void execute(HandleEvent<T> event) {
        if (iExecutor != null) {
            iExecutor.execute(event);
        }
    }

    @Override
    public <T> void executeDelay(HandleEvent<T> event, long delayMillis) {
        if (iExecutor != null) {
            iExecutor.executeDelay(event, delayMillis);
        }
    }

    @Override
    public <T> void removeEvent(HandleEvent<T> event) {
        if (iExecutor != null) {
            iExecutor.removeEvent(event);
        }
    }
}
