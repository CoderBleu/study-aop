// package cn.coderblue.studyaop.aspect;
//
// import cn.coderblue.studyaop.annotation.Log;
// import cn.coderblue.studyaop.entity.SysOperationLog;
// import com.alibaba.fastjson.JSON;
// import org.aspectj.lang.JoinPoint;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.Signature;
// import org.aspectj.lang.annotation.*;
// import org.aspectj.lang.reflect.MethodSignature;
// import org.springframework.http.HttpMethod;
// import org.springframework.stereotype.Component;
// import org.springframework.web.multipart.MultipartFile;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.lang.reflect.Method;
//
// /**
//  * @author coderblue
//  */
// @Aspect
// @Component
// public class MyLogAspectCopy {
//     @Pointcut("@annotation(cn.coderblue.studyaop.annotation.Log)")
//     public void addAdvice() {
//     }
//
//     @Around("addAdvice()")
//     public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
//         Object res = null;
//         long time = System.currentTimeMillis();
//         try {
//             res = joinPoint.proceed();
//             time = System.currentTimeMillis() - time;
//             return res;
//         } finally {
//             try {
//                 //方法执行完成后增加日志
//                 addOperatorLog(joinPoint, res, time);
//             } catch (Exception e) {
//                 System.out.println("LogAspect 操作失败：" + e.getMessage());
//                 e.printStackTrace();
//             }
//         }
//     }
//
//     private void addOperatorLog(JoinPoint joinPoint, Object result, long time) {
//         MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//         SysOperationLog OperatorLog = new SysOperationLog();
//         Log annotation = signature.getMethod().getAnnotation(Log.class);
//         if (annotation != null) {
//             OperatorLog.setOperatorType(annotation.operatorType().getValue());
//         }
//         //TODO 这里保存日志
//         System.out.println("记录日志:" + OperatorLog.toString());
//         // OperatorLogService.insert(OperatorLog);
//     }
//
//
//     /**
//      * 在切点前执行方法,内容为指定的切点
//      *
//      * @param joinPoint
//      */
//     @Before("addAdvice()")
//     public void before(JoinPoint joinPoint) {
//         MethodSignature sign = (MethodSignature) joinPoint.getSignature();
//         Method method = sign.getMethod();
//         Log annotation = method.getAnnotation(Log.class);
//         System.out.println("@Before - 打印：" + annotation.title() + " 开始前");
//     }
//
//     /**
//      * 在切点后,return前执行,
//      */
//     @After("addAdvice()")
//     public void after() {
//         System.out.println("@After - 方法执行后");
//     }
//
//     /**
//      * 获取注解中对方法的描述信息 用于Controller层注解
//      *
//      * @param log     日志
//      * @param operLog 操作日志
//      * @throws Exception
//      */
//     public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperationLog operLog) throws Exception {
//         // 设置action动作
//         operLog.setBusinessType(log.businessType().ordinal());
//         // 设置标题
//         operLog.setTitle(log.title());
//         // 设置操作人类别
//         operLog.setOperatorType(log.operatorType().ordinal());
//         // 是否需要保存request，参数和值
//         if (log.isSaveRequestData()) {
//             // 获取参数的信息，传入到数据库中。
//             setRequestValue(joinPoint, operLog);
//         }
//     }
//
//     /**
//      * 获取请求的参数，放到log中
//      *
//      * @param operLog 操作日志
//      * @throws Exception 异常
//      */
//     private void setRequestValue(JoinPoint joinPoint, SysOperationLog operLog) throws Exception {
//         String requestMethod = operLog.getRequestMethod();
//         if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
//             String params = argsArrayToString(joinPoint.getArgs());
//             // operLog.setOperParam(StringUtils.substring(params, 0, 2000));
//         } else {
//             // Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//             // operLog.setOperParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
//         }
//     }
//
//     /**
//      * 是否存在注解，如果存在就获取
//      */
//     private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
//         Signature signature = joinPoint.getSignature();
//         MethodSignature methodSignature = (MethodSignature) signature;
//         Method method = methodSignature.getMethod();
//
//         if (method != null) {
//             return method.getAnnotation(Log.class);
//         }
//         return null;
//     }
//
//     /**
//      * 判断是否需要过滤的对象。
//      *
//      * @param o 对象信息。
//      * @return 如果是需要过滤的对象，则返回true；否则返回false。
//      */
//     public boolean isFilterObject(final Object o)
//     {
//         return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
//     }
//
//     /**
//      * 参数拼装
//      */
//     private String argsArrayToString(Object[] paramsArray)
//     {
//         String params = "";
//         if (paramsArray != null && paramsArray.length > 0)
//         {
//             for (int i = 0; i < paramsArray.length; i++)
//             {
//                 if (!isFilterObject(paramsArray[i]))
//                 {
//                     Object jsonObj = JSON.toJSON(paramsArray[i]);
//                     params += jsonObj.toString() + " ";
//                 }
//             }
//         }
//         return params.trim();
//     }
// }
