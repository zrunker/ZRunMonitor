package cc.banzhi.runmonitor.executor;

import androidx.annotation.IntDef;

/**
 * @program: ZRunMonitor
 * @description: 线程类型
 * @author: zoufengli01
 * @create: 2022/3/16 10:07 上午
 **/
@IntDef({ThreadType.MAIN, ThreadType.THREAD})
public @interface ThreadType {
    int MAIN = 1;
    int THREAD = 2;
}
