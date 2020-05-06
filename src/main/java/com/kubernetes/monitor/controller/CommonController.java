package com.kubernetes.monitor.controller;

import com.kubernetes.monitor.service.CommonService;
import com.kubernetes.monitor.util.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {
    private CommonService commonService;

    @Autowired
    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/resources/nums")
    public ResponseMessage getResourcesNums(){
        return commonService.getResourcesNums();
    }
}
