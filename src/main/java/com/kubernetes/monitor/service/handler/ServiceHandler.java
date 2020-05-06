package com.kubernetes.monitor.service.handler;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1Status;
import org.springframework.stereotype.Component;

@Component
public class ServiceHandler {
    private CoreV1Api apiInstance = new CoreV1Api();
    public V1Service createNamespacedService(V1Service body, String namespace) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.createNamespacedService(namespace, body, null, null, null);
    }

    public V1Status deleteNamespacedService(String name, String namespace) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.deleteNamespacedService(name, namespace, null, null, null, null, null, null);
    }

    public V1ServiceList listNamespacedService(String namespace) throws ApiException{
        apiInstance = new CoreV1Api();
        return apiInstance.listNamespacedService(namespace, null, null, null, null, null, null, null, null, null);
    }

    public V1ServiceList listServiceForAllNamespaces() throws ApiException{
        apiInstance = new CoreV1Api();
        return apiInstance.listServiceForAllNamespaces(null,null,null,
                null, null,null,null,null,null);
    }

    public V1Service readNamespacedService(String name,String namespace) throws ApiException{
        apiInstance = new CoreV1Api();
        return apiInstance.readNamespacedService(name, namespace, null, null, null);
    }
}
