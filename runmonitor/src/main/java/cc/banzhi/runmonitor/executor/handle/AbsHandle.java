package cc.banzhi.runmonitor.executor.handle;

import android.content.Context;

import cc.banzhi.runmonitor.layer.MonitorLayer;

/**
 * @program: ZRunMonitor
 * @description:
 * @author: zoufengli01
 * @create: 2022/3/11 11:38 上午
 **/
public abstract class AbsHandle {

    public abstract <T> void handle(Context context, MonitorLayer layer, T data);
}
