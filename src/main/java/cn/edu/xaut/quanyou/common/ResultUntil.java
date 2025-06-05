package cn.edu.xaut.quanyou.common;

import com.fasterxml.jackson.databind.ser.Serializers;

public class ResultUntil {
    public static <T> BaseResponse<T> success(T data)
    {
        return new BaseResponse<>(0,data,"ok","");
    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode)
    {
        return new BaseResponse<>(errorCode);

    }
    public static <T> BaseResponse<T> error( int code,  String message, String description)
    {
        return new BaseResponse<>(code,null,message,description);

    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description)
    {
        return new BaseResponse<>(errorCode.getCode(),null,message,description);

    }

}
