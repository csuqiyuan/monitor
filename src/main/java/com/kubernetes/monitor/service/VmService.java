package com.kubernetes.monitor.service;

import com.kubernetes.monitor.entity.TokenAndSha;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.service.handler.VmHandler;
import com.kubernetes.monitor.util.ConnectUtil;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.cmd.Cmds;
import com.kubernetes.monitor.util.exception.CustomException;
import com.kubernetes.monitor.util.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VmService {

    private VmHandler vmHandler;

    @Autowired
    public VmService(VmHandler vmHandler) {
        this.vmHandler = vmHandler;
    }

    public ResponseMessage addVm(VmInfo info) {
        return ResultUtil.success(vmHandler.addVm(info));
    }

    public ResponseMessage deleteVm(String hostname) {
        try {
            vmHandler.deleteVm(hostname);
            return ResultUtil.success();
        } catch (CustomException e) {
            return ResultUtil.error(e.getCode(), e.getMsg());
        }
    }

    public ResponseMessage listVm() {
        return ResultUtil.success(vmHandler.listVm());
    }

    public ResponseMessage postTokenAndSha(TokenAndSha update) {
        try {
            return ResultUtil.success(vmHandler.postTokenAndSha(update));
        } catch (CustomException e) {
            return ResultUtil.error(e.getCode(), e.getMsg());
        }
    }

    public ResponseMessage master(VmInfo vmInfo) {
        String privateDockerRepo = "";
        String serverIp = "";
        vmHandler.addVm(vmInfo);
        try {
            // 下载kube_tool
            ConnectUtil.exec(vmInfo, Cmds.getTool, true);
            // 安装
            ConnectUtil.exec(vmInfo, Cmds.installMaster(privateDockerRepo), true);
            // 传token
            ConnectUtil.exec(vmInfo, Cmds.postToken(serverIp), true);
            // 复制config文件
            ConnectUtil.exec(vmInfo, Cmds.cpConfig, false);
        } catch (CustomException e) {
            return ResultUtil.error(e.getCode(), e.getMsg());
        }
        return ResultUtil.success();
    }

    public ResponseMessage node(VmInfo vmInfo) {
        String serverIp = "";
        String privateDockerRepo = "";
        vmHandler.addVm(vmInfo);
        try {
            // 从git下载kube_tool
            ConnectUtil.exec(vmInfo, Cmds.getTool, true);
            // 安装前先传一下token；
            VmInfo master = vmHandler.getMaster();
            ConnectUtil.exec(master, Cmds.postToken(serverIp), true);
            // 更新
            master = vmHandler.getMaster();
            if (master.getToken().equals("") || master.getToken() == null) {
                // 创建并重传token
                ConnectUtil.exec(master, Cmds.createPostToken(serverIp), true);
                master = vmHandler.getMaster();
            }
            String tokenArgs = privateDockerRepo + "," + master.getHostname() + "," + master.getToken() + "," + master.getSha();
            // 安装,需要token和sha和master ip
            ConnectUtil.exec(vmInfo, Cmds.installNode(tokenArgs), true);
            // 从master传config文件
            String scpArgs = vmInfo.getHostname()+","+vmInfo.getUsername()+","+vmInfo.getPassword();
            ConnectUtil.exec(master, Cmds.scpConfig(scpArgs), true);
            // 复制config文件
            ConnectUtil.exec(vmInfo, Cmds.cpConfig, false);
            // 安装网络插件
            ConnectUtil.exec(vmInfo, Cmds.network, true);
        } catch (CustomException e) {
            return ResultUtil.error(e.getCode(), e.getMsg());
        }
        return ResultUtil.success();
    }
}
