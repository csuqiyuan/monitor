package com.kubernetes.monitor.util;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;


public class KubeClient {
    private static ApiClient client = new ClientBuilder().setBasePath("https://192.168.110.53:6443").setVerifyingSsl(false).build();
    public static ApiClient getClient() {
        return client;
    }
}
