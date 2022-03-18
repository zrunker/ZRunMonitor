package cc.banzhi.runmonitor.monitor.main;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Printer;

import cc.banzhi.runmonitor.dto.Constants;
import cc.banzhi.runmonitor.dto.HandleEvent;
import cc.banzhi.runmonitor.executor.MonitorExecutorProxy;
import cc.banzhi.runmonitor.monitor.IMonitor;
import cc.banzhi.runmonitor.monitor.MonitorType;
import cc.banzhi.runmonitor.utils.SpUtil;

import static cc.banzhi.runmonitor.dto.Constants.MAIN_HANDLER_TIME;

/**
 * @program: ZRunMonitor
 * @description: 主线程Handler执行时长监视器
 * @author: zoufengli01
 * @create: 2022/3/18 6:03 下午
 **/
public class MainMonitor implements IMonitor {

    @Override
    public void monitor(Context context) {
        if (SpUtil.getBoolean(context, Constants.IS_OPEN_MAIN_KEY, true)) {
            HandleEvent<Void> event = new HandleEvent<>(MonitorType.MAIN);
            Looper.getMainLooper().setMessageLogging(new Printer() {
                @Override
                public void println(String x) {
                    if (TextUtils.isEmpty(x)) {
                        return;
                    }
                    if (x.startsWith(">>>>> Dispatching to")) {
                        // 开始执行
                        MonitorExecutorProxy.getInstance().executeDelay(event, MAIN_HANDLER_TIME);
                    }
                    if (x.startsWith("<<<<< Finished to")) {
                        // 执行结束
                        MonitorExecutorProxy.getInstance().removeEvent(event);
                    }
                }
            });
        }
    }
}
