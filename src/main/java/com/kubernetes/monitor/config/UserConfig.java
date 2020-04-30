package com.kubernetes.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("user")
public class UserConfig {
    private String serverIp;
    private String privateDockerRepo;

}
