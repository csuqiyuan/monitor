package com.kubernetes.monitor.util.exception;

import com.kubernetes.monitor.util.resultcode.ResultEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    public CustomException(ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }
}
