package com.kubernetes.monitor.controller;

import com.kubernetes.monitor.entity.TokenAndSha;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.service.NodeService;
import com.kubernetes.monitor.util.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis")
public class VmController {
    private NodeService nodeService;

    @Autowired
    public VmController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

//    @PostMapping("/vm")
//    public ResponseMessage addVm(@RequestBody VmInfo vmInfo) {
//        return vmService.addVm(vmInfo);
//    }

    @GetMapping("/cluster")
    public ResponseMessage getCluster(){
        return nodeService.getCluster();
    }

    @GetMapping("/exit")
    public ResponseMessage exit(){
        return nodeService.exit();
    }

    @DeleteMapping("/vm/{hostname}")
    public ResponseMessage deleteVmByHostname(@PathVariable String hostname) {
        return nodeService.deleteVm(hostname);
    }

    @GetMapping("/vm/list")
    public ResponseMessage listVm() {
        return nodeService.listVm();
    }

    @PostMapping("/callback/token")
    public ResponseMessage postTokenAndSha(TokenAndSha update) {
        return nodeService.postTokenAndSha(update);
    }

    @PostMapping("/master")
    public ResponseMessage master(@RequestBody VmInfo vmInfo) {
        System.out.println(vmInfo.toString());
        if (vmInfo.getIsMaster()==null||vmInfo.getIsMaster() != 1) {
            vmInfo.setIsMaster(1);
        }
        return nodeService.master(vmInfo);
    }

    @PostMapping("/create/master")
    public ResponseMessage createMaster(@RequestBody VmInfo vmInfo) {
        if (vmInfo.getIsMaster()==null||vmInfo.getIsMaster() != 1) {
            vmInfo.setIsMaster(1);
        }
        return nodeService.createMaster(vmInfo);
    }

    @PostMapping("/create/node")
    public ResponseMessage createNode(@RequestBody VmInfo vmInfo) {
        if (vmInfo.getIsMaster()==null||vmInfo.getIsMaster() != 0) {
            vmInfo.setIsMaster(0);
        }
        return nodeService.node(vmInfo);
    }
}
