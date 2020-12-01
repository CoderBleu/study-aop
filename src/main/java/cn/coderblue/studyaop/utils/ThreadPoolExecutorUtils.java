package cn.coderblue.studyaop.utils;

import cn.coderblue.studyaop.entity.SysOperationLog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 线程池
 *
 * @author coderblue
 */
public class ThreadPoolExecutorUtils {

    final Integer corePoolSize = 2;
    final Integer maximumPoolSize = 10;
    final Long keepAliveTime = 1L;

    ExecutorService executorService = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private static class SingletonInstance {
        private static final ThreadPoolExecutorUtils INSTANCE = new ThreadPoolExecutorUtils();
    }

    public static ThreadPoolExecutorUtils getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void executorTask(List<SysOperationLog> logList) {
        try {
            logList.parallelStream().forEach(item -> {
                long id = Long.parseLong(UUID.randomUUID().toString().replace("-", ""));
                item.setLogId(id);
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "log日志信息:" + item);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
