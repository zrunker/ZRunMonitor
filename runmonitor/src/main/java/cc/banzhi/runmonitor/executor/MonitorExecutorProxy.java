package cc.banzhi.runmonitor.executor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;

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
    public void run(Message msg) {
        if (iExecutor != null) {
            iExecutor.run(msg);
        }
    }

    @Override
    public void runDelay(Message msg, long delayMillis) {
        if (iExecutor != null) {
            iExecutor.runDelay(msg, delayMillis);
        }
    }
}
