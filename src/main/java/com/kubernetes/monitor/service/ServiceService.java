package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {
    private CoreV1Api apiInstance = new CoreV1Api();
    public ResponseMessage createNamespacedService(V1Service body, String namespace) {
        apiInstance = new CoreV1Api();
        try {
            V1Service result = apiInstance.createNamespacedService(namespace, body, null, null, null);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("strValue");
            String objJson = JSON.toJSONString(result,filter);
            return ResultUtil.success(JSON.parseObject(objJson));
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage deleteNamespacedService(String name, String namespace) {
        apiInstance = new CoreV1Api();
        try {
            V1Status result = apiInstance.deleteNamespacedService(name, namespace, null, null, null, null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage listNamespacedService(String namespace){
        apiInstance = new CoreV1Api();
        try {
            V1ServiceList result = apiInstance.listNamespacedService(namespace, null, null, null, null, null, null, null, null, null);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("strValue");
            String objJson = JSON.toJSONString(result,filter);
            return ResultUtil.success(JSON.parseObject(objJson));
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNamespacedService(String name,String namespace){
        apiInstance = new CoreV1Api();
        try {
            V1Service result = apiInstance.readNamespacedService(name, namespace, null, null, null);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("strValue");
            String objJson = JSON.toJSONString(result,filter);
            return ResultUtil.success(JSON.parseObject(objJson));
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }
}
