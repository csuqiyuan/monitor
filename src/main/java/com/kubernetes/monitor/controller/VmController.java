package com.kubernetes.monitor.controller;

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
    public VmController(VmService vmService){
        this.vmService = vmService;
    }

    @PostMapping("/vm")
    public ResponseMessage addVm(@RequestBody VmInfo vmInfo){
        VmInfo result = vmService.addVm(vmInfo);
        return ResultUtil.success(result);
    }
    @DeleteMapping("/vm/{hostname}")
    public ResponseMessage deleteVmByHostname(@PathVariable String hostname){
        vmService.deleteVm(hostname);
        return ResultUtil.success();
    }
}
