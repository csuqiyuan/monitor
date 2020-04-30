package com.kubernetes.monitor.util;

import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;

/**
 * 定义了请求成功和失败的封装类型
 */
public class ResultUtil {

    /**
     * 操作成功且需返回数据
     *
     * @param object 返回体中将要返回的数据
     * @param <T>    泛型，定义了返回体中的数据类型
     * @return {@link com.kubernetes.monitor.util.response.ResponseMessage}
     */
    public static <T> ResponseMessage<T> success(T object) {
        ResponseMessage<T> responseMessage = new ResponseMessage<>();
        responseMessage.setCode(ResultEnum.SUCCESS.getCode());
        responseMessage.setMessage(ResultEnum.SUCCESS.getMsg());
        responseMessage.setData(object);
        return responseMessage;
    }

    /**
     * 操作成功但不返回数据
     *
     * @return {@link com.kubernetes.monitor.util.response.ResponseMessage}
     */
    public static ResponseMessage success() {
        return success(null);
    }

    /**
     * 操作失败返回的消息
     *
     * @param code 一个定义好了的错误码
     * @param msg  对应错误码的文字描述
     * @return {@link com.kubernetes.monitor.util.response.ResponseMessage}
     */
    public static ResponseMessage error(int code, String msg) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(code);
        responseMessage.setMessage(msg);
        return responseMessage;
    }

    /**
     * 操作失败返回消息，对error的重载
     *
     * @param resultEnum 是一个自定义的错误类型，包含错误码和文字描述
     * @return {@link com.kubernetes.monitor.util.response.ResponseMessage}
     */
    public static ResponseMessage error(ResultEnum resultEnum) {
        return error(resultEnum.getCode(), resultEnum.getMsg());
    }
}
