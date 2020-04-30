package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.entity.Patch;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DepolyService {
    private AppsV1Api apiInstance = new AppsV1Api();

    public ResponseMessage createNamespacedDeploy(V1Deployment body, String namespace) {
        apiInstance = new AppsV1Api();
        try {
            V1Deployment result = apiInstance.createNamespacedDeployment(namespace, body, null, null, null);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("intValue");
            String objJson = JSON.toJSONString(result,filter);
            return ResultUtil.success(JSON.parseObject(objJson));
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage deleteNamespacedDeploy(String name, String namespace) {
        apiInstance = new AppsV1Api();
        try {
            V1Status result = apiInstance.deleteNamespacedDeployment(name, namespace, null, null,
                    null, null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage listNamespacedDeploy(String namespace){
        apiInstance = new AppsV1Api();
        try {
            V1DeploymentList result = apiInstance.listNamespacedDeployment(namespace, null,
                    null, null, null, null, null,
                    null, null, null);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("intValue");
            String objJson = JSON.toJSONString(result,filter);
            return ResultUtil.success(JSON.parseObject(objJson));
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNamespacedDeploy(String name,String namespace){
        apiInstance = new AppsV1Api();
        try {
            V1Deployment result = apiInstance.readNamespacedDeployment(name, namespace, null, null, null);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("intValue");
            String objJson = JSON.toJSONString(result,filter);
            return ResultUtil.success(JSON.parseObject(objJson));
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage patchNamespacedDeploy(String name,String namespace,Patch patch){
        apiInstance = new AppsV1Api();
        ArrayList<JSONObject> body = new ArrayList<>();
        body.add(JSON.parseObject(JSON.toJSONString(patch)));
        V1Patch v1Patch = new V1Patch(body.toString());
        try {
            V1Deployment result = apiInstance.patchNamespacedDeployment(name, namespace, v1Patch, "true", null, null, null);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("intValue");
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
