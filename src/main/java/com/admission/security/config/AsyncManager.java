package com.admission.security.config;

import com.admission.security.utils.SpringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class AsyncManager {
    /**
     * 异步操作任务调度线程池
     */
    private ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolTaskExecutor");

    private ScheduledExecutorService springSchedule = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncManager() {
    }

    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me() {
        return me;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        executor.execute(task);
    }

    /**
     * 提交任务等待返回
     *
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        executor.shutdown();
    }

    public ScheduledExecutorService getSpringSchedule() {
        return springSchedule;
    }
}
