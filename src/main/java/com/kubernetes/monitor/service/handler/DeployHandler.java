package com.kubernetes.monitor.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kubernetes.monitor.entity.Patch;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1Status;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DeployHandler {
    private AppsV1Api apiInstance = new AppsV1Api();

    public V1Deployment createNamespacedDeploy(V1Deployment body, String namespace) throws ApiException {
        apiInstance = new AppsV1Api();
        return apiInstance.createNamespacedDeployment(namespace, body, null, null, null);
    }

    public V1Status deleteNamespacedDeploy(String name, String namespace) throws ApiException {
        apiInstance = new AppsV1Api();
        return apiInstance.deleteNamespacedDeployment(name, namespace, null, null,
                    null, null, null, null);
    }

    public V1DeploymentList listNamespacedDeploy(String namespace) throws ApiException {
        apiInstance = new AppsV1Api();
        return apiInstance.listNamespacedDeployment(namespace, null,
                    null, null, null, null, null,
                    null, null, null);
    }

    public V1DeploymentList listDeploymentsForAllNamespaces() throws ApiException{
        apiInstance = new AppsV1Api();
        return apiInstance.listDeploymentForAllNamespaces(null,null,null,
                null,null,null,null,null,null);
    }

    public V1Deployment readNamespacedDeploy(String name, String namespace) throws ApiException {
        apiInstance = new AppsV1Api();
        return apiInstance.readNamespacedDeployment(name, namespace, null, null, null);
    }

    public V1Deployment patchNamespacedDeploy(String name, String namespace, Patch patch) throws ApiException {
        apiInstance = new AppsV1Api();
        ArrayList<JSONObject> body = new ArrayList<>();
        body.add(JSON.parseObject(JSON.toJSONString(patch)));
        V1Patch v1Patch = new V1Patch(body.toString());
        return apiInstance.patchNamespacedDeployment(name, namespace, v1Patch, "true", null, null, null);
    }
}
