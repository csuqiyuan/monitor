package com.kubernetes.monitor.service.handler;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1Status;
import org.springframework.stereotype.Component;

@Component
public class NamespaceHandler {

    private CoreV1Api apiInstance = new CoreV1Api();

    public V1Namespace createNamespace(V1Namespace body) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.createNamespace(body, null, null, null);

    }

    public V1Status deleteNamespace(String name) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.deleteNamespace(name, null, null, null,
                null, null, null);

    }

    public V1NamespaceList listNamespace() throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.listNamespace(null, null, null,
                    null, null, null, null, null, null);

    }

    public V1Namespace readNamespace(String name) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.readNamespace(name, null, null, null);
    }
}
