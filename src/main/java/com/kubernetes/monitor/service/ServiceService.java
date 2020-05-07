package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.service.handler.ServiceHandler;
import com.kubernetes.monitor.util.CommonUtil;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {
    private ServiceHandler serviceHandler;

    @Autowired
    public ServiceService(ServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    public ResponseMessage createNamespacedService(V1Service body, String namespace) {
        try {
            V1Service result = serviceHandler.createNamespacedService(body,namespace);
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage deleteNamespacedService(String name, String namespace) {
        try {
            V1Status result = serviceHandler.deleteNamespacedService(name, namespace);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage listNamespacedService(String namespace){
        try {
            V1ServiceList result = serviceHandler.listNamespacedService(namespace);
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage listServiceForAllNamespaces(){
        try {
            V1ServiceList result = serviceHandler.listServiceForAllNamespaces();
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNamespacedService(String name,String namespace){
        try {
            V1Service result = serviceHandler.readNamespacedService(name, namespace);
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }
}
