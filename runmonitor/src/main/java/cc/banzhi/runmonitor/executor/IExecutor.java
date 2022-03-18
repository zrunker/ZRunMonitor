package cc.banzhi.runmonitor.executor;

import cc.banzhi.runmonitor.dto.HandleEvent;

/**
 * @program: ZRunMonitor
 * @description: 分发接口
 * @author: zoufengli01
 * @create: 2022/3/10 4:54 下午
 **/
public interface IExecutor {
    <T> void execute(HandleEvent<T> event);

    <T> void executeDelay(HandleEvent<T> event, long delayMillis);

    <T> void removeEvent(HandleEvent<T> event);
}