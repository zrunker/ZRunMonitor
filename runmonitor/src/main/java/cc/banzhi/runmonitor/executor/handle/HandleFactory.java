package cc.banzhi.runmonitor.executor.handle;

import java.util.HashMap;
import java.util.Map;

import cc.banzhi.runmonitor.type.MonitorType;

/**
 * @program: ZRunMonitor
 * @description: 消费工厂类
 * @author: zoufengli01
 * @create: 2022/3/11 2:11 下午
 **/
public class HandleFactory {
    private final Map<Integer, AbsHandle> map = new HashMap<>();
    private static volatile HandleFactory instance;

    private HandleFactory() {
    }

    public static HandleFactory getInstance() {
        if (instance == null) {
            synchronized (HandleFactory.class) {
                if (instance == null) {
                    instance = new HandleFactory();
                }
            }
        }
        return instance;
    }

    public AbsHandle get(@MonitorType int type) {
        AbsHandle absHandle = map.get(type);
        if (absHandle == null) {
            switch (type) {
                case MonitorType.MEMORY:
                    absHandle = new MemoryHandle();
                    map.put(MonitorType.MEMORY, absHandle);
                    break;
            }
        }
        return absHandle;
    }

}
