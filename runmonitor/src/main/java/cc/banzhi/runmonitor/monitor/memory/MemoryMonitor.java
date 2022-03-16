package cc.banzhi.runmonitor.monitor.memory;

import android.content.Context;

import cc.banzhi.runmonitor.dto.Constants;
import cc.banzhi.runmonitor.dto.HandleEvent;
import cc.banzhi.runmonitor.dto.MemoryBean;
import cc.banzhi.runmonitor.executor.MonitorExecutorProxy;
import cc.banzhi.runmonitor.executor.ThreadType;
import cc.banzhi.runmonitor.monitor.IMonitor;
import cc.banzhi.runmonitor.monitor.IMonitorCallBack;
import cc.banzhi.runmonitor.monitor.MonitorType;
import cc.banzhi.runmonitor.utils.SpUtil;

/**
 * @program: ZRunMonitor
 * @description: 内存监视器
 * @author: zoufengli01
 * @create: 2022/3/10 3:42 下午
 **/
public class MemoryMonitor implements IMonitor {

    @Override
    public void monitor(Context context) {
        if (context != null &&
                SpUtil.getBoolean(context, Constants.IS_OPEN_MEMORY_KEY, true)) {
            HandleEvent<MemoryBean> event = new HandleEvent<>(
                    MonitorType.MEMORY,
                    ThreadType.MAIN,
                    MemoryUtil.getMemoryInfo(context),
                    new IMonitorCallBack() {
                        @Override
                        public void onBack(int monitorType) {
                            if (monitorType == MonitorType.MEMORY) {
                                monitor(context);
                            }
                        }
                    }
            );
            MonitorExecutorProxy.getInstance().executeDelay(event, 1000);
        }
    }

}
