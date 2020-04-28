package com.kubernetes.monitor.service;

import com.kubernetes.monitor.util.KubeClient;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.util.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1Pod;

import org.springframework.stereotype.Service;

@Service
public class NodeService {
    private CoreV1Api apiInstance = new CoreV1Api(KubeClient.getClient());
    public ResponseMessage createNode(V1Pod body, String namespace) {
        return ResultUtil.success();
    }

    public ResponseMessage deleteNode(String name, String namespace) {
        return ResultUtil.success();
    }

    public ResponseMessage listNode(){
        try {
            V1NodeList result = apiInstance.listNode(null, null, null,
                    null, null, null, null, null, null);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode()==404){
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(),e.getResponseBody());
        }
    }

    public ResponseMessage readNode(String name,String namespace){
        return ResultUtil.success();
    }
}
