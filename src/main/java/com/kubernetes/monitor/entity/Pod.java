package com.kubernetes.monitor.entity;

import io.kubernetes.client.openapi.models.V1Pod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Pod {
    private String name;
    private String namespace;
    private String image;
    private Map<String,String> labels;
    private Integer containerPort;
    private String nodeName;
    private String hostIp;
    private String limitCpu;
    private String limitMemory;
    private String requestsCpu;
    private String requestsMemory;
    private String phase;

}
