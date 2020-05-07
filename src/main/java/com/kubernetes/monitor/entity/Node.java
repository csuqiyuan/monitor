package com.kubernetes.monitor.entity;

import io.kubernetes.client.openapi.models.V1Node;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Node {
    private String hostname;
    private String address;
    private String osImage;
    private BigDecimal totalCpu;
    private BigDecimal totalMemory;
    private BigDecimal restCpu;
    private BigDecimal restMemory;
    private BigDecimal usableCpu;
    private BigDecimal usableMemory;
    private String createTime;
    private Integer isMaster;

    private Node(String hostname, String address, String osImage, BigDecimal totalCpu, BigDecimal totalMemory, BigDecimal restCpu, BigDecimal restMemory) {
        this.hostname = hostname;
        this.address = address;
        this.osImage = osImage;
        this.totalCpu = totalCpu;
        this.totalMemory = totalMemory;
        this.restCpu = restCpu;
        this.restMemory = restMemory;
    }

    public static Node toNode(V1Node v1Node){
        return new Node(v1Node.getMetadata().getName(),
                v1Node.getStatus().getAddresses().get(0).getAddress(),
                v1Node.getStatus().getNodeInfo().getOsImage(),
                v1Node.getStatus().getCapacity().get("cpu").getNumber(),
                v1Node.getStatus().getCapacity().get("memory").getNumber(),
                v1Node.getStatus().getAllocatable().get("cpu").getNumber(),
                v1Node.getStatus().getAllocatable().get("memory").getNumber());
    }
}
