package cc.banzhi.runmonitor.dto;

import cc.banzhi.runmonitor.utils.FileUtil;

/**
 * @program: ZRunMonitor
 * @description: 系统内存相关数据
 * @author: zoufengli01
 * @create: 2022/3/10 5:58 下午
 **/
public class MemoryBean {
    // 系统可用内存
    public long sysAvailMem;
    // 系统是否处于低内存运行
    public boolean sysLowMemory;
    // 当系统剩余内存低于memInfo.threshold时就是低内存运行，系统将开始kill后台服务和进程
    public long sysThreshold;
    // 系统内存的总大小
    public long sysTotalMem;
    // 系统已分配内存
    public long sysAllocatedMem;

    // app可使用的最大memory size，即getprop中的heapgrowthlimit
    public int maxMemory;
    // manifest中设置了largeHeap=true后，app可使用的最大memory size，即getprop中的heapsize
    public int largeMaxMemory;

    // Java
    public long javaMem;
    // Native
    public long nativeMem;
    // Code
    public long codeMem;
    // Stack
    public long stackMem;
    // Graphics
    public long graphicsMem;
    // Other
    public long privateOtherMem;
    // SYS
    public long systemMem;
    // Total
    public long totalPssMem;
    // TotalSwap
    public long totalSwapMem;
    // Pss
    public long pssMem;

    // 应用程序最大可用内存，即getprop中的heapgrowthlimit
    public long appMaxMem;
    // 应用程序已获得的总内存
    public long appTotalMem;
    // 应用程序已获得内存中未使用内存
    public long appFreeMem;
    // 应用程序已使用内存
    public long appAllocatedMem;

    public String formatMem;

    @Override
    public String toString() {
        return "内存详情：\n" +
                "\nappMaxMem：" + FileUtil.formatFileSize(appMaxMem) +
                "\nappTotalMem：" + FileUtil.formatFileSize(appTotalMem) +
                "\nappFreeMem：" + FileUtil.formatFileSize(appFreeMem) +
                "\nappAllocatedMem：" + FileUtil.formatFileSize(appAllocatedMem) +
                "\n" +
                "\njavaMem：" + FileUtil.formatFileSize(javaMem) +
                "\nnativeMem：" + FileUtil.formatFileSize(nativeMem) +
                "\ncodeMem：" + FileUtil.formatFileSize(codeMem) +
                "\nstackMem：" + FileUtil.formatFileSize(stackMem) +
                "\ngraphicsMem：" + FileUtil.formatFileSize(graphicsMem) +
                "\nprivateOtherMem：" + FileUtil.formatFileSize(privateOtherMem) +
                "\nsystemMem：" + FileUtil.formatFileSize(systemMem) +
                "\ntotalPssMem：" + FileUtil.formatFileSize(totalPssMem) +
                "\ntotalSwapMem：" + FileUtil.formatFileSize(totalSwapMem) +
                "\npssMem：" + FileUtil.formatFileSize(pssMem) +
                "\n" +
                "\nmaxMemory：" + FileUtil.formatFileSize(maxMemory) +
                "\nlargeMaxMemory：" + FileUtil.formatFileSize(largeMaxMemory) +
                "\n" +
                "\nsysLowMemory：" + sysLowMemory +
                "\nsysAvailMem：" + FileUtil.formatFileSize(sysAvailMem) +
                "\nsysThreshold：" + FileUtil.formatFileSize(sysThreshold) +
                "\nsysTotalMem：" + FileUtil.formatFileSize(sysTotalMem) +
                "\nsysAllocatedMem：" + FileUtil.formatFileSize(sysAllocatedMem);
    }
}
