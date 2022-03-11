package cc.banzhi.runmonitor.executor.handle;

import android.content.Context;

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

    @Override
    public <T> void handle(Context context, MonitorLayer layer, T data) {
        if (context != null) {
            if (SpUtil.getBoolean(context, Constants.IS_OPEN_MEMORY_KEY, true)) {
                if (mMemoryView == null) {
                    mMemoryView = new MemoryView(context);
                    mMemoryView.setListener(new MemoryView.ClickListener() {
                        @Override
                        public void onClose() {
                            if (layer != null) {
                                layer.hide();
                            }
                            SpUtil.putBoolean(context, Constants.IS_OPEN_MEMORY_KEY, false);
                        }
                    });
                }
                if (data instanceof MemoryBean) {
                    mMemoryView.onBind((MemoryBean) data);
                }
                if (layer != null) {
                    layer.addView(mMemoryView);
                    layer.show();
                }
            } else {
                if (layer != null) {
                    layer.hide();
                }
            }
        }
    }

}
