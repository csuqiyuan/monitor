package com.kubernetes.monitor.service;

import com.kubernetes.monitor.service.handler.CommonHandler;
import com.kubernetes.monitor.util.ResultUtil;
import com.kubernetes.monitor.util.response.ResponseMessage;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    private CommonHandler commonHandler;

    @Autowired
    public CommonService(CommonHandler commonHandler) {
        this.commonHandler = commonHandler;
    }

    public ResponseMessage getResourcesNums(){
        try{
            return ResultUtil.success(commonHandler.getResourcesNums());
        }catch (ApiException e){
            return ResultUtil.success();
        }
    }
}
