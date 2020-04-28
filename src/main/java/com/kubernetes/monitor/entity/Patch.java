package com.kubernetes.monitor.entity;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Serialization
public class Patch<T> {
    private String op;
    private String path;
    private T value;
}
