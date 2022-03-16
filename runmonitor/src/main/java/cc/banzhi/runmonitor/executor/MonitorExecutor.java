package cc.banzhi.runmonitor.executor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

import cc.banzhi.runmonitor.MonitorMap;
import cc.banzhi.runmonitor.dto.HandleEvent;
import cc.banzhi.runmonitor.executor.handle.AbsHandle;
import cc.banzhi.runmonitor.executor.handle.HandleFactory;
import cc.banzhi.runmonitor.layer.MonitorLayer;
import cc.banzhi.runmonitor.monitor.IMonitor;
import cc.banzhi.runmonitor.utils.SpUtil;

/**
 * @program: ZRunMonitor
 * @description: 监控分发器
 * @author: zoufengli01
 * @create: 2022/3/10 4:54 下午
 **/
public class MonitorExecutor extends Thread implements IExecutor {
    private Context mContext;
    private Handler mThreadHandler;
    private MonitorLayer mLayer;
    private Handler mMainHandler;

    public MonitorExecutor(Context context) {
        if (context != null) {
            Log.i("MonitorExecutor", "监视器初始化！");
            this.mContext = context.getApplicationContext();
            this.mLayer = new MonitorLayer(mContext);
            // TODO 测试
            SpUtil.clear(mContext);
            setName("RunMonitor");
            start();
        }
    }

    /**
     * 执行消息
     *
     * @param event 待执行消息
     */
    @Override
    public <T> void execute(HandleEvent<T> event) {
        executeDelay(event, 0);
    }

    /**
     * 延迟执行消息
     *
     * @param event       待执行消息
     * @param delayMillis 延迟时长
     */
    @Override
    public <T> void executeDelay(HandleEvent<T> event, long delayMillis) {
        if (!isAlive() || isInterrupted()) {
            start();
        }
        if (mThreadHandler != null) {
            mThreadHandler.sendMessageDelayed(createMessage(event), delayMillis);
        } else {
            Log.e("MonitorExecutor", "监视器未启动，或启动失败！");
            Toast.makeText(mContext, "启动监视器失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private <T> Message createMessage(HandleEvent<T> event) {
        Message msg = Message.obtain();
        if (event != null) {
            msg.obj = event;
        }
        return msg;
    }

    @Override
    public void run() {
        Log.i("MonitorExecutor", "监视器已启动！");
        Looper.prepare();
        initHandler();
        Looper.loop();
    }

    /**
     * 初始化Handler
     */
    private void initHandler() {
        if (mThreadHandler == null) {
            mThreadHandler = new Handler(Looper.myLooper()) {
                public void handleMessage(Message msg) {
                    handle(msg);
                }
            };
            Log.i("MonitorExecutor", "执行全部监视功能！");
            startMonitor();
        }
    }

    /**
     * 启动所有监视器
     */
    private void startMonitor() {
        Set<Map.Entry<Integer, MonitorMap.Item>> set = MonitorMap.map.entrySet();
        for (Map.Entry<Integer, MonitorMap.Item> entry : set) {
            if (entry != null && entry.getValue() != null) {
                IMonitor iMonitor = entry.getValue().monitor;
                if (iMonitor != null) {
                    iMonitor.monitor(mContext);
                }
            }
        }
    }

    /**
     * 执行消息
     *
     * @param msg 待执行消息
     */
    private void handle(Message msg) {
        if (msg != null && msg.obj != null) {
            HandleEvent<?> result = (HandleEvent<?>) msg.obj;
            AbsHandle absHandle = HandleFactory.getInstance().get(result.monitorType);
            if (absHandle != null) {
                int threadType = result.threadType;
                if (threadType == ThreadType.MAIN) {
                    executeMainTask(() -> absHandle.handle(mContext, mLayer, result.data));
                } else {
                    absHandle.handle(mContext, mLayer, result.data);
                }
            }
            if (result.callBack != null) {
                result.callBack.onBack(result.monitorType);
            }
        }
    }

    /**
     * 主线程执行任务
     *
     * @param runnable 待执行任务
     */
    private void executeMainTask(Runnable runnable) {
        if (runnable != null) {
            if (this.mMainHandler == null) {
                this.mMainHandler = new Handler(Looper.getMainLooper());
            }
            this.mMainHandler.post(runnable);
        }
    }
}