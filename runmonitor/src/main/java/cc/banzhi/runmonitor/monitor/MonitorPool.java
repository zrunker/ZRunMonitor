package cc.banzhi.runmonitor.monitor;

import java.util.HashMap;
import java.util.Map;

import cc.banzhi.runmonitor.monitor.memory.MemoryMonitor;
import cc.banzhi.runmonitor.type.MonitorType;

/**
 * @program: ZRunMonitor
 * @description: 监视器池
 * @author: zoufengli01
 * @create: 2022/3/10 8:24 下午
 **/
public class MonitorPool {
    public static Map<Integer, IMonitor> map = new HashMap<>();

    static {
        map.put(MonitorType.MEMORY, new MemoryMonitor());
    }
}
