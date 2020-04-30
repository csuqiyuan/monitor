package com.kubernetes.monitor.service;

import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

@Service
public class PodService {

    private CoreV1Api apiInstance = new CoreV1Api();

    public ResponseMessage createNamespacedPod(V1Pod body, String namespace) {
        apiInstance = new CoreV1Api();
        try {
            V1Pod result = apiInstance.createNamespacedPod(namespace, body, null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }
    }

    public ResponseMessage deleteNamespacedPod(String name, String namespace) {
        apiInstance = new CoreV1Api();
        try {
            apiInstance.deleteNamespacedPod(name, namespace, null, null, null, null, null, null);
            return ResultUtil.success();
        } catch (ApiException e){
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }catch (Exception e) {
            IllegalStateException ise = (IllegalStateException) e.getCause();
            if (ise.getMessage() != null && ise.getMessage().contains("Expected a string but was BEGIN_OBJECT")) {
                return ResultUtil.success();
            }
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    public ResponseMessage listNamespacedPod(String namespace){
        apiInstance = new CoreV1Api();
        try {
            V1PodList result = apiInstance.listNamespacedPod(namespace, null, null,
                    null, null, null, null, null,
                    null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNamespacedPod(String name,String namespace){
        apiInstance = new CoreV1Api();
        try {
            V1Pod result = apiInstance.readNamespacedPod(name, namespace,
                    null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }
}
