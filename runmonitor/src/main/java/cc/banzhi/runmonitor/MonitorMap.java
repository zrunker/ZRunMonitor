package cc.banzhi.runmonitor;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cc.banzhi.runmonitor.executor.handle.AbsHandle;
import cc.banzhi.runmonitor.executor.handle.MainHandle;
import cc.banzhi.runmonitor.executor.handle.MemoryHandle;
import cc.banzhi.runmonitor.monitor.IMonitor;
import cc.banzhi.runmonitor.monitor.MonitorType;
import cc.banzhi.runmonitor.monitor.main.MainMonitor;
import cc.banzhi.runmonitor.monitor.memory.MemoryMonitor;

/**
 * @program: ZRunMonitor
 * @description: 监视器映射文件
 * @author: zoufengli01
 * @create: 2022/3/16 2:06 下午
 **/
public class MonitorMap {
    public static Map<Integer, Item> map = new ConcurrentHashMap<>();

    // 注册清单
    static {
        map.put(MonitorType.MEMORY, new Item(new MemoryMonitor(), MemoryHandle.class));
        map.put(MonitorType.MAIN, new Item(new MainMonitor(), MainHandle.class));
    }

    public static class Item {
        public IMonitor monitor;
        public Class<? extends AbsHandle> clazz;

        public Item(@Nullable IMonitor monitor, Class<? extends AbsHandle> clazz) {
            this.monitor = monitor;
            this.clazz = clazz;
        }
    }
}
