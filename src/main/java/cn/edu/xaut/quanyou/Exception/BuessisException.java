package cn.edu.xaut.quanyou.Exception;

import cn.edu.xaut.quanyou.common.ErrorCode;

public class BuessisException extends RuntimeException{
    private static int code;
    private static String edesciption;

    public BuessisException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.edesciption = errorCode.getDescription();
    }

    public BuessisException(ErrorCode errorCode,   String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.edesciption = description;
    }
    public static int getCode() {
        return code;
    }

    public static String getEdesciption() {
        return edesciption;
    }


}
