package cc.banzhi.runmonitor.executor.handle;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import cc.banzhi.runmonitor.layer.MonitorLayer;

import static cc.banzhi.runmonitor.dto.Constants.MAIN_HANDLER_TIME;

/**
 * @program: ZRunMonitor
 * @description: 主线程Handler执行时长处理器
 * @author: zoufengli01
 * @create: 2022/3/18 6:32 下午
 **/
public class MainHandle extends AbsHandle {

    public MainHandle(Context context, MonitorLayer layer, Handler mainHandler) {
        super(context, layer, mainHandler);
    }

    @Override
    public <T> void handle(T data) {
        String info = "执行时间 > " + MAIN_HANDLER_TIME + "\n" + stackTrace();
        Log.e("MainHandle", info);
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mContext != null) {
                    Toast.makeText(mContext, "主线程事件执行时长超过" + MAIN_HANDLER_TIME
                            + "ms，警惕ANR错误，详细信息查看MainHandle日志！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String stackTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        for (StackTraceElement s : stackTrace) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }
}
