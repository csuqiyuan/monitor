package com.kubernetes.monitor.entity;

import lombok.Data;

@Data
public class Node {
    private String hostName;
    private String address;
    private String osImage;
    private int totalCpu;
    private int totalMemory;
    private int restCpu;
    private int restMemory;

}
