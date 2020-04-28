package com.kubernetes.monitor.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "vm_info")
public class VmInfo {
    @Id
    @Column(name = "hostname")
    private String hostname;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "root_name")
    private String rootName;
    @Column(name = "root_password")
    private String rootPassword;
    @Column(name = "is_master")
    private Integer isMaster;
    @Column(name = "token")
    private String token;
    @Column(name = "sha")
    private String sha;
}
