package cc.banzhi.runmonitor.monitor;

import androidx.annotation.IntDef;

/**
 * @program: ZRunMonitor
 * @description: 监控类型
 * @author: zoufengli01
 * @create: 2022/3/10 6:40 下午
 **/
@IntDef({MonitorType.MEMORY})
public @interface MonitorType {
    int MEMORY = 1;
}
