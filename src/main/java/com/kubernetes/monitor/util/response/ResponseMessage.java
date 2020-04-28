package com.kubernetes.monitor.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @param <T> 泛型，表示返回体中的数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> {
    // status code
    private int  code;
    // status message
    private String message;
    // response data
    private T data;
}
