package cc.banzhi.runmonitor.layer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Shader;
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
 * @description: 折线图
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
    /*线条画笔*/
    private Paint mLinePaint;
    /*线条路径*/
    private Path mLinePath;
    /*阴影画笔*/
    private Paint mShaderPaint;
    /*阴影路径*/
    private Path mShaderPath;
    /*圆点画笔*/
    private Paint mCirclePaint;

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

        // 线条画笔
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(0xFF40AFF2);

        // 阴影画笔
        mShaderPaint = new Paint();
        mShaderPaint.setAntiAlias(true);

        // 圆点画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(10);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setColor(0xFF40AFF2);

        mLinePath = new Path();
        mShaderPath = new Path();
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

                    drawShader(cList);
                    drawLine(cList);
                    drawCircle(cList);
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

    // 绘制圆点
    private void drawCircle(List<PolylineBean> list) {
        Iterator<PolylineBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            PolylineBean item = iterator.next();
            if (item == null) {
                iterator.remove();
            } else {
                mCanvas.drawCircle(item.x, item.y, 10, mCirclePaint);
            }
        }
    }

    // 画折线图
    private void drawLine(List<PolylineBean> list) {
        mLinePath.reset();
        PolylineBean firstItem = list.get(0);
        mLinePath.moveTo(firstItem.x, firstItem.y);
        Iterator<PolylineBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            PolylineBean item = iterator.next();
            if (item == null) {
                iterator.remove();
            } else {
                mLinePath.lineTo(item.x, item.y);
            }
        }
        mCanvas.drawPath(mLinePath, mLinePaint);
    }

    // 画阴影
    private void drawShader(List<PolylineBean> list) {
        mShaderPath.reset();
        PolylineBean firstItem = list.get(0);
        mShaderPath.moveTo(firstItem.x, getMeasuredHeight());
        Iterator<PolylineBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            PolylineBean item = iterator.next();
            if (item == null) {
                iterator.remove();
            } else {
                mShaderPath.lineTo(item.x, item.y);
            }
        }
        mShaderPath.lineTo(list.get(list.size() - 1).x, getMeasuredHeight());
        mShaderPath.lineTo(firstItem.x, getMeasuredHeight());
        mShaderPath.close();

        mShaderPaint.setStyle(Paint.Style.FILL);
        mShaderPaint.setShader(getShader());
        mCanvas.drawPath(mShaderPath, mShaderPaint);
    }

    // 获取Shader
    private Shader getShader() {
        int[] shadeColors = new int[]{
                Color.argb(150, 32, 208, 88),
                Color.argb(100, 234, 115, 9),
                Color.argb(200, 250, 49, 33)
        };
        return new LinearGradient(
                getMeasuredWidth() / 2.0f,
                getMeasuredHeight(),
                getMeasuredWidth() / 2.0f,
                0f,
                shadeColors,
                null,
                Shader.TileMode.REPEAT);
    }

    // 保存坐标集合
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
