package cc.banzhi.runmonitor.layer;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * @program: AIChinese
 * @description: 浮层对外接口类
 * @author: zoufengli01
 * @create: 2021-10-12 11:09
 **/
public interface IMonitorLayer extends IMonitorLayerConfig {

    /**
     * 展示
     */
    void show();

    /**
     * 隐藏
     */
    void hide();

    /**
     * 添加View
     *
     * @param view 待添加View
     */
    void addView(@NonNull View view);

    /**
     * 移除View
     *
     * @param view 待处理View
     */
    void removeView(@NonNull View view);

    /**
     * 销毁
     */
    void destroy();

    /**
     * 清屏
     */
    void clear();

}
