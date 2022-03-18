package cc.banzhi.runmonitor.executor.handle;

import android.content.Context;
import android.os.Handler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import cc.banzhi.runmonitor.MonitorMap;
import cc.banzhi.runmonitor.layer.MonitorLayer;
import cc.banzhi.runmonitor.monitor.MonitorType;

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

    public AbsHandle get(@MonitorType int type,
                         Context context, MonitorLayer layer, Handler mainHandler) {
        AbsHandle absHandle = map.get(type);
        if (absHandle == null) {
            MonitorMap.Item item = MonitorMap.map.get(type);
            if (item != null) {
                Class<? extends AbsHandle> clazz = item.clazz;
                if (clazz != null) {
                    try {
                        absHandle = clazz.getConstructor(
                                Context.class,
                                MonitorLayer.class,
                                Handler.class)
                                .newInstance(context, layer, mainHandler);
                        map.put(type, absHandle);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return absHandle;
    }

}
