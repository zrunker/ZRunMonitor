package cc.banzhi.runmonitor.monitor.memory;

import android.content.Context;
import android.os.Message;

import cc.banzhi.runmonitor.dto.Constants;
import cc.banzhi.runmonitor.dto.MessageResult;
import cc.banzhi.runmonitor.executor.MonitorExecutorProxy;
import cc.banzhi.runmonitor.monitor.IMonitor;
import cc.banzhi.runmonitor.monitor.IMonitorCallBack;
import cc.banzhi.runmonitor.type.MonitorType;
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
            Message message = Message.obtain();
            message.what = MonitorType.MEMORY;
            message.obj = new MessageResult<>(MemoryUtil.getMemoryInfo(context), new IMonitorCallBack() {
                @Override
                public void onBack(int type) {
                    if (type == MonitorType.MEMORY) {
                        monitor(context);
                    }
                }
            });
            MonitorExecutorProxy.getInstance().runDelay(message, 1000);
        }
    }

}
