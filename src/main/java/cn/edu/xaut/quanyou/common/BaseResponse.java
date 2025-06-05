package cn.edu.xaut.quanyou.common;

import lombok.Data;

import java.io.Serializable;

/**
 *公共返回结果类
 * @param <T>
 */

@Data
public class BaseResponse<T> implements Serializable {
private int code;
private String message;
private T data;
private  String description;

    public BaseResponse( int code, T data,String message, String description) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, String message) {
        this(code, null ,message,"");

    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(int code, String message, T data) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
       this(code, data, "", "");
    }
}
