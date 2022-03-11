package cc.banzhi.runmonitor.monitor.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;

import androidx.annotation.RequiresApi;

import cc.banzhi.runmonitor.dto.MemoryBean;

/**
 * @program: ZRunMonitor
 * @description: 内存管理类
 * @author: zoufengli01
 * @create: 2022/3/10 5:50 下午
 **/
public class MemoryUtil {
    /**
     * 获取系统内存信息
     */
    public static MemoryBean getMemoryInfo(Context context) {
        if (context != null) {
            MemoryBean memoryBean = new MemoryBean();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // 获取系统内容
            ActivityManager.MemoryInfo sysMemInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(sysMemInfo);

            // 系统可用内存
            memoryBean.sysAvailMem = sysMemInfo.availMem;
            // 系统是否处于低内存运行
            memoryBean.sysLowMemory = sysMemInfo.lowMemory;
            // 当系统剩余内存低于memInfo.threshold时就是低内存运行，系统将开始kill后台服务和进程
            memoryBean.sysThreshold = sysMemInfo.threshold;
            // 系统内存的总大小
            memoryBean.sysTotalMem = sysMemInfo.totalMem;
            // 系统已分配内存
            memoryBean.sysAllocatedMem = sysMemInfo.totalMem - sysMemInfo.availMem;

            // app可使用的最大memory size，即getprop中的heapgrowthlimit
            memoryBean.maxMemory = activityManager.getMemoryClass() * 1024 * 1024;
            // manifest中设置了largeHeap=true后，app可使用的最大memory size，即getprop中的heapsize
            memoryBean.largeMaxMemory = activityManager.getLargeMemoryClass() * 1024 * 1024;

            // 获取APP内存
            getRuntimeMem(memoryBean);

            // 获取APP内存明细
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getAppSummary(activityManager, memoryBean);
            }

            memoryBean.formatMem = memoryBean.toString();
            return memoryBean;
        }
        return null;
    }

    /**
     * 读取内存信息 - 跟Android Profiler、dumpsys meminfo结论一致
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static MemoryBean getAppSummary(ActivityManager activityManager, MemoryBean memoryBean) {
        Debug.MemoryInfo[] memInfos = activityManager.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});
        if (memInfos.length > 0) {
            Debug.MemoryInfo dMemInfo = memInfos[0];

            String javaMem = dMemInfo.getMemoryStat("summary.java-heap");
            memoryBean.javaMem = Long.parseLong(javaMem);

            String nativeMem = dMemInfo.getMemoryStat("summary.native-heap");
            memoryBean.nativeMem = Long.parseLong(nativeMem);

            String codeMem = dMemInfo.getMemoryStat("summary.code");
            memoryBean.codeMem = Long.parseLong(codeMem);

            String stackMem = dMemInfo.getMemoryStat("summary.stack");
            memoryBean.stackMem = Long.parseLong(stackMem);

            String graphicsMem = dMemInfo.getMemoryStat("summary.graphics");
            memoryBean.graphicsMem = Long.parseLong(graphicsMem);

            String privateOtherMem = dMemInfo.getMemoryStat("summary.private-other");
            memoryBean.privateOtherMem = Long.parseLong(privateOtherMem);

            String systemMem = dMemInfo.getMemoryStat("summary.system");
            memoryBean.systemMem = Long.parseLong(systemMem);

            String totalPssMem = dMemInfo.getMemoryStat("summary.total-pss");
            memoryBean.totalPssMem = Long.parseLong(totalPssMem);

            String totalSwapMem = dMemInfo.getMemoryStat("summary.total-swap");
            memoryBean.totalSwapMem = Long.parseLong(totalSwapMem);

            memoryBean.pssMem = Long.parseLong(javaMem)
                    + Long.parseLong(nativeMem)
                    + Long.parseLong(graphicsMem)
                    + Long.parseLong(stackMem)
                    + Long.parseLong(codeMem)
                    + Long.parseLong(privateOtherMem)
                    + Long.parseLong(systemMem);
        }
        return memoryBean;
    }

    /**
     * 获取当前应用程序内存信息
     */
    private static MemoryBean getRuntimeMem(MemoryBean memoryBean) {
        // 应用程序最大可用内存，即getprop中的heapgrowthlimit
        memoryBean.appMaxMem = Runtime.getRuntime().maxMemory();
        // 应用程序已获得的总内存
        memoryBean.appTotalMem = Runtime.getRuntime().totalMemory();
        // 应用程序已获得内存中未使用内存
        memoryBean.appFreeMem = Runtime.getRuntime().freeMemory();
        memoryBean.appAllocatedMem = memoryBean.appTotalMem - memoryBean.appFreeMem;
        return memoryBean;
    }
}
