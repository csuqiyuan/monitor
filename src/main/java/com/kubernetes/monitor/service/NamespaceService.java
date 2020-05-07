package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.kubernetes.monitor.service.handler.NamespaceHandler;
import com.kubernetes.monitor.util.CommonUtil;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NamespaceService {
    private NamespaceHandler namespaceHandler;

    @Autowired
    public NamespaceService(NamespaceHandler namespaceHandler) {
        this.namespaceHandler = namespaceHandler;
    }

    public ResponseMessage createNamespace(V1Namespace body) {
        try {
            V1Namespace result = namespaceHandler.createNamespace(body);
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage deleteNamespace(String name) {
        try {
            V1Status result = namespaceHandler.deleteNamespace(name);
            return ResultUtil.success(result);
        } catch (ApiException e) {
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

    public ResponseMessage listNamespace(){
        try {
            V1NamespaceList result = namespaceHandler.listNamespace();
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNamespace(String name){
        try {
            V1Namespace result = namespaceHandler.readNamespace(name);
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

}
