package cc.banzhi.runmonitor.monitor;

import cc.banzhi.runmonitor.type.MonitorType;

/**
 * @program: ZRunMonitor
 * @description: 分发回调
 * @author: zoufengli01
 * @create: 2022/3/10 5:26 下午
 **/
public interface IMonitorCallBack {

    void onBack(@MonitorType int type);
}
