package cc.banzhi.runmonitor.dto;

import cc.banzhi.runmonitor.monitor.IMonitorCallBack;

/**
 * @program: ZRunMonitor
 * @description: 包装类
 * @author: zoufengli01
 * @create: 2022/3/10 6:55 下午
 **/
public class MessageResult<T> {
    public T data;
    public IMonitorCallBack callBack;

    public MessageResult(T data, IMonitorCallBack callBack) {
        this.data = data;
        this.callBack = callBack;
    }

    @Override
    public String toString() {
        return "MessageResult{" +
                "data=" + data +
                ", callBack=" + callBack +
                '}';
    }
}
