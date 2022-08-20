package cn.mesmile.admin.common.handler;

import cn.mesmile.admin.common.constant.AdminConstant;
import cn.mesmile.admin.common.exceptions.*;
import cn.mesmile.admin.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * @author zb
 * @Description 全局异常拦截
 * <p>
 * 如果我同时捕获了父类和子类，那么到底能够被那个异常处理器捕获呢？比如 Exception 和 BusinessException
 * 当然是 BusinessException 的异常处理器捕获了，精确匹配，如果没有 BusinessException 的异常处理器才会轮到它的 父亲 ，
 * 父亲 没有才会到 祖父 。总之一句话， 精准匹配，找那个关系最近的
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param businessException 业务异常
     * @return @ResponseBody
     * @ExceptionHandler相当于controller的@RequestMapping 如果抛出的的是BusinessException，则调用该方法
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handle(BusinessException businessException) {
        // 获取指定包名前缀的异常信息，减少不必要的日志
        String stackTraceByPn = getStackTraceByPn(businessException, AdminConstant.BASE_PACKAGE);
        log.error("记录业务异常信息, 消息:{} 编码:{} {}", businessException.getMessage(), businessException.getCode(), stackTraceByPn);
        return R.fail(businessException.getCode(), businessException.getMessage());
    }

    /**
     * 拦截限流异常信息
     * */
    @ExceptionHandler(RateLimiterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handle(RateLimiterException rateLimiterException) {
        // 获取指定包名前缀的异常信息，减少不必要的日志
//        String stackTraceByPn = getStackTraceByPn(rateLimiterException, AdminConstant.BASE_PACKAGE);
        log.error("拦截限流异常信息, 消息:{} 编码:{}", rateLimiterException.getMessage(), rateLimiterException.getCode());
        return R.fail(rateLimiterException.getCode(), rateLimiterException.getMessage());
    }
    /**
     * 重复提交异常信息
     * */
    @ExceptionHandler(RepeatSubmitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handle(RepeatSubmitException repeatSubmitException) {
        // 获取指定包名前缀的异常信息，减少不必要的日志
//        String stackTraceByPn = getStackTraceByPn(rateLimiterException, AdminConstant.BASE_PACKAGE);
        log.error("重复提交异常信息, 消息:{} 编码:{}", repeatSubmitException.getMessage(), repeatSubmitException.getCode());
        return R.fail(repeatSubmitException.getCode(), repeatSubmitException.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handle(ServiceException serviceException) {
        // 这里记录所有堆栈信息
        log.error("记录业务异常信息, 消息:{} 编码:{}", serviceException.getMessage(), serviceException.getCode(), serviceException);
        return R.fail(serviceException.getCode(), serviceException.getMessage());
    }

    @ExceptionHandler(OssException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handle(OssException ossException) {
        // 这里记录所有堆栈信息
        log.error("oss异常信息, 消息:{} 编码:{}", ossException.getMessage(), ossException.getCode(), ossException);
        return R.fail(ossException.getCode(), ossException.getMessage());
    }

    private String getStackTraceByPn(Throwable e, String packagePrefix) {
        StringBuilder append = new StringBuilder("\n").append(e);
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().startsWith(packagePrefix)) {
                append.append("\n\tat ").append(stackTraceElement);
            }
        }
        return append.toString();
    }

    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handleValidatedException(Exception exception) {
        BindingResult bindingResult = null;
         if (exception instanceof MethodArgumentNotValidException){
             MethodArgumentNotValidException e = (MethodArgumentNotValidException) exception;
             bindingResult = e.getBindingResult();
             if (bindingResult.hasErrors()) {
//            String collect = bindingResult.getAllErrors().stream()
//                    .map(ObjectError::getDefaultMessage)
//                    .collect(Collectors.joining(";"));
                 FieldError fieldError = bindingResult.getFieldError();
                 if (fieldError != null) {
                     return R.fail(fieldError.getField()+ "：" + fieldError.getDefaultMessage());
                 }
             }
         }else if (exception instanceof ConstraintViolationException){
             ConstraintViolationException e = (ConstraintViolationException) exception;
             String collect = e.getConstraintViolations().stream()
                     .map(ConstraintViolation::getMessage)
                     .collect(Collectors.joining(";"));
             return R.fail(collect);
         }else if (exception instanceof BindException){
             BindException e = (BindException) exception;
             bindingResult = e.getBindingResult();
             if (bindingResult.hasErrors()) {
//            String collect = bindingResult.getAllErrors().stream()
//                    .map(ObjectError::getDefaultMessage)
//                    .collect(Collectors.joining(";"));
                 FieldError fieldError = bindingResult.getFieldError();
                 if (fieldError != null) {
                     return R.fail(fieldError.getField()+ "：" + fieldError.getDefaultMessage());
                 }
             }
         }
        return R.fail(exception.getMessage());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handlerMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        String message = exception.getMessage();
        log.error("全局捕获MissingServletRequestParameterException错误信息: {}", message, exception);
        return R.fail("缺少必要参数");
    }

    /**
     * 捕获空指针异常
     **/
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handlerNullPointException(NullPointerException exception) {
        String message = exception.getMessage();
        log.error("全局捕获null错误信息: {}", exception.toString(), exception);
        return R.fail(message);
    }

    /**
     * 捕获授权异常 403
     **/
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R handlerAccessDeniedException(AccessDeniedException exception) {
        String message = exception.getMessage();
        log.error("全局捕获授权错误信息，{}", message ,exception);
        return R.fail(HttpStatus.FORBIDDEN.value(),"权限不足");
    }

    /**
     * 捕获最大异常
     **/
    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handlerBindException(Exception exception) {
        String message = exception.getMessage();
        log.error("全局捕获错误信息: {}", exception.toString(), exception);
        return R.fail(message);
    }
}