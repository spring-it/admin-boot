package cn.mesmile.admin.common.exceptions;


import cn.mesmile.admin.common.result.IResultCode;
import cn.mesmile.admin.common.result.ResultCode;

/**
 * @author zb
 * @Description 自定义服务异常
 */
public class ServiceException extends RuntimeException {

    private final long serialVersionUID = 1L;

    private int code = ResultCode.FAILURE.getCode();

    private String msg = ResultCode.FAILURE.getMessage();

    public ServiceException() {
        super();
    }

    public ServiceException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ServiceException(IResultCode resultCode, String msg) {
        super(msg);
        this.code = resultCode.getCode();
        this.msg = msg;
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public ServiceException(IResultCode resultCode, String msg, Throwable cause) {
        super(msg, cause);
        this.code = resultCode.getCode();
        this.msg = msg;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
