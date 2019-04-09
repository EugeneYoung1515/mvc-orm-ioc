package com.smart.core.mvc.annotationhandlers;

import com.alibaba.fastjson.JSON;

public class ResponseBodyHandler {
    public String process(Object o){
        return JSON.toJSONString(o);
    }
}
