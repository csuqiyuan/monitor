package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.entity.Patch;
import com.kubernetes.monitor.util.KubeClient;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.util.resultcode.ResultEnum;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NamespaceService {
    private CoreV1Api apiInstance = new CoreV1Api(KubeClient.getClient());
    public ResponseMessage createNamespace(V1Namespace body) {
        try {
            V1Namespace result = apiInstance.createNamespace(body, null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage deleteNamespace(String name) {
        try {
            V1Status result = apiInstance.deleteNamespace(name, null, null, null,
                    null, null, null);
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
            V1NamespaceList result = apiInstance.listNamespace(null, null, null,
                    null, null, null, null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNamespace(String name){
        try {
            V1Namespace result = apiInstance.readNamespace(name, null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

}
