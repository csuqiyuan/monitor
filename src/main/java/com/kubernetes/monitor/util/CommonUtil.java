package com.kubernetes.monitor.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.kubernetes.monitor.util.response.ResponseMessage;

public class CommonUtil {
    public static ResponseMessage<JSONObject> toJsonObject(Object object){
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("intValue");
        filter.getExcludes().add("strValue");
        String objJson = JSON.toJSONString(object,filter);
        return ResultUtil.success(JSON.parseObject(objJson));
    }
}
