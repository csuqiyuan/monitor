package com.kubernetes.monitor.service.handler;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class PodHandler {
    private CoreV1Api apiInstance = new CoreV1Api();

    public List<V1Pod> listPodsByNode(String hostIp) throws ApiException{
        apiInstance = new CoreV1Api();
        V1PodList list = apiInstance.listPodForAllNamespaces(null,null,null,null,
                null,null,null,null,null);
        List<V1Pod> result = new LinkedList<>();
        for (V1Pod item:list.getItems()){
            if (item.getStatus().getHostIP()!=null&&item.getStatus().getHostIP().equals(hostIp)){
                result.add(item);
            }
        }
        return result;
    }

    public V1Pod createNamespacedPod(V1Pod body, String namespace) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.createNamespacedPod(namespace, body, null, null, null);

    }

    public void deleteNamespacedPod(String name, String namespace) throws ApiException {
        apiInstance.deleteNamespacedPod(name, namespace, null, null, null, null, null, null);
    }

    public V1PodList listNamespacedPod(String namespace) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.listNamespacedPod(namespace, null, null,
                null, null, null, null, null,
                null, null);
    }

    public V1Pod readNamespacedPod(String name, String namespace) throws ApiException {
        apiInstance = new CoreV1Api();
        return apiInstance.readNamespacedPod(name, namespace,
                null, null, null);
    }
}
