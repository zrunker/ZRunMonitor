package cc.banzhi.runmonitor.executor.handle;

import android.content.Context;
import android.os.Handler;

import cc.banzhi.runmonitor.layer.MonitorLayer;

/**
 * @program: ZRunMonitor
 * @description:
 * @author: zoufengli01
 * @create: 2022/3/11 11:38 上午
 **/
public abstract class AbsHandle {
    protected Context mContext;
    protected MonitorLayer mLayer;
    protected Handler mMainHandler;

    public AbsHandle(Context context, MonitorLayer layer, Handler mainHandler) {
        this.mContext = context;
        this.mLayer = layer;
        this.mMainHandler = mainHandler;
    }

    public abstract <T> void handle(T data);
}
