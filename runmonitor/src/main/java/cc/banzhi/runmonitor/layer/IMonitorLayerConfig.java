package cc.banzhi.runmonitor.layer;

import androidx.annotation.NonNull;

/**
 * @program: AIChinese
 * @description: 浮层属性接口
 * @author: zoufengli01
 * @create: 2021-10-15 10:30
 **/
public interface IMonitorLayerConfig {

    /**
     * 设置浮层位置
     *
     * @param sX X轴开始位置
     * @param sY Y轴开始位置
     */
    void location(int sX, int sY);

    /**
     * 设置浮层大小
     *
     * @param w 宽
     * @param h 高
     */
    void size(int w, int h);

    /**
     * 设置显示区域
     */
    void region(@NonNull @MLayerRegion String region);

}
