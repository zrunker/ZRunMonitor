package cc.banzhi.runmonitor.layer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @program: ZRunMonitor
 * @description:
 * @author: zoufengli01
 * @create: 2022/3/11 6:23 下午
 **/
public class PolylineView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {
    /*SurfaceHolder实例*/
    private SurfaceHolder sHolder;
    /*标记是否能够绘制*/
    private boolean isCanDraw;
    /*Surface画布*/
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mCirclePaint;
    private Path mPath;

    /*记录上一次绘制时间 ms*/
    private long preDrawTime;

    public PolylineView(Context context) {
        this(context, null);
    }

    public PolylineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolylineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setZOrderOnTop(true);
        // 获取SurfaceHolder
        sHolder = getHolder();
        sHolder.setFormat(PixelFormat.TRANSPARENT);
        sHolder.addCallback(this);
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(0xFF40AFF2);
        // 画圆点
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setColor(0xFFFFFFFF);

        mPath = new Path();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isCanDraw = true;
        // 开启常驻子线程
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder,
                               int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isCanDraw = false;
    }

    @Override
    public void run() {
        while (isCanDraw) {
            long currentTime = SystemClock.currentThreadTimeMillis();
            if (currentTime - preDrawTime > 1000) {
                preDrawTime = currentTime;
                draw();
            }
        }
    }

    private synchronized void draw() {
        try {
            mCanvas = sHolder.lockCanvas();
            if (null != mCanvas) {
                List<PolylineBean> cList = new CopyOnWriteArrayList<>(list);
                if (cList.size() > 0) {
                    Collections.reverse(cList);
                    // 清屏
                    mCanvas.drawColor(PixelFormat.TRANSPARENT, PorterDuff.Mode.CLEAR);

                    // 画折线图
                    mPath.reset();
                    PolylineBean firstItem = cList.get(0);
                    mPath.moveTo(firstItem.x, firstItem.y);
                    // 处理集合
                    Iterator<PolylineBean> iterator = cList.iterator();
                    while (iterator.hasNext()) {
                        PolylineBean item = iterator.next();
                        if (item == null) {
                            iterator.remove();
                        } else {
                            mPath.lineTo(item.x, item.y);
                            // 绘制圆点
                            mCanvas.drawCircle(item.x, item.y, 5, mCirclePaint);
                        }
                    }
                    mCanvas.drawPath(mPath, mPaint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("PolylineView：", e.getMessage() + "");
        } finally {
            if (mCanvas != null) {
                sHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    // 保存地址集合
    private final List<PolylineBean> list = new CopyOnWriteArrayList<>();

    public synchronized void addPolyline(PolylineBean data) {
        // 分段数目
        int sectionNum = 10;
        if (list.size() > sectionNum) {
            list.remove(0);
        }
        list.add(data);
        int width = getMeasuredWidth();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            PolylineBean item = list.get(i);
            item.x = (int) (width * (i * 1.0f / sectionNum));
        }
    }

    public synchronized void addPolyline(float yRatio) {
        int height = getMeasuredHeight();
        if (height > 0) {
            float y = height * (1.0f - yRatio);
            PolylineBean data = new PolylineBean();
            data.y = y;
            addPolyline(data);
        }
    }

}
