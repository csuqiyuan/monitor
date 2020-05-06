package com.kubernetes.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.service.handler.PodHandler;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodService {
    private PodHandler podHandler;

    @Autowired
    public PodService(PodHandler podHandler) {
        this.podHandler = podHandler;
    }

    public ResponseMessage createNamespacedPod(V1Pod body, String namespace) {
        try {
            V1Pod result = podHandler.createNamespacedPod(body, namespace);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }
    }

    public ResponseMessage deleteNamespacedPod(String name, String namespace) {
        try {
            podHandler.deleteNamespacedPod(name, namespace);
            return ResultUtil.success();
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        } catch (Exception e) {
            IllegalStateException ise = (IllegalStateException) e.getCause();
            if (ise.getMessage() != null && ise.getMessage().contains("Expected a string but was BEGIN_OBJECT")) {
                return ResultUtil.success();
            }
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    public ResponseMessage listNamespacedPod(String namespace) {
        try {
            V1PodList result = podHandler.listNamespacedPod(namespace);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }
    }

    public ResponseMessage readNamespacedPod(String name, String namespace) {
        try {
            V1Pod result = podHandler.readNamespacedPod(name, namespace);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }
    }

    public ResponseMessage listPodsByNode(String hostname) {
        try {
            V1PodList result = podHandler.listPodsByNode(hostname);
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("strValue");
            String objJson = JSON.toJSONString(result,filter);
            return ResultUtil.success(JSON.parseObject(objJson));
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }

    }
}
