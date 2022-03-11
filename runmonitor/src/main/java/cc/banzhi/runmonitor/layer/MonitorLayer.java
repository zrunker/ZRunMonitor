package cc.banzhi.runmonitor.layer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

/**
 * @program: AIChinese
 * @description: 浮层管理类
 * @author: zoufengli01
 * @create: 2021-10-12 10:41
 **/
public class MonitorLayer implements IMonitorLayer {
    private Context mContext;
    private WindowManager wm;
    private WindowManager.LayoutParams wParams;
    private FrameLayout fLayout;
    private MLayerConfig mLayerConfig;
    /*子View的LayoutParams*/
    private ViewGroup.MarginLayoutParams cLayoutParams;
    /*屏幕高度*/
    private int screenHeight;

    public MonitorLayer(Context context) {
        if (context != null) {
            this.mContext = context;
            init();
        }
    }

    private void init() {
        if (mLayerConfig == null) {
            mLayerConfig = new MLayerConfig();
        }
        if (wm == null) {
            wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wParams == null) {
            wParams = new WindowManager.LayoutParams();
            // 设置type
//            wParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                wParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                wParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            // 设置flags
            wParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            wParams.gravity = Gravity.START | Gravity.TOP;
            // 背景设置成透明
            wParams.format = PixelFormat.TRANSPARENT;
        }
        initView();
    }

    /**
     * 初始化父View
     */
    private void initView() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.wm.getDefaultDisplay().getMetrics(outMetrics);
        this.screenHeight = outMetrics.heightPixels;

        this.mLayerConfig.height = screenHeight;
        this.mLayerConfig.width = outMetrics.widthPixels;
        this.mLayerConfig.startX = 0;
        this.mLayerConfig.startY = 0;

        this.wParams.x = mLayerConfig.startX;
        this.wParams.y = mLayerConfig.startY;
        this.wParams.width = mLayerConfig.width;
        this.wParams.height = mLayerConfig.height / 3;
        this.wParams.horizontalMargin = 0;
        this.wParams.verticalMargin = 0;

        this.fLayout = new FrameLayout(mContext);
        this.fLayout.setBackgroundColor(Color.parseColor("#AA000000"));

        show();
    }

    /**
     * 展示
     */
    @Override
    public void show() {
        if (fLayout.getParent() == null) {
            MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    wm.addView(fLayout, wParams);
                    return false;
                }
            };
            Looper.myQueue().addIdleHandler(idleHandler);
        }
    }

    /**
     * 隐藏
     */
    @Override
    public void hide() {
        if (fLayout.getParent() != null) {
            this.wm.removeView(fLayout);
        }
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        this.clear();
        this.hide();
    }

    /**
     * 清屏
     */
    @Override
    public void clear() {
        this.fLayout.removeAllViews();
    }

    /**
     * 设置浮层位置
     *
     * @param sX X轴开始位置
     * @param sY Y轴开始位置
     */
    @Override
    public void location(int sX, int sY) {
        this.mLayerConfig.startX = sX;
        this.mLayerConfig.startY = sY;
        this.wParams.x = mLayerConfig.startX;
        this.wParams.y = mLayerConfig.startY;
        this.wm.updateViewLayout(fLayout, wParams);
    }

    /**
     * 设置浮层大小
     *
     * @param w 宽
     * @param h 高
     */
    @Override
    public void size(int w, int h) {
        if (w < 0 || h < 0) {
            return;
        }
        this.mLayerConfig.width = w;
        this.mLayerConfig.height = h;
        this.wParams.width = mLayerConfig.width;
        this.wParams.height = mLayerConfig.height;
        this.wm.updateViewLayout(fLayout, wParams);
    }

    /**
     * 设置显示区域
     */
    @Override
    public void region(@NonNull @MLayerRegion String region) {
        this.mLayerConfig.region = region;
        switch (this.mLayerConfig.region) {
            case MLayerRegion.SCREEN_FULL:
                this.wParams.height = screenHeight;
                this.wParams.verticalMargin = 0;
                break;
            case MLayerRegion.SCREEN_CENTER:
                this.wParams.verticalMargin = 0;
                this.wParams.height = screenHeight / 3;
                this.wParams.gravity = Gravity.CENTER;
                break;
            case MLayerRegion.SCREEN_TOP:
                this.wParams.verticalMargin = 0;
                this.wParams.height = screenHeight / 3;
                this.wParams.gravity = Gravity.TOP;
                break;
            case MLayerRegion.SCREEN_BOTTOM:
                this.wParams.verticalMargin = 0;
                this.wParams.height = screenHeight / 3;
                this.wParams.gravity = Gravity.BOTTOM;
                break;
        }
        this.wm.updateViewLayout(fLayout, wParams);
    }

    /**
     * 添加View
     *
     * @param view 待处理View
     */
    @Override
    public void addView(@NonNull View view) {
        if (view.getParent() != fLayout) {
            removeView(view);
        }
        if (view.getParent() == fLayout) {
            return;
        }
        // 设置LayoutParams
        if (cLayoutParams == null) {
            cLayoutParams = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        this.fLayout.addView(view, cLayoutParams);
    }

    /**
     * 移除View
     *
     * @param view 待处理View
     */
    @Override
    public void removeView(@NonNull View view) {
        if (view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
    }
}
