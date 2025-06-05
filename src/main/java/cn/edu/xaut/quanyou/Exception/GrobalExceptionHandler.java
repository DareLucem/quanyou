package cn.edu.xaut.quanyou.Exception;

import cn.edu.xaut.quanyou.common.ErrorCode;
import cn.edu.xaut.quanyou.common.ResultUntil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GrobalExceptionHandler {

    @ExceptionHandler(BuessisException.class)
    public Object buessisExceptionHandler(BuessisException e) {
        return ResultUntil.error(e.getCode(), e.getMessage(), e.getEdesciption());
    }
    @ExceptionHandler(RuntimeException.class)
    public Object runtimeExceptionHandler(RuntimeException e) {
        return ResultUntil.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
