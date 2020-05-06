package com.kubernetes.monitor.service.handler;

import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommonHandler {
    private NodeHandler nodeHandler;
    private NamespaceHandler namespaceHandler;
    private DeployHandler deployHandler;
    private ServiceHandler serviceHandler;
    private PodHandler podHandler;

    @Autowired
    public CommonHandler(NodeHandler nodeHandler,
                         NamespaceHandler namespaceHandler,
                         DeployHandler deployHandler,
                         ServiceHandler serviceHandler,
                         PodHandler podHandler) {
        this.nodeHandler = nodeHandler;
        this.namespaceHandler = namespaceHandler;
        this.deployHandler = deployHandler;
        this.serviceHandler = serviceHandler;
        this.podHandler = podHandler;
    }

    public Map<String, Integer> getResourcesNums() throws ApiException {
        HashMap<String, Integer> result = new HashMap<>();
        int nodeNum = nodeHandler.listNode().size();
        result.put("nodeNum", nodeNum);
        int namespaceNum = namespaceHandler.listNamespace().getItems().size();
        result.put("namespaceNum", namespaceNum);
        int deployNum = deployHandler.listDeploymentesForAllNamespaces().getItems().size();
        result.put("deployNum", deployNum);
        int serviceNum = serviceHandler.listServiceForAllNamespaces().getItems().size();
        result.put("serviceNum", serviceNum);
        int podNum = podHandler.listPodForAllNamespaces().getItems().size();
        result.put("podNum", podNum);
        return result;
    }
}
