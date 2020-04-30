package com.kubernetes.monitor.service.handler;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NodeList;
import org.springframework.stereotype.Component;

@Component
public class NodeHandler {

    public V1NodeList listNode() throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api();
        return apiInstance.listNode(null, null, null,
                null, null, null, null, null, null);
    }
}
