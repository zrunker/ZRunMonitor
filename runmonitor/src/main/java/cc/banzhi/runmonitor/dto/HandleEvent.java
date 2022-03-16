package cc.banzhi.runmonitor.dto;

import cc.banzhi.runmonitor.executor.ThreadType;
import cc.banzhi.runmonitor.monitor.IMonitorCallBack;
import cc.banzhi.runmonitor.monitor.MonitorType;

/**
 * @program: ZRunMonitor
 * @description: 事件包装类
 * @author: zoufengli01
 * @create: 2022/3/10 6:55 下午
 **/
public class HandleEvent<T> {
    @MonitorType
    public int monitorType;
    @ThreadType
    public int threadType = ThreadType.THREAD;
    public T data;
    public IMonitorCallBack callBack;

    public HandleEvent(int monitorType, int threadType, T data, IMonitorCallBack callBack) {
        this.monitorType = monitorType;
        this.threadType = threadType;
        this.data = data;
        this.callBack = callBack;
    }

    public HandleEvent(int monitorType, T data, IMonitorCallBack callBack) {
        this.monitorType = monitorType;
        this.data = data;
        this.callBack = callBack;
    }

    public HandleEvent(int monitorType, T data) {
        this.monitorType = monitorType;
        this.data = data;
    }

    public HandleEvent(int monitorType) {
        this.monitorType = monitorType;
    }

    @Override
    public String toString() {
        return "HandleEvent{" +
                "monitorType=" + monitorType +
                ", threadType=" + threadType +
                ", data=" + data +
                ", callBack=" + callBack +
                '}';
    }
}
