package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.entity.Patch;
import com.kubernetes.monitor.service.handler.DeployHandler;
import com.kubernetes.monitor.util.CommonUtil;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeployService {
    private DeployHandler deployHandler;

    @Autowired
    public DeployService(DeployHandler deployHandler) {
        this.deployHandler = deployHandler;
    }

    public ResponseMessage createNamespacedDeploy(V1Deployment body, String namespace) {
        try {
            V1Deployment result = deployHandler.createNamespacedDeploy(body,namespace);
            return CommonUtil.toJsonObject(result);
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
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage listDeploymentsForAllNamespaces(){
        try{
            V1DeploymentList result = deployHandler.listDeploymentsForAllNamespaces();
            return CommonUtil.toJsonObject(result);
        }catch (ApiException e){
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNamespacedDeploy(String name,String namespace){
        try {
            V1Deployment result = deployHandler.readNamespacedDeploy(name, namespace);
            return CommonUtil.toJsonObject(result);
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
            return CommonUtil.toJsonObject(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }
}
