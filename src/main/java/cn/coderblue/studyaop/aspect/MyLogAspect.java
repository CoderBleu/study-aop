package cn.coderblue.studyaop.aspect;

import cn.coderblue.studyaop.annotation.Log;
import cn.coderblue.studyaop.entity.SysOperationLog;
import cn.coderblue.studyaop.enums.BusinessStatus;
import cn.coderblue.studyaop.manager.AsyncManager;
import cn.coderblue.studyaop.utils.ServletUtils;
import cn.coderblue.studyaop.utils.SnowflakeIdUtils;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author coderblue
 */
@Aspect
@Component
public class MyLogAspect {
    private static final Logger log = LoggerFactory.getLogger(MyLogAspect.class);

    @Pointcut("@annotation(cn.coderblue.studyaop.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            // 获取当前的用户，设置setOperName,setDeptName

            // *========数据库日志=========*//
            SysOperationLog operLog = new SysOperationLog();
            operLog.setStatus(BusinessStatus.SUCCESS.getCode());
            // 返回结果参数
            operLog.setJsonResult(JSON.toJSONString(jsonResult));
            // 设置请求url
            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.getCode());
                operLog.setErrorMsg(e.getMessage());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            operLog.setOperTime(new Date());
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog);
            operLog.setLogId(new SnowflakeIdUtils(2, 3).nextId());
            // 保存数据库操作 TODO
            AsyncManager.me().saveLog(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("前置通知异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperationLog operLog) throws Exception {
        // 设置action动作：ordinal()返回此枚举常数的序数
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog);
        }
    }

    /**
     * TODO JSON参数过来获取不到
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperationLog operLog) throws Exception {
        Enumeration<String> paramter = ServletUtils.getRequest().getParameterNames();
        JSONObject jsonObject = new JSONObject();
        while (paramter.hasMoreElements()) {
            String str = paramter.nextElement();
            String value = ServletUtils.getRequest().getParameter(str);
            jsonObject.put(str, value);
        }
        operLog.setOperParam(jsonObject.toJSONString());
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSONUtil.parseObj(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }
}
