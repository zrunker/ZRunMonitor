package cc.banzhi.runmonitor.layer;

import androidx.annotation.StringDef;

/**
 * @program: AIChinese
 * @description: 显示区域
 * @author: zoufengli01
 * @create: 2021-10-12 10:41
 **/
@StringDef({MLayerRegion.SCREEN_CENTER, MLayerRegion.SCREEN_FULL,
        MLayerRegion.SCREEN_TOP, MLayerRegion.SCREEN_BOTTOM})
public @interface MLayerRegion {
    /*全屏*/
    String SCREEN_FULL = "screen_full";
    /*屏幕中间*/
    String SCREEN_CENTER = "screen_center";
    /*屏幕顶部*/
    String SCREEN_TOP = "screen_top";
    /*屏幕底部*/
    String SCREEN_BOTTOM = "screen_bottom";
}
