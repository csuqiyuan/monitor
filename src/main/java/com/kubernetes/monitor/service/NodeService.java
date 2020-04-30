package com.kubernetes.monitor.service;

import com.kubernetes.monitor.service.handler.NodeHandler;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1NodeList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeService {
    private NodeHandler nodeHandler;

    @Autowired
    public NodeService(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    public ResponseMessage listNode() {
        try {
            V1NodeList result = nodeHandler.listNode();
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }
    }
}
