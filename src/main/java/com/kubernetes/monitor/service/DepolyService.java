package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.entity.Patch;
import com.kubernetes.monitor.service.handler.DeployHandler;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepolyService {
    private DeployHandler deployHandler;

    @Autowired
    public DepolyService(DeployHandler deployHandler) {
        this.deployHandler = deployHandler;
    }

    public ResponseMessage createNamespacedDeploy(V1Deployment body, String namespace) {
        try {
            V1Deployment result = deployHandler.createNamespacedDeploy(body,namespace);
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
        try {
            V1Status result = deployHandler.deleteNamespacedDeploy(name, namespace);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage listNamespacedDeploy(String namespace){
        try {
            V1DeploymentList result = deployHandler.listNamespacedDeploy(namespace);
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
        try {
            V1Deployment result = deployHandler.readNamespacedDeploy(name, namespace);
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
        try {
            V1Deployment result = deployHandler.patchNamespacedDeploy(name, namespace, patch);
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
