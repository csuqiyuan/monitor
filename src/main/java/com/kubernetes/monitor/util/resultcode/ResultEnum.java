package com.kubernetes.monitor.util.resultcode;

/**
 * 自定义错误类型
 * 为防止与HTTP状态码混淆，第一位添加“1”表示是自定义状态码
 */
public enum ResultEnum {
    UNKNOWN_ERROR(100, "未知错误"),
    UN_OR_PW_ERROR(104, "密码错误"),
    SUCCESS(0, "操作成功"),
    NOT_FIND(404,"找不到资源");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
