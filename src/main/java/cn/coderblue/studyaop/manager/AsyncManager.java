package cn.coderblue.studyaop.manager;

import cn.coderblue.studyaop.aspect.MyLogAspect;
import cn.coderblue.studyaop.entity.SysOperationLog;
import cn.coderblue.studyaop.utils.SnowflakeIdUtils;
import cn.coderblue.studyaop.utils.ThreadPoolExecutorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 异步任务管理器
 *
 * @author coderblue
 */
public class AsyncManager {

    private static final Logger log = LoggerFactory.getLogger(MyLogAspect.class);

    /**
     * 单例模式
     */
    private AsyncManager() {
    }

    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me() {
        return me;
    }

    @Async
    public void saveLog(SysOperationLog sysOperationLog) {
        log.info("异步处理日志信息中...{}", sysOperationLog);
    }

    /**
     * 批量处理大量sql
     * @param listLog
     */
    @Async
    public void saveLog(List<SysOperationLog> listLog) {
        ThreadPoolExecutorUtils.getInstance().executorTask(listLog);
    }
}
