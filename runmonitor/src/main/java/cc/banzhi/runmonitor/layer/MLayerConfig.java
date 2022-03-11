package cc.banzhi.runmonitor.layer;

/**
 * @program: AIChinese
 * @description: 浮层属性类
 * @author: zoufengli01
 * @create: 2021-10-12 10:41
 **/
public class MLayerConfig {
    /*浮层容器显示区域*/
    @MLayerRegion
    public String region = MLayerRegion.SCREEN_FULL;
    /*浮层容器X轴开始坐标*/
    public int startX;
    /*浮层容器Y轴开始坐标*/
    public int startY;
    /*浮层容器宽度*/
    public int width;
    /*浮层容器高度*/
    public int height;
}