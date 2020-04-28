package com.kubernetes.monitor.entity;

import lombok.Data;

@Data
public class TokenAndSha {
    private String token;
    private String sha;
}
