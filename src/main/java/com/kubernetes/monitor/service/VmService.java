package com.kubernetes.monitor.service;

import com.kubernetes.monitor.config.UserConfig;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import com.kubernetes.monitor.entity.TokenAndSha;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.service.handler.VmHandler;
import com.kubernetes.monitor.util.ConnectUtil;
import com.kubernetes.monitor.util.KubeClient;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.config.cmd.CmdsConfig;
import com.kubernetes.monitor.util.exception.CustomException;
import com.kubernetes.monitor.util.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VmService {

    private VmHandler vmHandler;
    private UserConfig userConfig;

    @Autowired
    public VmService(VmHandler vmHandler,
                     UserConfig userConfig) {
        this.vmHandler = vmHandler;
        this.userConfig = userConfig;
    }

//    public ResponseMessage addVm(VmInfo info) {
//        return ResultUtil.success(vmHandler.addVm(info));
//    }

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
        vmHandler.addVm(vmInfo);
        new KubeClient(vmInfo.getHostname());
        return ResultUtil.success();
    }

    public ResponseMessage createMaster(VmInfo vmInfo) {
        String privateDockerRepo = userConfig.getPrivateDockerRepo();
        String serverIp = userConfig.getServerIp();
        vmHandler.addVm(vmInfo);
        try {
            // 下载kube_tool
            ConnectUtil.exec(vmInfo, CmdsConfig.getTool(vmInfo.getUsername()), true);
            // 安装
            ConnectUtil.exec(vmInfo, CmdsConfig.installMaster(vmInfo.getUsername(),privateDockerRepo), true);
            // 复制config文件
            ConnectUtil.exec(vmInfo, CmdsConfig.cpConfig(vmInfo.getUsername(),vmInfo.getPassword()), false);
            // 传token
            ConnectUtil.exec(vmInfo, CmdsConfig.postToken(vmInfo.getUsername(),serverIp), false);
            new KubeClient(vmInfo.getHostname());
        } catch (CustomException e) {
            return ResultUtil.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
        return ResultUtil.success();
    }

    public ResponseMessage node(VmInfo vmInfo) {
        String serverIp = userConfig.getServerIp();
        String privateDockerRepo = userConfig.getPrivateDockerRepo();
        System.out.println(serverIp + " " + privateDockerRepo);
        vmHandler.addVm(vmInfo);
        try {
            // 从git下载kube_tool
            ConnectUtil.exec(vmInfo, CmdsConfig.getTool(vmInfo.getUsername()), true);
            // 安装前先传一下token；
            VmInfo master = vmHandler.getMaster();
            ConnectUtil.exec(master, CmdsConfig.postToken(master.getUsername(),serverIp), false);
            // 更新
            master = vmHandler.getMaster();
            if (master.getToken() == null || master.getToken().equals("")) {
                // 创建并重传token
                ConnectUtil.exec(master, CmdsConfig.createPostToken(master.getUsername(),serverIp), false);
                master = vmHandler.getMaster();
            }
            String tokenArgs = privateDockerRepo + "," + master.getHostname() + ":6443," + master.getToken() + "," + master.getSha();
            // 安装,需要token和sha和master ip
            ConnectUtil.exec(vmInfo, CmdsConfig.installNode(vmInfo.getUsername(),tokenArgs), true);
            // 从master传config文件
            String scpArgs = vmInfo.getHostname() + ",22," + vmInfo.getRootName() + "," + vmInfo.getRootPassword();
            ConnectUtil.exec(master, CmdsConfig.scpConfig(master.getUsername(),scpArgs), true);
            // 复制config文件
            ConnectUtil.exec(vmInfo, CmdsConfig.cpConfig(vmInfo.getUsername(),vmInfo.getPassword()), false);
            // 安装网络插件
            ConnectUtil.exec(vmInfo, CmdsConfig.netWork(vmInfo.getUsername()), false);
        } catch (CustomException e) {
            return ResultUtil.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
        return ResultUtil.success();
    }

//    public void setClient(){
//        VmInfo vmInfo = vmHandler.getMaster();
//        if (vmInfo!=null){
//            KubeClient.setClient(vmInfo.getHostname());
//        }
//    }
}
