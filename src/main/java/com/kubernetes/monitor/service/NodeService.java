package com.kubernetes.monitor.service;

import com.kubernetes.monitor.config.UserConfig;
import com.kubernetes.monitor.config.cmd.CmdsConfig;
import com.kubernetes.monitor.entity.Node;
import com.kubernetes.monitor.entity.TokenAndSha;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.service.handler.NodeHandler;
import com.kubernetes.monitor.util.ConnectUtil;
import com.kubernetes.monitor.util.KubeClient;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.exception.CustomException;
import com.kubernetes.monitor.util.response.ResponseMessage;
import com.kubernetes.monitor.config.resultcode.ResultEnum;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1NodeList;

import io.kubernetes.client.openapi.models.V1Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {
    private NodeHandler nodeHandler;
    private UserConfig userConfig;

    @Autowired
    public NodeService(NodeHandler nodeHandler,
                       UserConfig userConfig) {
        this.nodeHandler = nodeHandler;
        this.userConfig = userConfig;
    }

    public ResponseMessage getCluster(){
        try {
            nodeHandler.getCluster();
        }catch (CustomException e){
            return ResultUtil.error(e.getCode(),e.getMsg());
        }
        return ResultUtil.success();
    }

    public ResponseMessage exit(){
        nodeHandler.exit();
        return ResultUtil.success();
    }

    public ResponseMessage listNode() {
        try {
            List<Node> result = nodeHandler.listNode();
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.error(ResultEnum.NOT_FIND);
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }
    }

    public ResponseMessage postTokenAndSha(TokenAndSha update) {
        try {
            return ResultUtil.success(nodeHandler.postTokenAndSha(update));
        } catch (CustomException e) {
            return ResultUtil.error(e.getCode(), e.getMsg());
        }
    }

    public ResponseMessage master(VmInfo vmInfo) {
        nodeHandler.addVm(vmInfo);
        new KubeClient(vmInfo.getHostname());
        return ResultUtil.success();
    }

    public ResponseMessage createMaster(VmInfo vmInfo) {
        String privateDockerRepo = userConfig.getPrivateDockerRepo();
        String serverIp = userConfig.getServerIp();
        nodeHandler.addVm(vmInfo);
        try {
            // 下载kube_tool
            ConnectUtil.exec(vmInfo, CmdsConfig.getTool(vmInfo.getUsername()), true);
            // 清空一下
            ConnectUtil.exec(vmInfo,CmdsConfig.uninstall(vmInfo.getUsername()),true);
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
        nodeHandler.addVm(vmInfo);
        try {
            // 从git下载kube_tool
            ConnectUtil.exec(vmInfo, CmdsConfig.getTool(vmInfo.getUsername()), true);
            // 清空一下
            ConnectUtil.exec(vmInfo,CmdsConfig.uninstall(vmInfo.getUsername()),true);
            // 安装前先传一下token；
            VmInfo master = nodeHandler.getMaster();
            ConnectUtil.exec(master, CmdsConfig.postToken(master.getUsername(),serverIp), false);
            // 更新
            master = nodeHandler.getMaster();
            if (master.getToken() == null || master.getToken().equals("")) {
                // 创建并重传token
                ConnectUtil.exec(master, CmdsConfig.createPostToken(master.getUsername(),serverIp), false);
                master = nodeHandler.getMaster();
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

    public ResponseMessage removeNode(String name){
        try {
            V1Status result = nodeHandler.removeNode(name);
            VmInfo master = nodeHandler.getMaster();
            if (name.equals(master.getHostname())){
                nodeHandler.deleteAll();
            }else{
                nodeHandler.deleteVm(name);
            }
            nodeHandler.removeNode(name);
            return ResultUtil.success(result);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                return ResultUtil.success();
            }
            return ResultUtil.error(e.getCode(), e.getResponseBody());
        }
    }
//    public ResponseMessage getCluster(){
//        VmInfo vmInfo =
//        new KubeClient(vmInfo.getHostname());
//    }
}
