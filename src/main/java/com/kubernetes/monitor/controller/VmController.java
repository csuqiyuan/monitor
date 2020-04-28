package com.kubernetes.monitor.controller;

import com.kubernetes.monitor.entity.TokenAndSha;
import com.kubernetes.monitor.entity.VmInfo;
import com.kubernetes.monitor.service.VmService;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis")
public class VmController {
    private VmService vmService;

    @Autowired
    public VmController(VmService vmService) {
        this.vmService = vmService;
    }

    @PostMapping("/vm")
    public ResponseMessage addVm(@RequestBody VmInfo vmInfo) {
        return vmService.addVm(vmInfo);
    }

    @DeleteMapping("/vm/{hostname}")
    public ResponseMessage deleteVmByHostname(@PathVariable String hostname) {
        return vmService.deleteVm(hostname);
    }

    @GetMapping("/vm/list")
    public ResponseMessage listVm() {
        return vmService.listVm();
    }

    @PostMapping("/callback/token")
    public ResponseMessage postTokenAndSha(TokenAndSha update) {
        return vmService.postTokenAndSha(update);
    }

    @PostMapping("/master")
    public ResponseMessage master(@RequestBody VmInfo vmInfo) {
        if (vmInfo.getIsMaster() != 0) {
            vmInfo.setIsMaster(0);
        }
        return ResultUtil.success();
    }
}
