package cc.banzhi.runmonitor.executor.handle;

import android.content.Context;
import android.os.Handler;

import cc.banzhi.runmonitor.dto.Constants;
import cc.banzhi.runmonitor.dto.MemoryBean;
import cc.banzhi.runmonitor.layer.MonitorLayer;
import cc.banzhi.runmonitor.layer.view.MemoryView;
import cc.banzhi.runmonitor.utils.SpUtil;

/**
 * @program: ZRunMonitor
 * @description: 内存处理类
 * @author: zoufengli01
 * @create: 2022/3/11 11:34 上午
 **/
public class MemoryHandle extends AbsHandle {
    private MemoryView mMemoryView;

    public MemoryHandle(Context context, MonitorLayer layer, Handler mainHandler) {
        super(context, layer, mainHandler);
    }

    @Override
    public <T> void handle(T data) {
        if (mContext == null || data == null) {
            return;
        }
        Runnable runnable = () -> {
            if (SpUtil.getBoolean(mContext, Constants.IS_OPEN_MEMORY_KEY, true)) {
                if (mMemoryView == null) {
                    mMemoryView = new MemoryView(mContext);
                    mMemoryView.setListener(new MemoryView.ClickListener() {
                        @Override
                        public void onClose() {
                            if (mLayer != null) {
                                mLayer.hide();
                            }
                            SpUtil.putBoolean(mContext, Constants.IS_OPEN_MEMORY_KEY, false);
                        }
                    });
                }
                if (data instanceof MemoryBean) {
                    mMemoryView.onBind((MemoryBean) data);
                }
                if (mLayer != null) {
                    mLayer.addView(mMemoryView);
                    mLayer.show();
                }
            } else {
                if (mLayer != null) {
                    mLayer.hide();
                }
            }
        };
        mMainHandler.post(runnable);
    }
}
