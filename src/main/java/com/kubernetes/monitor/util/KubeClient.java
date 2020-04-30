package com.kubernetes.monitor.util;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;

public class KubeClient {
    public KubeClient(String ip) {
        init(ip);
    }

    private void init(String ip){
        ApiClient client = new ClientBuilder().setBasePath(String.format("https://%s:6443",ip)).setVerifyingSsl(false).build();
        Configuration.setDefaultApiClient(client);
    }
}
