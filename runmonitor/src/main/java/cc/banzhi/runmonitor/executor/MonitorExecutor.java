package cc.banzhi.runmonitor.executor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

import cc.banzhi.runmonitor.dto.MessageResult;
import cc.banzhi.runmonitor.executor.handle.HandleFactory;
import cc.banzhi.runmonitor.layer.MonitorLayer;
import cc.banzhi.runmonitor.monitor.IMonitor;
import cc.banzhi.runmonitor.monitor.MonitorPool;
import cc.banzhi.runmonitor.type.MonitorType;
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
            this.mMainHandler = new Handler(Looper.getMainLooper());
            // TODO 测试
            SpUtil.clear(mContext);
            setName("RunMonitor");
            start();
        }
    }

    /**
     * 启动所有监视器
     */
    private void startMonitor() {
        Map<Integer, IMonitor> map = MonitorPool.map;
        if (map != null) {
            Set<Map.Entry<Integer, IMonitor>> set = map.entrySet();
            for (Map.Entry<Integer, IMonitor> entry : set) {
                if (entry != null && entry.getValue() != null) {
                    entry.getValue().monitor(mContext);
                }
            }
        }
    }

    /**
     * 执行消息
     *
     * @param msg 待执行消息
     */
    @Override
    public void run(Message msg) {
        runDelay(msg, 0);
    }

    /**
     * 延迟执行消息
     *
     * @param msg         待执行消息
     * @param delayMillis 延迟时长
     */
    @Override
    public void runDelay(Message msg, long delayMillis) {
        if (!isAlive() || isInterrupted()) {
            start();
        }
        if (mThreadHandler != null) {
            mThreadHandler.sendMessageDelayed(msg, delayMillis);
        } else {
            Log.e("MonitorExecutor", "监视器未启动，或启动失败！");
            Toast.makeText(mContext, "启动监视器失败！", Toast.LENGTH_SHORT).show();
        }
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
     * 执行消息
     *
     * @param msg 待执行消息
     */
    private void handle(Message msg) {
        if (msg != null && msg.obj != null) {
            MessageResult<?> result = (MessageResult<?>) msg.obj;
            switch (msg.what) {
                case MonitorType.MEMORY:
                    if (result.data != null) {
                        Log.d("RunMonitor", result.data.toString());
                        // 主线程执行
                        this.mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                HandleFactory.getInstance()
                                        .get(MonitorType.MEMORY).handle(mContext, mLayer, result.data);
                            }
                        });
                    }
                    break;
            }
            if (result.callBack != null) {
                result.callBack.onBack(MonitorType.MEMORY);
            }
        }
    }
}